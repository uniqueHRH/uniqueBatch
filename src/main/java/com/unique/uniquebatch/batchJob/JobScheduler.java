package com.unique.uniquebatch.batchJob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobScheduler {
    JobLauncher     jobLauncher;

    @Scheduled()
    public void insertTradeScheduler() {
        log.info("[ JobScheduler : insertTradeScheduler ] START");

        
    }

}
