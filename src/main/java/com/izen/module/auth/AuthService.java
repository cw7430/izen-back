package com.izen.module.auth;

import com.izen.common.api.exception.CustomException;
import com.izen.common.api.type.ResponseCode;
import com.izen.common.config.security.JwtProvider;
import com.izen.common.config.security.JwtUtil;
import com.izen.common.config.security.type.TokenResponseClaim;
import com.izen.module.auth.dto.request.*;
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
        if (reqDto.refreshToken() == null) {
            return;
        }

        authMapper.deleteRefreshTokenByToken(reqDto.refreshToken());
    }

    public void checkUserDuplicate(CheckUserRequestDto reqDto) {
        boolean isDuplicate = authMapper.existsByUserName(reqDto.userName());
        if (isDuplicate) {
            throw new CustomException(ResponseCode.DUPLICATE_RESOURCE);
        }

        log.info("Check User successfully for user name:{}", reqDto.userName());
    }

    @Transactional
    public void updateUserName(UpdateUserNameRequestDto reqDto) {
        Long accountId = jwtUtil.getCurrentUserId();
        boolean isDuplicate = authMapper.existsByUserName(reqDto.userName());
        if (isDuplicate) {
            throw new CustomException(ResponseCode.DUPLICATE_RESOURCE);
        }

        String passwordHash = authMapper.findPasswordHash(accountId)
                .orElseThrow(() -> new CustomException(ResponseCode.UNAUTHORIZED));

        if (passwordEncoder.matches(reqDto.password(), passwordHash)) {
            throw new CustomException(ResponseCode.PASSWORD_ERROR);
        }

        int updateCnt = authMapper.updateUserName(accountId, reqDto.userName());
        if (updateCnt == 0) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        log.info("Update User successfully for account ID:{}", accountId);
    }

    @Transactional
    public void updatePassword(UpdatePasswordRequestDto reqDto) {
        Long accountId = jwtUtil.getCurrentUserId();
        String passwordHash = authMapper.findPasswordHash(accountId)
                .orElseThrow(() -> new CustomException(ResponseCode.UNAUTHORIZED));

        if (passwordEncoder.matches(reqDto.prevPassword(), passwordHash)) {
            throw new CustomException(ResponseCode.PASSWORD_ERROR);
        }

        int updateCnt = authMapper.updatePassword(accountId, reqDto.prevPassword());
        if (updateCnt == 0) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        log.info("Update Password successfully for account ID:{}", accountId);
    }

    @Transactional
    public void updateAccount(UpdateAccountRequestDto reqDto) {
        Long accountId = jwtUtil.getCurrentUserId();
        String passwordHash = authMapper.findPasswordHash(accountId)
                .orElseThrow(() -> new CustomException(ResponseCode.UNAUTHORIZED));

        if (passwordEncoder.matches(reqDto.password(), passwordHash)) {
            throw new CustomException(ResponseCode.PASSWORD_ERROR);
        }

        int updateCnt = authMapper.updateAccount(accountId, reqDto.phone(), reqDto.email());
        if (updateCnt == 0) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        
        log.info("Update Account successfully for account ID:{}", accountId);
    }
}
