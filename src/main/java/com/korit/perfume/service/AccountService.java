package com.korit.perfume.service;

import com.korit.perfume.dto.ApiRespDto;
import com.korit.perfume.dto.account.ChangeNicknameReqDto;
import com.korit.perfume.dto.account.ChangePasswordReqDto;
import com.korit.perfume.entity.User;
import com.korit.perfume.repository.UserRepository;
import com.korit.perfume.security.model.PrincipalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AccountService {


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    public ApiRespDto<?> changePassword(ChangePasswordReqDto changePasswordReqDto, PrincipalUser principalUser) {
        Optional<User> userByUserId = userRepository.getUserByUserId(principalUser.getUserId());
        if (userByUserId.isEmpty()) {
            return new ApiRespDto<>("failed", "존재하지 않는 사용자입니다.", null);

        }

        if (!Objects.equals(changePasswordReqDto.getUserId(), principalUser.getUserId())) {
            return new ApiRespDto<>("failed", "잘못된 요청입니다.", null);
        }

        if (!bCryptPasswordEncoder.matches(changePasswordReqDto.getOldPassword(), userByUserId.get().getPassword())) {
            return new ApiRespDto<>("failed", "기존 비밀번호가 일치하지 않습니다.", null);

        }

        if (bCryptPasswordEncoder.matches(changePasswordReqDto.getNewPassword(), userByUserId.get().getPassword())) {
            return new ApiRespDto<>("failed", "새 비밀번호는 기존 비밀번호와 달라야 합니다.", null);
        }

        int result = userRepository.changePassword(changePasswordReqDto.toEntity(bCryptPasswordEncoder));
        if (result != 1) {
            return new ApiRespDto<>("failed", "문제가 발생했습니다.", null);

        }

        return new ApiRespDto<>("success", "비밀번호 변경이 완료되었습니다.\n다시 로그인 해주세요.", null);


    }


    public ApiRespDto<?> changeNickname(ChangeNicknameReqDto changeNicknameReqDto, PrincipalUser principalUser) {
        Optional<User> userByUserId = userRepository.getUserByUserId(principalUser.getUserId());
        if (userByUserId.isEmpty()) {
            return new ApiRespDto<>("failed", "존재하지 않는 사용자입니다.", null);
        }

        if (!Objects.equals(changeNicknameReqDto.getUserId(), principalUser.getUserId())) {
            return new ApiRespDto<>("failed", "잘못된 요청입니다.", null);
        }

        // 닉네임 중복 체크
        Optional<User> userByNickname = userRepository.getUserByNickname(changeNicknameReqDto.getNewNickname());
        if (userByNickname.isPresent()) {
            return new ApiRespDto<>("failed", "이미 사용 중인 닉네임입니다.", null);
        }

        User userToUpdate = userByUserId.get();
        userToUpdate.setNickname(changeNicknameReqDto.getNewNickname());

        int result = userRepository.changeNickname(userToUpdate);
        if (result != 1) {
            return new ApiRespDto<>("failed", "닉네임 변경에 실패했습니다.", null);
        }

        return new ApiRespDto<>("success", "닉네임 변경이 완료되었습니다.", null);
    }
}

