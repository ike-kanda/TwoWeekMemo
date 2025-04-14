package com.example.twoWeekMemo.config.batch;

import com.example.twoWeekMemo.mapper.MemoMapper;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

// 動作確認未実施
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final MemoMapper memoMapper;

    public BatchConfig(MemoMapper memoMapper) {
        this.memoMapper = memoMapper;
    }

    /**
     * バッチジョブの定義
     * - JobRepositoryはジョブの実行状態を管理
     * - deleteOldMemosStep（後述）を1つだけ含むシンプルなジョブ
     */
    @Bean
    public Job deleteOldMemosJob(JobRepository jobRepository, Step deleteOldMemosStep) {
        return new JobBuilder("deleteOldMemosJob", jobRepository)
                .start(deleteOldMemosStep) // このジョブでは1ステップだけ実行
                .build();
    }

    /**
     * ステップの定義（タスクレットベース）
     * - updated_atが2週間以上前のメモを削除する処理
     * - PlatformTransactionManagerを指定することでDB操作がトランザクション管理される
     */
    @Bean
    public Step deleteOldMemosStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("deleteOldMemosStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    int deleted = memoMapper.deleteMemosOlderThan14Days(); // MyBatisで削除実行
                    System.out.println(deleted + " 件の古いメモを削除しました");
                    return RepeatStatus.FINISHED; // 一回で終わるタスク
                }, transactionManager)
                .build();
    }
}
