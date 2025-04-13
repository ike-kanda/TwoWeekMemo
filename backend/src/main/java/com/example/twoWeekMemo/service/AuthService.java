package com.example.twoWeekMemo.service;

import com.example.twoWeekMemo.mapper.UserMapper;
import com.example.twoWeekMemo.model.User;
import com.example.twoWeekMemo.config.security.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public AuthService(UserMapper userMapper, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    public String login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return jwtUtil.generateToken(user);
        }
        return null;
    }

    public void register(User user) {
        userMapper.createUser(user);
    }
}
