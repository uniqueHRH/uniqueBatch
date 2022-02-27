package com.unique.uniquebatch.domain.support;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class TradeDaoImpl extends JdbcTemplate implements com.unique.uniquebatch.domain.TradeDaoImpl {

    public TradeDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @SuppressWarnings("unchecked")  // 검증되지 않은 연산자 관련 경고 억제
    @Override
    public int insertTrade(int user_no, int trade_type, int trade_amt) {
        return 0;
//        return query(
//                "insert into trade_list (trade_user_no, trade_type, trade_amt, trade_dt) values (2,2, 1500, now()",
//                new Object[] {user_no, trade_type, trade_amt},
//                (rs, rowNum) -> {
//
//                }
//        );
    }

}
