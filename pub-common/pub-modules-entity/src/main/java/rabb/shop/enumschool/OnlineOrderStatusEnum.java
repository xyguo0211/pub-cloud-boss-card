package rabb.shop.enumschool;

import java.util.Arrays;
import java.util.Objects;

/**
 * SaaS业务单状态枚举
 */
public enum OnlineOrderStatusEnum {
    TRACKING_STATUS_WAITING(0, "in review", "tracking_status"),
    TRACKING_STATUS_TRACKED(1, "fail", "tracking_status"),
    TRACKING_STATUS_EXCEPTION(2, "success", "tracking_status"),

    TR_TYPE_ORDER(1, "Order transaction", "TR_status"),
    TR_TYPE_PERSON(2, "Invitation code cashback", "TR_status"),
    TR_TYPE_RECOD(3, "Withdrawal", "TR_status"),

    /**
     * 状态  9成功  -1失败  0 取消  1 初始化
     */
    DrawalFee_STATUS_INIT(1, "in review", "DrawalFee_status"),
    DrawalFee_STATUS_FAIL(-1, "fail", "DrawalFee_status"),
    DrawalFee_STATUS_SUCESS(9, "success", "DrawalFee_status");


    private final Integer code;
    private final String name;
    private final String columnName;

    OnlineOrderStatusEnum(Integer code, String name, String columnName) {
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

    /**
     * 状态  9成功  -1失败  0 取消  1 初始化
     */
    public static String getDrawalFeeStatusStr(Integer trackingStatus) {
        String targetColumnName = "DrawalFee_status";
        return getStr(targetColumnName, trackingStatus);
    }

    public static String getStr(String columnName, Integer code) {
        return Arrays.stream(OnlineOrderStatusEnum.values())
                     .filter(item ->
                             item.getColumnName().equals(columnName) && Objects.equals(item.getCode(), code))
                     .findFirst()
                     .map(OnlineOrderStatusEnum::getName).orElse("");
    }





}
