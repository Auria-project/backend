package com.korit.perfume.dto.account;

import com.korit.perfume.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeNicknameReqDto {
    private Integer userId;
    private String nickname;

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .nickname(nickname)
                .build();
    }
}