package com.korit.perfume.service;

import com.korit.perfume.dto.auth.SignupReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public void registerUser(SignupReqDto request) {
        // 중복 검사 예시
        if(userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("이미 사용중인 아이디입니다.");
        if(userRepository.existsByNickname(request.getNickname()))
            throw new RuntimeException("이미 사용중인 닉네임입니다.");
        if(userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("이미 등록된 이메일입니다.");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setGender(request.getGender());
        user.setAge(request.getAge());
        // perfumePreferences JSON/별도 객체로 저장

        userRepository.save(user);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 올바르지 않습니다.");
        }

        return jwtTokenProvider.createToken(user.getUsername(), user.getNickname());
    }

    public String getNickname(String username) {
        return userRepository.findByUsername(username)
                .map(User::getNickname)
                .orElse(null);
    }
}
