package com.yilly.lims.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    // ⚠️ 替换为安全的32+字节密钥（建议放配置）
    private final Key key = Keys.hmacShaKeyFor("CHANGE_THIS_TO_A_32+_BYTE_SECRET_KEY_123456".getBytes());
    private final long EXP_MS = 1000L * 60 * 60 * 12;

    public String generate(String subject, Map<String, Object> claims) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXP_MS))
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}
