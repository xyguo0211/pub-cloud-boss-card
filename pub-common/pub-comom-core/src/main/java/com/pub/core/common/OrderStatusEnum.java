package com.pub.core.common;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;

/**
 * SaaS业务单状态枚举
 */
@Slf4j
public enum OrderStatusEnum {
    TRACKING_STATUS_WAITING(0, "初始化", "tracking_status"),
    TRACKING_STATUS_TRACKED(-1, "异常", "tracking_status"),
    TRACKING_STATUS_EXCEPTION(1, "已完成", "tracking_status");


    private final Integer code;
    private final String name;
    private final String columnName;

    OrderStatusEnum(Integer code, String name, String columnName) {
        this.code = code;
        this.name = name;
        this.columnName = columnName;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getColumnName() {
        return columnName;
    }


    /** 获取展示文案 - 跟踪状态 */
    public static String getOrderStatusStr(Integer trackingStatus) {
        String targetColumnName = "tracking_status";
        return getStr(targetColumnName, trackingStatus);
    }

    public static String getStr(String columnName, Integer code) {
        return Arrays.stream(OrderStatusEnum.values())
                     .filter(item ->
                             item.getColumnName().equals(columnName) && Objects.equals(item.getCode(), code))
                     .findFirst()
                     .map(OrderStatusEnum::getName).orElse("");
    }





}
