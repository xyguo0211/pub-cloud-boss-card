package com.pub.core.common;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;

/**
 * SaaS业务单状态枚举
 */
@Slf4j
public enum OfflineStatusEnum {
    BLACK_STATUS(-1, "黑名单", "black_status"),
    BLACK_STATUS_NORMAL(9, "正常", "black_status");


    private final Integer code;
    private final String name;
    private final String columnName;

    OfflineStatusEnum(Integer code, String name, String columnName) {
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
    public static String getBlackStr(Integer trackingStatus) {
        String targetColumnName = "black_status";
        return getStr(targetColumnName, trackingStatus);
    }

    public static String getStr(String columnName, Integer code) {
        return Arrays.stream(OfflineStatusEnum.values())
                     .filter(item ->
                             item.getColumnName().equals(columnName) && Objects.equals(item.getCode(), code))
                     .findFirst()
                     .map(OfflineStatusEnum::getName).orElse("");
    }





}
