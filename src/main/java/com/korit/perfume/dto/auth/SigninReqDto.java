package com.korit.perfume.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SigninReqDto {
    private String userid;
    private String password;
}
