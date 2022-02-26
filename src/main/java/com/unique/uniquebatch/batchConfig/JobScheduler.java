package com.unique.uniquebatch.batchConfig;

import com.unique.uniquebatch.batchJob.TradeJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobScheduler {
    JobLauncher     jobLauncher;
    TradeJob        tradeJob;

    @Scheduled(cron = "0 0 13 * * *")
    public void insertTradeScheduler() {
        log.info("[ JobScheduler : insertTradeScheduler ] START");

        SimpleDateFormat    format      = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar            time        = Calendar.getInstance();
        String              format_time = format.format(time.getTime());
        Integer             intUnixTime = (int) System.currentTimeMillis() / 1000;

        log.debug("format === " + format);
        log.debug("time === " + time);
        log.debug("format_time === " + format_time);
        log.debug("intUnixTime === " + intUnixTime);

        JobParametersBuilder    jobParametersBuilder    = new JobParametersBuilder();
        jobParametersBuilder.addString("date", format_time + intUnixTime + "_trade");
        JobParameters           jobParameters           = jobParametersBuilder.toJobParameters();
        JobExecution            jobExecution            = null;

        jobExecution    = jobLauncher.run(tradeJob.);
    }

//    @Scheduled(cron = "")
//    public void calcTotalAmtScheduler() {
//        log.info("[ JobScheduler : calcTotalAmtScheduler ] START");
//
//    }

}

/**
 * 1. 주기적으로 유저별 거래내역 추가 : insertTradeList
 * 2. 유저별 총합산액 내역 추가      : insertTradeTotalAmt
 *    일자별 총 합산액  추가        : insertTotalAmtByDay
 */