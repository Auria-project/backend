package com.korit.perfume.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer userId;
    private String username;
    private String nickname;
    private String fullname;
    private String password;
    private String email;

    private String gender;
    private Integer age;

    private LocalDateTime createDt;
    private LocalDateTime updateDt;

    private List<UserRole> userRoles;
}