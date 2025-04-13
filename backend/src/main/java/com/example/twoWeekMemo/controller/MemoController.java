package com.example.twoWeekMemo.controller;

import com.example.twoWeekMemo.model.Memo;
import com.example.twoWeekMemo.service.MemoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memos")
public class MemoController {

    private final MemoService memoService;

    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    // メモ一覧取得
    @GetMapping
    public List<Memo> getAllMemos() {
        return memoService.getAllMemos();
    }

    // メモ新規作成
    @PostMapping
    public Memo createMemo(@RequestBody Memo memo, HttpServletRequest request) {
        return memoService.createMemoForAuthenticatedUser(memo, request);
    }
    // メモ新規作成時に利用（メモ作成で中身が空で登録される対策）
    @GetMapping("/{id}")
    public Memo getMemoById(@PathVariable Long id) {
        return memoService.getMemoById(id);
    }

    // メモ更新
    @PutMapping("/{id}")
    public void updateMemo(@PathVariable Long id, @RequestBody Memo memo) {
        memo.setId(id);
        memoService.updateMemo(memo);
    }

    // メモ削除
    @DeleteMapping("/{id}")
    public void deleteMemo(@PathVariable Long id) {
        memoService.deleteMemo(id);
    }
}
