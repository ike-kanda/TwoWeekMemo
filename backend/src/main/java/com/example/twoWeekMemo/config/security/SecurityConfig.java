package com.example.twoWeekMemo.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .cors(Customizer.withDefaults())  // SpringBoot側のCORS許可
            .csrf(csrf -> csrf.disable()) // CSRF無効（API向け）
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // セッション使わない
            .authorizeHttpRequests(auth -> auth
                // 開発環境では認証不要なので、permitAll()
                .requestMatchers("/api/auth/**").permitAll() 

                // 本番環境ではログイン必須に切り替える（↓の行を有効にして、上のpermitAll()行をコメントアウト）
                // .requestMatchers("/api/auth/**").authenticated()
                
                // 他は全て認証必要
                .anyRequest().authenticated() 
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // JWTフィルターを組み込む
            .build();
    }
}
