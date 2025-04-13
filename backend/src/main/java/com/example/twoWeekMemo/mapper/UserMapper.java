package com.example.twoWeekMemo.mapper;

import com.example.twoWeekMemo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findByUsername(@Param("username") String username);
    void createUser(User user);
}