package com.unique.uniquebatch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableBatchProcessing
@SpringBootApplication
@MapperScan(basePackages = "com.unique.uniquebatch.mapper")
public class UniqueBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniqueBatchApplication.class, args);
    }

}

/**
 * Cursor & Paging
 * [ Cursor ]
 * -> 하나의 Connection 으로 Batch 가 끝날 때까지 사용되기 때문에, Batch 종료 전 connection 이 먼저 끊어질 수 있음
 * -> Batch 수행 시간이 오래 걸리는 경우, Paging 을 사용하는 것이 유리
 *
 * [ Paging ]
 * -> 한 페이지를 읽을 때마다 Connection 을 맺고 끊기 때문에, 타임아웃과 부하없이 수행
 * -> 여러 쿼리를 실행하여 각 쿼리가 결과의 일부를 가져 온다 (개별 실행)
 * -> PageSize 에 맞게 offset & limit 이 자동 생성된다
 * -> 실행 결과의 순서가 보장될 수 있도록 정렬 (order) 사용 필수
 */