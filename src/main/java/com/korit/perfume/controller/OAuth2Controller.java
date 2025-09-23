package com.korit.perfume.controller;

import com.korit.perfume.dto.oauth2.OAuth2MergeReqDto;
import com.korit.perfume.dto.oauth2.OAuth2SignupReqDto;
import com.korit.perfume.service.OAuth2AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @Autowired
    private OAuth2AuthService oAuth2AuthService;

    @PostMapping("/merge")
    public ResponseEntity<?> mergeAccount(@RequestBody OAuth2MergeReqDto dto) {
        try {
            return ResponseEntity.ok(oAuth2AuthService.mergeAccount(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("계정 병합 실패: " + e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody OAuth2SignupReqDto dto) {
        try {
            return ResponseEntity.ok(oAuth2AuthService.signup(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("OAuth2 회원가입 실패: " + e.getMessage());
        }
    }
}
