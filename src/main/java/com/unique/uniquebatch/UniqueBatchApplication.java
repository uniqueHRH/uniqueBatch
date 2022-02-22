package com.unique.uniquebatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing  // batch 사용 선언
@SpringBootApplication
public class UniqueBatchApplication {
    @Autowired
    private JobBuilderFactory jobFactory;
    @Autowired
    private StepBuilderFactory stepFactory;

    @Bean
    public Job job() {
        return this.jobFactory.get("job")
                .start(step())
                .build();
    }

    @Bean
    public Step step() {
        return this.stepFactory.get("step1")
                .tasklet(new Tasklet() {

                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("Hello, World!");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(UniqueBatchApplication.class, args);
    }

}
