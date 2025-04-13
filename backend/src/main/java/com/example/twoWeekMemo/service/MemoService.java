package com.example.twoWeekMemo.service;

import jakarta.servlet.http.HttpServletRequest;
import com.example.twoWeekMemo.mapper.MemoMapper;
import com.example.twoWeekMemo.model.Memo;
import com.example.twoWeekMemo.model.User;
import com.example.twoWeekMemo.mapper.UserMapper;
import com.example.twoWeekMemo.config.security.JwtUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MemoService {

    private final MemoMapper memoMapper;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    
    public MemoService(MemoMapper memoMapper, JwtUtil jwtUtil, UserMapper userMapper) {
        this.memoMapper = memoMapper;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
    }

    public List<Memo> getAllMemos() {
        return memoMapper.findAll();
    }

    public Memo createMemoForAuthenticatedUser(Memo memo, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            String username = jwtUtil.validateAndGetUsername(jwt);
            User user = userMapper.findByUsername(username);
            memo.setUserId(user.getId());
        }
        memo.setCreatedAt(LocalDateTime.now());
        memo.setUpdatedAt(LocalDateTime.now());
        memoMapper.insert(memo);
        return memo;
    }

    public Memo getMemoById(Long id) {
        return memoMapper.findById(id);
    }

    public void updateMemo(Memo memo) {
        memo.setUpdatedAt(LocalDateTime.now());
        memoMapper.update(memo);
    }

    public void deleteMemo(Long id) {
        memoMapper.delete(id);
    }
}
