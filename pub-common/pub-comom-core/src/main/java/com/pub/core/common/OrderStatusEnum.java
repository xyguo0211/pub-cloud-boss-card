package com.pub.core.common;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;

/**
 * SaaS业务单状态枚举
 */
public enum OrderStatusEnum {
    TRACKING_STATUS_WAITING(0, "初始化", "tracking_status"),
    TRACKING_STATUS_TRACKED(1, "失败", "tracking_status"),
    TRACKING_STATUS_EXCEPTION(2, "成功", "tracking_status"),

    TR_TYPE_ORDER(1, "订单交易", "TR_status"),
    TR_TYPE_PERSON(2, "邀请人返现", "TR_status"),
    TR_TYPE_RECOD(3, "提现", "TR_status"),

    /**
     * 管理员审核状态
     */
    IS_INSPECT_NO(-1, "不审核", "INSPECT_status"),
    IS_INSPECT_WAITING(0, "审核中", "INSPECT_status"),
    IS_INSPECT_SUCESS(9, "审核完成", "INSPECT_status"),


    /**
     * 状态  9成功  -1失败  0 取消  1 初始化
     */
    DrawalFee_STATUS_INIT(1, "审核中", "DrawalFee_status"),
    DrawalFee_STATUS_FAIL(-1, "失败", "DrawalFee_status"),
    DrawalFee_STATUS_SUCESS(9, "已完成", "DrawalFee_status");


    private final Integer code;
    private final String name;
    private final String columnName;

    OrderStatusEnum(Integer code, String name, String columnName) {
        this.code = code;
        this.name = name;
        this.columnName = columnName;
    }

    public static String getTransactionTypeStr(Integer type) {
        String targetColumnName = "TR_status";
        return getStr(targetColumnName, type);
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


    public static String getOrderStatusStr(Integer trackingStatus) {
        String targetColumnName = "tracking_status";
        return getStr(targetColumnName, trackingStatus);
    }
    public static String getIsInspectStr(Integer trackingStatus) {
        String targetColumnName = "INSPECT_status";
        return getStr(targetColumnName, trackingStatus);
    }

    /**
     * 状态  9成功  -1失败  0 取消  1 初始化
     */
    public static String getDrawalFeeStatusStr(Integer trackingStatus) {
        String targetColumnName = "DrawalFee_status";
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
