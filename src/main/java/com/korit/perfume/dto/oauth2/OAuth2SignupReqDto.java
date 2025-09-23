package com.korit.perfume.dto.oauth2;

import com.korit.perfume.entity.OAuth2User;
import com.korit.perfume.entity.User;
import lombok.Data;

@Data
public class OAuth2SignupReqDto {
    private String username;
    private String nickname;
    private String password;
    private String email;
    private String provider;
    private String providerUserId;

    public User toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return User.builder()
                .username(username)
                .nickname(nickname)
                .password(bCryptPasswordEncoder.encode(password))
                .email(email)
                .build();
    }

    public OAuth2User toOAuth2User(Integer userId) {
        return OAuth2User.builder()
                .userId(userId)
                .provider(provider)
                .providerUserId(providerUserId)
                .nickname(nickname)
                .build();
    }
}
