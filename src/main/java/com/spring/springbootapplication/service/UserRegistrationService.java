package com.spring.springbootapplication.service;

import com.spring.springbootapplication.domain.User;
import com.spring.springbootapplication.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserRegistrationService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User register(String name, String email, String rawPassword) {
        if (userMapper.countByEmail(email) > 0) {
            throw new IllegalArgumentException("このメールアドレスは既に登録されています");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));

        userMapper.insert(user);
        return user;
    }
}