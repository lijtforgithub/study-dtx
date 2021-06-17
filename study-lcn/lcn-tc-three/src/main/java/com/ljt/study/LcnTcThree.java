package com.ljt.study;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author LiJingTang
 * @date 2021-06-11 14:59
 */
@EnableDistributedTransaction
@SpringBootApplication
public class LcnTcThree {

    public static void main(String[] args) {
        SpringApplication.run(LcnTcThree.class, args);
    }

}