package com.heaven.online;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableScheduling  //开启定时任务
@EnableAsync // 启动异步调用
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class },scanBasePackages = {"com.heaven.online"})
@EnableSwagger2
/**
 * 一定加上mapper扫描。因为默认扫描application下
 */
@MapperScan("rabb.heaven.mapper")
public class HeavenOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeavenOnlineApplication.class, args);
    }

}
