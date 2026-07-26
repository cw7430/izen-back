package com.izen.module.auth;

import com.izen.common.api.exception.CustomException;
import com.izen.common.api.type.ResponseCode;
import com.izen.common.config.security.JwtProvider;
import com.izen.common.config.security.JwtUtil;
import com.izen.common.config.security.type.TokenResponseClaim;
import com.izen.module.auth.dto.request.LoginRequestDto;
import com.izen.module.auth.dto.request.LogoutRequestDto;
import com.izen.module.auth.dto.request.RefreshRequestDto;
import com.izen.module.auth.dto.response.LoginResponseDto;
import com.izen.module.auth.dto.vo.LoginVo;
import com.izen.module.auth.dto.vo.RefreshTokenVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthMapper authMapper;
    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private LoginResponseDto issueTokensAndBuild(LoginVo loginVo, boolean isAuto) {
        TokenResponseClaim accessClaims = jwtProvider.generateAccessToken(
                loginVo.accountId().toString(), loginVo.authRole().toString()
        );

        TokenResponseClaim refreshClaims = jwtProvider.generateRefreshToken(
                loginVo.accountId().toString(), isAuto
        );

        long refreshTokenExpiresAtMs = refreshClaims.expiresAtMs();
        Instant refreshTokenExpiresAtDate = Instant.ofEpochMilli(refreshTokenExpiresAtMs);

        int insertRefreshTokenCnt = authMapper.createRefreshToken(
                loginVo.accountId(),
                refreshClaims.token(),
                refreshTokenExpiresAtDate
        );

        if (insertRefreshTokenCnt == 0) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        return new LoginResponseDto(
                accessClaims.token(), accessClaims.expiresAtMs(), refreshClaims.token(), refreshTokenExpiresAtMs,
                isAuto, loginVo.employeeCode(), loginVo.employeeName(), loginVo.authRole(), loginVo.employeeRole(),
                loginVo.departmentCode(), loginVo.teamCode(), loginVo.positionCode()
        );
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto reqDto) {
        LoginVo info = authMapper.findLoginInfo(reqDto)
                .orElseThrow(() -> new CustomException(ResponseCode.LOGIN_ERROR));

        if (!passwordEncoder.matches(reqDto.password(), info.passwordHash())) {
            throw new CustomException(ResponseCode.LOGIN_ERROR);
        }

        log.info("Login In successfully for account ID:{}", info.accountId());

        return issueTokensAndBuild(info, reqDto.isAuto());
    }

    @Transactional
    public LoginResponseDto refresh(HttpServletRequest req, RefreshRequestDto reqDto) {
        String refreshToken = jwtUtil.extractToken(req);
        Long accountId = jwtUtil.extractUserIdFromRefresh(refreshToken);

        LoginVo refreshInfo = authMapper.findRefreshInfo(accountId)
                .orElseThrow(() -> new CustomException(ResponseCode.UNAUTHORIZED));
        RefreshTokenVo tokenInfo = authMapper.findRefreshToken(accountId, refreshToken)
                .orElseThrow(() -> new CustomException(ResponseCode.UNAUTHORIZED));

        authMapper.deleteRefreshTokenById(tokenInfo.refreshTokenId());

        log.info("Refresh successfully for account ID:{}", accountId);

        return issueTokensAndBuild(refreshInfo, reqDto.isAuto());
    }

    public void logout(LogoutRequestDto reqDto) {
        if(reqDto.refreshToken() == null) {
            return;
        }

        authMapper.deleteRefreshTokenByToken(reqDto.refreshToken());
    }
}
