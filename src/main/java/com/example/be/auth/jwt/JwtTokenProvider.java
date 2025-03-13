package com.example.be.auth.jwt;

import com.example.be.auth.entity.User;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;
    private final RedisTemplate<String, String> redisTemplate;
    private final long ACCESS_FIVE_MINUTES = 1000L * 60;
    private final long REFRESH_SEVEN_DAYS = 1000L * 60 * 60 * 24 * 7;

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(User user){
        Date now = new Date();

        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .claim("tokenType", "access")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_FIVE_MINUTES))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return token;
    }

    public String createRefreshToken(User user){
        Date now = new Date();

        String refreshToken = Jwts.builder()
                .setSubject(user.getEmail())
                .claim("tokenType", "refresh")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_SEVEN_DAYS))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        redisTemplate.delete(user.getEmail());
        redisTemplate.opsForHash().put(user.getEmail(), "refreshToken", refreshToken);
        redisTemplate.opsForHash().put(user.getEmail(), "createdAt", String.valueOf(now.getTime()));

        redisTemplate.expire(user.getEmail(), REFRESH_SEVEN_DAYS, TimeUnit.MILLISECONDS);

        return refreshToken;
    }

    public String getEmail(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request){
        return request.getHeader("Authorization").substring(7);
    }

    public boolean validateAccessToken(String accessToken){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken);
            return !claims.getBody().getExpiration().before(new Date()) && !"access".equals(claims.getBody().get("tokenType", String.class));
        }
        catch(ExpiredJwtException e){
            return false;
        }
    }

    public boolean validateRefreshToken(String refreshToken){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(refreshToken);
            return !claims.getBody().getExpiration().before(new Date()) && !"refresh".equals(claims.getBody().get("tokenType", String.class));
        }
        catch(ExpiredJwtException e){
            return false;
        }
    }
}
