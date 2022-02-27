package com.unique.uniquebatch.batchConfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

@Slf4j
public class uniqueRunIdIncrementer extends RunIdIncrementer {
    private static final String RUN_ID = "run.id";

    public JobParameters getNext(JobParameters parameters) {
        JobParameters           params  = (parameters == null) ? new JobParameters() : parameters;
        JobParametersBuilder    builder = new JobParametersBuilder();
        builder.addString(RUN_ID, params.getLong(RUN_ID, 0L) + "_unique");
        builder.toJobParameters();
        log.info("parameter 출력 === " + params);
        log.info("parameter builder 출력 === " + builder);

        return params;
    }
}
