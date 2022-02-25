package com.unique.uniquebatch.batchJob;

import com.unique.uniquebatch.batchConfig.ExploringTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;

/**
 * Batch - JobExplorer
 */
@RequiredArgsConstructor
public class TestJob2 {
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private JobExplorer jobExplorer;

    @Bean
    public Job explorerJob() {
        return this.jobBuilderFactory.get("explorerJob")
                .start(explorerStep())
                .build();
    }

    @Bean
    public Step explorerStep() {
        return this.stepBuilderFactory.get("explorerStep")
                .tasklet(explorerTasklet())
                .build();
    }

    @Bean
    public Tasklet explorerTasklet() {
        return new ExploringTasklet(this.jobExplorer);
    }
}
