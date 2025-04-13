package com.example.twoWeekMemo.config.security;

import com.example.twoWeekMemo.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // シークレットキー（本番ではもっと複雑にして管理）
    private final String secret = "this_is_a_sample_secret_key_for_jwt_which_should_be_long";
    
    // 有効期限（1時間だが、ここは本番では柔軟に変更）
    private final long expiration = 1000 * 60 * 60;

    // 署名キーを生成
    private final Key key = Keys.hmacShaKeyFor(secret.getBytes());

    // ユーザー情報からJWTトークンを生成（フロントに渡す用）
    public String generateToken(User user) {
        return Jwts.builder()
            .setSubject(user.getUsername()) // トークンに入れる情報（ここではusername）
            .setIssuedAt(new Date()) // 発行時間
            .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 有効期限
            .signWith(key, SignatureAlgorithm.HS256) // 署名
            .compact();
    }

    // トークンからユーザー名を取得（失敗したらnullを返す）
    public String validateAndGetUsername(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        } catch (JwtException e) {
            return null;
        }
    }
}
