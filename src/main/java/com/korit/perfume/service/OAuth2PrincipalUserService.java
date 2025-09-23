package com.korit.perfume.service;

import com.korit.perfume.entity.OAuth2User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OAuth2PrincipalUserService extends DefaultOAuth2UserService {

    @Override
    public org.springframework.security.oauth2.core.user.OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String provider = userRequest.getClientRegistration().getRegistrationId();
        System.out.println("provider : " + provider);

        String id;
        String email;

        if ("google".equals(provider)) {
            id = attributes.get("sub").toString();
            email = (String) attributes.get("email");
        } else {
            throw new OAuth2AuthenticationException("Only Google login is supported.");
        }

        Map<String, Object> newAttributes = Map.of(
                "id", id,
                "provider", provider,
                "email", email
        );

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_TEMPORARY"));

        return new DefaultOAuth2User(authorities, newAttributes, "id");
    }
}
