package com.korit.perfume.controller;

import com.korit.perfume.dto.auth.SignupReqDto;
import com.korit.perfume.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signup")
public class SignupController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<?> signup(@RequestBody SignupReqDto request) {
        try {
            userService.registerUser(request);
            return ResponseEntity.ok("회원가입 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        }
    }
}
