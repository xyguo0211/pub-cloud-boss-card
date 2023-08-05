package com.cn.offline.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "offlinefilepath")
public class OfflineFilePathOnlineConfig {

    private    String root ;

    private  String  image;

    private  String  baseUrl;


}
