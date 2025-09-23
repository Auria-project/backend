package com.korit.perfume.controller;

import com.korit.perfume.dto.ApiRespDto;
import com.korit.perfume.dto.auth.SigninReqDto;
import com.korit.perfume.dto.auth.SignupReqDto;
import com.korit.perfume.security.model.PrincipalUser;
import com.korit.perfume.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/principal")
    public ResponseEntity<ApiRespDto<PrincipalUser>> getPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        ApiRespDto<PrincipalUser> apiRespDto = new ApiRespDto<>("success", "", principalUser);
        return ResponseEntity.ok(apiRespDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiRespDto<?>> signup(@RequestBody SignupReqDto signupReqDto) {
        try {
            return ResponseEntity.ok(authService.signup(signupReqDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiRespDto<>("fail", e.getMessage(), null));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiRespDto<?>> signin(@RequestBody SigninReqDto signinReqDto) {
        try {
            return ResponseEntity.ok(authService.signin(signinReqDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiRespDto<>("fail", e.getMessage(), null));
        }
    }
}
