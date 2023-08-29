package com.sn.online.config.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 注册接收实体类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-07-31
 */
@Data
public class OnlineOrderSubmitDto {


    private Integer totalAmonunt;

    private String userRemarks;

    private Integer thirdId;

    private Integer secondId;

    private Integer firstId;

    private String totalAmonuntFee;

    private String rate;

    private List<String> images;

}
