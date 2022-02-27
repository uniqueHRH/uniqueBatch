package com.unique.uniquebatch.batchConfig;

import com.unique.uniquebatch.batchJob.TradeJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Slf4j
@Component
public class JobScheduler {
    @Autowired
    private JobLauncher     jobLauncher;
    @Autowired
    private TradeJob        tradeJob;

    @Scheduled(cron = "0 0 13 * * *")
    public void insertTradeScheduler() throws JobExecutionAlreadyRunningException, JobRestartException {
        log.info("[ JobScheduler : insertTradeScheduler ] START");

        SimpleDateFormat    format      = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar            time        = Calendar.getInstance();
        String              format_time = format.format(time.getTime());
        Integer             intUnixTime = (int) System.currentTimeMillis() / 1000;

        log.debug("format === " + format);
        log.debug("time === " + time);
        log.debug("format_time === " + format_time);
        log.debug("intUnixTime === " + intUnixTime);

        JobParametersBuilder jobParametersBuilder    = new JobParametersBuilder();
        jobParametersBuilder.addString("date", format_time + intUnixTime + "_trade");
        JobParameters jobParameter           = jobParametersBuilder.toJobParameters();
        JobExecution jobExecution            = null;

        try {
            jobExecution = jobLauncher.run(tradeJob.calcTradeTotalAmtJob(), jobParameter);
            JobInstance instance = jobExecution.getJobInstance();

            if (jobExecution.getExitStatus().equals("isUnsuccessful")) {
                log.info("##### job id(parameter) === " + jobExecution.getId() + "("+jobParameter + ") Batch Fail");
                log.info("##### instanceId ========== " + instance.getInstanceId());
                log.info("##### jobName ============= " + instance.getJobName());
            }
            log.info("jobExecution finished exit cod ##### " + jobExecution.getExitStatus());
        } catch (JobInstanceAlreadyCompleteException | IllegalStateException | JobParametersInvalidException e) {
            log.debug("########## " + jobParameter + " BATCH RUN ERROR #########");
            e.printStackTrace();
        }
    }

//    @Scheduled(cron = "")
//    public void calcTotalAmtScheduler() {
//        log.info("[ JobScheduler : calcTotalAmtScheduler ] START");
//
//    }

}

/**
 * 유저별 총합산액 내역 추가      : calcTradeTotalAmtJob
 * 일자별 총 합산액  추가        : insertTotalAmtByDay
 */