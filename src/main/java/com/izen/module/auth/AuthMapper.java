package com.izen.module.auth;

import com.izen.module.auth.dto.request.LoginRequestDto;
import com.izen.module.auth.dto.vo.LoginVo;
import com.izen.module.auth.dto.vo.RefreshTokenVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.Instant;
import java.util.Optional;

@Mapper
public interface AuthMapper {
    Optional<LoginVo> findLoginInfo(LoginRequestDto loginRequestDto);

    Optional<LoginVo> findRefreshInfo(@Param("accountId") Long accountId);

    Optional<RefreshTokenVo> findRefreshToken(
            @Param("accountId") Long accountId,
            @Param("refreshToken") String refreshToken
    );

    int createRefreshToken(
            @Param("accountId") Long accountId,
            @Param("refreshToken") String refreshToken,
            @Param("expiresAt") Instant expiresAt
    );

    void deleteRefreshTokenById(@Param("refreshTokenId") Long refreshTokenId);

    void deleteRefreshTokenByToken(@Param("refreshToken") String refreshToken);
}
