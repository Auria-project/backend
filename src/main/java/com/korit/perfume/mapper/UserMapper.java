package com.korit.perfume.mapper;

import com.korit.perfume.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {
    int addUser(User user);
    Optional<User> getUserByUserId(Integer userId);
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByfullname(String fullname);
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByNickname(String nickname);

    int updatePassword(User user);
    int updateProfileImg(User user);
    int updateNickname(User user);
}
