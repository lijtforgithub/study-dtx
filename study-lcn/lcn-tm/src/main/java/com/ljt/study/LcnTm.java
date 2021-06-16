package com.ljt.study;

import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableTransactionManagerServer
public class LcnTm {

    public static void main(String[] args) {
        SpringApplication.run(LcnTm.class, args);
    }

}
