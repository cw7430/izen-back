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
    Optional<LoginVo> findLoginInfo(LoginRequestDto dto);

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

    boolean existsByUserName(@Param("userName") String userName);

    Optional<String> findPasswordHash(@Param("accountId") Long accountId);

    int updateUserName(@Param("accountId") Long accountId, @Param("userName") String userName);

    int updatePassword(@Param("accountId") Long accountId, @Param("passwordHash") String passwordHash);

    int updateAccount(
            @Param("accountId") Long accountId,
            @Param("phone") String phone,
            @Param("email") String email
    );

    int createAccount(
            @Param("userName") String userName,
            @Param("passwordHash") String passwordHash,
            @Param("phone") String phone,
            @Param("email") String email
    );
}
