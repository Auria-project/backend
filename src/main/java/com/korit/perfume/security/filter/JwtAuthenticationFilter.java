package com.korit.perfume.security.filter;

import com.korit.perfume.entity.User;
import com.korit.perfume.repository.UserRepository;
import com.korit.perfume.security.jwt.JwtUtils;
import com.korit.perfume.security.model.PrincipalUser;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter implements Filter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        List<String> methods = List.of("POST", "GET", "PUT", "PATCH", "DELETE");
        if (!methods.contains(request.getMethod())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String authorization = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authorization);

        if (authorization == null || !jwtUtils.isBearer(authorization)) {
            // 인증 헤더 없으면 인증이 필요한 경로는 Security 설정에서 처리하므로 넘어감
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String accessToken = jwtUtils.removeBearer(authorization);

        try {
            // 토큰에서 클레임 추출
            var claims = jwtUtils.getClaims(accessToken);
            String id = claims.getId();
            Integer userId = Integer.parseInt(id);

            Optional<User> optionalUser = userRepository.getUserByUserId(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                PrincipalUser principalUser = PrincipalUser.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .nickname(user.getNickname())
                        .password(user.getPassword())
                        .email(user.getEmail())
                        .userRoles(user.getUserRoles())
                        .build();

                Authentication authentication = new UsernamePasswordAuthenticationToken(principalUser, "", principalUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"status\":\"fail\",\"message\":\"인증 실패: 사용자 정보를 찾을 수 없습니다.\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"status\":\"fail\",\"message\":\"토큰이 유효하지 않거나 만료되었습니다.\"}");
        }
    }
}
