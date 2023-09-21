package com.cn.school.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "schoolfilepath")
public class FilePathOnlineConfig {

    private    String root ;

    private  String  baseUrl;



}
