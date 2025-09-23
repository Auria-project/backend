package com.korit.perfume.repository;

import com.korit.perfume.entity.User;
import com.korit.perfume.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.util.Optional;

@Repository
public class UserRepository {

    @Autowired
    private UserMapper userMapper;

    public Optional<User> addUser(User user) {
        try {
            userMapper.addUser(user);
        } catch (DuplicateKeyException e) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public Optional<User> getUserByUserId(Integer userId) {
        return userMapper.getUserByUserId(userId);
    }

    public Optional<User> getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    public Optional<User> getUserByNickname(String nickname) {
        return userMapper.getUserByNickname(nickname);
    }

    public int changePassword(User user) {
        return userMapper.updatePassword(user);
    }


    public int changeNickname(User user) {
        return userMapper.updateNickname(user);
    }
}
