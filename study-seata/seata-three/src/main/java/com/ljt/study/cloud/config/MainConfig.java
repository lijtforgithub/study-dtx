package com.ljt.study.cloud.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author LiJingTang
 * @date 2021-06-11 15:15
 */
@Configuration
@MapperScan("com.ljt.study.cloud.mapper")
public class MainConfig {
}
