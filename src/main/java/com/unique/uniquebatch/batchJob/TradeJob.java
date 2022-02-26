package com.unique.uniquebatch.batchJob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TradeJob {
    private JobBuilderFactory   jobBuilderFactory;
    private StepBuilderFactory  stepBuilderFactory;

    @Bean
    public Job insertTradeListJob() {
        log.info("[ TradeJob - insertTradeListJob ] START");

        return this.jobBuilderFactory.get("insertTradeListJob")
                .incrementer()
                .start()
                .build();
    }

    @Bean
    public Step insertTradeListStep() {
        log.info("[ TradeJob - insertTradeListStep ] START");

        return this.stepBuilderFactory.get("insertTradeListStep")
                .tasklet()
                .build();
    }

    @Bean
    public Tasklet insertTradeListTasklet() {
        return () -> {

        };
    }
}
