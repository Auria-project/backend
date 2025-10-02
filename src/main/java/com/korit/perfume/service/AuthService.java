package com.korit.perfume.service;

import com.korit.perfume.dto.ApiRespDto;
import com.korit.perfume.dto.auth.SigninReqDto;
import com.korit.perfume.dto.auth.SignupReqDto;
import com.korit.perfume.entity.User;
import com.korit.perfume.entity.UserRole;
import com.korit.perfume.repository.UserRepository;
import com.korit.perfume.repository.UserRoleRepository;
import com.korit.perfume.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> signup(SignupReqDto signupReqDto) {
        Optional<User> userByUsername = userRepository.getUserByUsername(signupReqDto.getUsername());
        if (userByUsername.isPresent()) {
            return new ApiRespDto<>("failed", "이미 사용 중인 아이디입니다.", null);
        }

        Optional<User> userByEmail = userRepository.getUserByEmail(signupReqDto.getEmail());
        if (userByEmail.isPresent()) {
            return new ApiRespDto<>("failed", "이미 사용 중인 이메일입니다.", null);
        }

        Optional<User> userByNickname = userRepository.getUserByNickname(signupReqDto.getNickname());
        if (userByNickname.isPresent()) {
            return new ApiRespDto<>("failed", "이미 사용 중인 닉네임입니다.", null);
        }

        try {
            Optional<User> optionalUser = userRepository.addUser(signupReqDto.toEntity(bCryptPasswordEncoder));

            if (optionalUser.isEmpty()) {
                throw new RuntimeException("회원 정보 추가에 실패했습니다.");
            }

            User user = optionalUser.get();

            UserRole userRole = UserRole.builder()
                    .userId(user.getUserId())
                    .roleId(3)
                    .build();

            int addUserRoleResult = userRoleRepository.addUserRole(userRole);
            if (addUserRoleResult != 1) {
                throw new RuntimeException("권한 추가에 실패했습니다.");
            }

            return new ApiRespDto<>("success", "회원가입이 성공적으로 완료되었습니다.", user);

        } catch (Exception e) {
            return new ApiRespDto<>("failed", "회원가입 중 오류가 발생했습니다. :" + e.getMessage(), null);
        }
    }

    public ApiRespDto<?> signin(SigninReqDto signinReqDto) {
        Optional<User> optionalUser = userRepository.getUserByUsername(signinReqDto.getUsername());
        if (optionalUser.isEmpty()) {
            return new ApiRespDto<>("failed", "아이디 또는 비밀번호가 일치하지 않습니다.", null);
        }

        User user = optionalUser.get();

        if (!bCryptPasswordEncoder.matches(signinReqDto.getPassword(), user.getPassword())) {
            return new ApiRespDto<>("failed", "아이디 또는 비밀번호가 일치하지 않습니다.", null);
        }

        String accessToken = jwtUtils.generateAccessToken(user.getUserId().toString());
        return new ApiRespDto<>("success", "로그인이 성공적으로 완료되었습니다.", accessToken);
    }
}
