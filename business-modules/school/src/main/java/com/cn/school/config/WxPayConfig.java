package com.cn.school.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.Serializable;



@Data
@Component
@ConfigurationProperties(prefix = "wx")
public class WxPayConfig {

	private String url;

	private String appid;

	private String mchid;

	private String notifyUrl;



	private String wxAppSecret;

}
