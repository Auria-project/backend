package com.korit.perfume.dto;

import com.korit.perfume.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupReqDto {

    private String fullName;   // 유저 이름 (ex: 홍길동)
    private String username;   // 로그인용 아이디
    private String nickname;   // 커뮤니티 닉네임
    private String email;      // 이메일 주소
    private String password;   // 비밀번호

    private String gender;  // 성별
    private Integer age;    // 나이

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