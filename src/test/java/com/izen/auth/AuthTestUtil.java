package com.izen.auth;

import com.izen.module.auth.AuthMapper;
import com.izen.module.auth.AuthService;
import com.izen.module.auth.dto.request.LoginRequestDto;
import com.izen.module.auth.dto.response.LoginResponseDto;
import com.izen.module.auth.dto.vo.LoginVo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AuthTestUtil {

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private AuthService authService;

    @Value("${security.api-key}")
    private String apiKey;

    @Value("${jwt.access.secret}")
    private String accessSecretKey;

    @Value("${jwt.refresh.secret}")
    private String refreshSecretKey;

    public LoginResponseDto getTestToken(LoginRequestDto reqDto) {
        return authService.login(reqDto);
    }

    public String getTestApiKey() {
        return apiKey;
    }

    public String generateExpiredAccessToken(LoginRequestDto reqDto) {
        LoginVo loginData = authMapper.findLoginInfo(reqDto).orElseThrow();
        Date now = new Date();
        Date expiry = new Date(now.getTime() - 1);

        return Jwts.builder()
                .subject(loginData.accountId().toString())
                .claim("role", loginData.authRole().toString())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecretKey)), Jwts.SIG.HS256)
                .compact();
    }

    public String generateExpiredRefreshToken(LoginRequestDto reqDto) {
        LoginVo loginData = authMapper.findLoginInfo(reqDto).orElseThrow();
        Date now = new Date();
        Date expiry = new Date(now.getTime() - 1);

        return Jwts.builder()
                .subject(loginData.accountId().toString())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretKey)), Jwts.SIG.HS256)
                .compact();
    }
}
