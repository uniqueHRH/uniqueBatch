package com.unique.uniquebatch.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@ToString
public class TradeDTO {
    private BigInteger  trade_seq;
    private int         user_no;
    private int         trade_type;
    private int         trade_amt;
    private Date        trade_dt;
}
