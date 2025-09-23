package com.korit.perfume.config;

import com.korit.perfume.security.filter.JwtAuthenticationFilter;
import com.korit.perfume.security.handler.OAuth2SuccessHandler;
import com.korit.perfume.service.OAuth2PrincipalUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // JWT 인증 필터
    private final OAuth2PrincipalUserService oAuth2PrincipalUserService; // OAuth2 사용자 서비스
    private final OAuth2SuccessHandler oAuth2SuccessHandler; // OAuth2 로그인 성공 핸들러

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern(CorsConfiguration.ALL);
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults()); // CORS 설정 적용
        http.csrf(csrf -> csrf.disable()); // CSRF 비활성화
        http.formLogin(formLogin -> formLogin.disable()); // 폼 로그인 비활성화
        http.httpBasic(httpBasic -> httpBasic.disable()); // HTTP Basic 인증 비활성화
        http.logout(logout -> logout.disable()); // 로그아웃 비활성화

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션 무상태 정책

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/auth/**", "/oauth2/**", "/login/oauth2/**", "/mail/verify").permitAll();
            auth.anyRequest().authenticated();
        });

        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2PrincipalUserService))
                .successHandler(oAuth2SuccessHandler)
        );

        return http.build();
    }
}
