package com.unique.uniquebatch.batchJob;

import com.unique.uniquebatch.batchConfig.DailyJobTimestamper;
import com.unique.uniquebatch.batchConfig.ParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

/**
 * Batch - JobParametersValidator (Parameter 검증)
 * CompositeJobParametersValidator   : 여러개의 parameter 검증
 * DefaultJobParametersValidator(java.lang.string[] requiredKeys,   : 매개변수 키를 사용해 사 유효성 검사기 생성
 *                               java.lang.string[] optionalKeys)      -> requiredKeys  : 필요키
 *                                                                        optionalKeys  : 옵션 키
 * afterPropertiesSet                : 필수 키와 선택 키가 겹치지 않는지 체크
 */
public class TestJob3 {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public CompositeJobParametersValidator validator() {
        CompositeJobParametersValidator validator                       = new CompositeJobParametersValidator();
        DefaultJobParametersValidator   defaultJobParametersValidator   = new DefaultJobParametersValidator (
                new String[] {"fileName"},
                new String[] {"name", "currentDate"}
        );

        defaultJobParametersValidator.afterPropertiesSet();

        validator.setValidators(
                Arrays.asList(new ParameterValidator(),
                        defaultJobParametersValidator)
        );
        return validator;
    }

    @Bean
    public Job helloWorldJob() {
        return this.jobBuilderFactory.get("jelloWorldJob")
                .incrementer(new DailyJobTimestamper())
                .start(helloWorldStep())
                .build();
    }

    @Bean
    public Step helloWorldStep() {
        return this.stepBuilderFactory.get("helloWorldStep")
                .tasklet(hellowWorldTasklet(null, null))
                .build();
    }

    @StepScope
    @Bean
    public Tasklet hellowWorldTasklet(
            @Value("#{jobParameters['name']}")      String name,
            @Value("{jobParameters['fileName']}")   String fileName) {
        return (contribution, chunkContext) -> {
            System.out.println(String.format("Hello, %s!", name));
            System.out.println(String.format("fileName = %s", fileName));

            return RepeatStatus.FINISHED;
        };
    }
}
