package com.unique.uniquebatch.batchJob;

import com.unique.uniquebatch.batchConfig.uniqueRunIdIncrementer;
import com.unique.uniquebatch.user.TradeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TradeJob {
    private static final int CHUNKSIZE = 10;

    @Autowired
    private JobBuilderFactory   jobBuilderFactory;
    @Autowired
    private StepBuilderFactory  stepBuilderFactory;
    @Autowired
    private DataSource          dataSource;

    @Bean
    public Job calcTradeTotalAmtJob() {
        log.info("[ TradeJob - calcTradeTotalAmtJob ] START");

        Job job = this.jobBuilderFactory.get("calcTradeTotalAmtJob")
                .incrementer(new uniqueRunIdIncrementer())
                .start(calcTradeTotalAmtStep())
                .next(updateReflectChkStep())
                .build();
        job.isRestartable();
        return job;
    }

    @Bean
    public Step calcTradeTotalAmtStep() {
        log.info("[ TradeJob - calcTradeTotalAmtStep ] START");

        Step step = stepBuilderFactory.get("calcTradeTotalAmtStep")
                .allowStartIfComplete(true)
                .<TradeDTO, TradeDTO>chunk(CHUNKSIZE)
                .reader(calcTradeTotalAmtReader())
                .writer(calcTradeTotalAmtWriter())
                .build();
        return step;
    }

    @Bean
    public JdbcCursorItemReader<TradeDTO> calcTradeTotalAmtReader() {
        JdbcCursorItemReader<TradeDTO> ir  = new JdbcCursorItemReaderBuilder<TradeDTO>()
                .fetchSize(CHUNKSIZE)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(TradeDTO.class))
                .sql("select trade_user_no AS user_no, sum(trade_amt) AS trade_amt, DATE_FORMAT(trade_dt, '%Y-%m-%d') as trade_dt\n" +
                        "from trade_list tl\n" +
                        "where reflect_yn = 'N'\n" +
                        "group by trade_user_no, DATE_format(trade_dt, '%Y-%m-%d')\n" +
                        "order by trade_dt")
                .name("calcTradeTotalAmtWriter")
                .build();
        return ir;
    }

    @Bean
    public JdbcBatchItemWriter<TradeDTO> calcTradeTotalAmtWriter() {
        JdbcBatchItemWriter<TradeDTO> iw = new JdbcBatchItemWriter<>();
        iw.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        iw.setAssertUpdates(false);            // 업데이트할 열이 없을 때 발생하는 Exception 닫기
        iw.setSql("insert into trade_total (user_no, trade_dt, total_amt, reg_dt) values (:user_no, :trade_dt, :trade_amt, now())");
        iw.setDataSource(dataSource);
        iw.afterPropertiesSet();
        return iw;
    }
//////////////////////////////////////////////////////////////////////////////////
    @Bean
    public Step updateReflectChkStep() {
        log.info("[ TradeJob - updateReflectChkStep ] START");

        Step step = stepBuilderFactory.get("updateReflectChkStep")
                .allowStartIfComplete(true)
                .<TradeDTO, TradeDTO>chunk(CHUNKSIZE)
                .reader(updateReflectChkReader())
                .writer(updateReflectChkWriter())
                .build();
        return step;
    }

    @Bean
    public JdbcCursorItemReader<TradeDTO> updateReflectChkReader() {
        JdbcCursorItemReader<TradeDTO> ir  = new JdbcCursorItemReaderBuilder<TradeDTO>()
                .fetchSize(CHUNKSIZE)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(TradeDTO.class))
                .sql("select trade_seq, trade_user_no, trade_type, trade_amt, trade_dt, reflect_yn\n" +
                        "from trade_list tl\n" +
                        "where reflect_yn = 'N'")
                .name("updateReflectChkWriter")
                .build();
        return ir;
    }

    @Bean
    public JdbcBatchItemWriter<TradeDTO> updateReflectChkWriter() {
        JdbcBatchItemWriter<TradeDTO> iw = new JdbcBatchItemWriter<TradeDTO>();
        iw.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        iw.setAssertUpdates(false);            // 업데이트할 열이 없을 때 발생하는 Exception 닫기
        iw.setSql("update trade_list set reflect_yn = 'N' where trade_seq = :trade_seq");
        iw.setDataSource(dataSource);
        iw.afterPropertiesSet();
        return iw;
    }

    @Bean
    public ItemProcessor<TradeDTO, TradeDTO> processor() {
        return tradeDto -> {
            return tradeDto;
        };
    }
}

/**
 * .isRestartable()
 *    -> return 타입 boolean
 *    -> 실패했을 경우, 재실행 여부
 *    -> default : false / 실행이 가능하도록 할 경우 : true
 */