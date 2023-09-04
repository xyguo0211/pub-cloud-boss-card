package com.cn.school.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "trip")
public class TripConfig {

	/**
	 * 出发前多少个小时不允许退票
	 */
	private int noRefundTime;
	/**
	 * 超过一次，扣费10元
	 */
	private int refundOneFee;
	/**
	 * 发车前2小时，扣除20元
	 */
	private int refundCarFee;
	/**
	 * 出发前多少个小时扣除费用
	 */
	private int noRefundCarTime;
	/**
	 * 该天超过3次不付款,已被禁止购票
	 */
	private int blackCount;
	/**
	 * 该天超过1次一小時后
	 */
	private int waitCount;
	/**
	 * 多少次等待支付1小时
	 */
	private int waitTimeCount;



}
