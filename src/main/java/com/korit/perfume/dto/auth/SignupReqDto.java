package com.korit.perfume.dto.auth;

import com.korit.perfume.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupReqDto {

    private String fullName;
    private String username;
    private String nickname;
    private String email;
    private String password;

    public User toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return User.builder()
                .fullname(this.fullName)
                .username(this.username)
                .nickname(this.nickname)
                .email(this.email)
                .password(bCryptPasswordEncoder.encode(this.password))
                .build();
    }
}