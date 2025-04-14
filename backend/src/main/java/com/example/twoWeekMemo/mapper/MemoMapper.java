package com.example.twoWeekMemo.mapper;

import com.example.twoWeekMemo.model.Memo;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemoMapper {
    List<Memo> findByUserId(Long userId);
    void insert(Memo memo);
    void update(Memo memo);
    void delete(Long id);
    Memo findById(Long id);
    int deleteMemosOlderThan14Days();
}