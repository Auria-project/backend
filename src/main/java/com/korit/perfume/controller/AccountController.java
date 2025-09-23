package com.korit.perfume.controller;

import com.korit.perfume.dto.account.ChangeNicknameReqDto;
import com.korit.perfume.dto.account.ChangePasswordReqDto;
import com.korit.perfume.security.model.PrincipalUser;
import com.korit.perfume.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/change/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordReqDto dto, @AuthenticationPrincipal PrincipalUser principalUser) {
        try {
            accountService.changePassword(dto, principalUser);
            return ResponseEntity.ok("비밀번호 변경 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("비밀번호 변경 실패: " + e.getMessage());
        }
    }

    @PostMapping("/change/nickname")
    public ResponseEntity<?> changeNickname(@RequestBody ChangeNicknameReqDto dto, @AuthenticationPrincipal PrincipalUser principalUser) {
        try {
            accountService.changeNickname(dto, principalUser);
            return ResponseEntity.ok("닉네임 변경 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("닉네임 변경 실패: " + e.getMessage());
        }
    }
}
