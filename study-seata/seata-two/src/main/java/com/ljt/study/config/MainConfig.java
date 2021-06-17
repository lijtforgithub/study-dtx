package com.ljt.study.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author LiJingTang
 * @date 2021-06-11 15:15
 */
@Configuration
@MapperScan("com.ljt.study.mapper")
public class MainConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
