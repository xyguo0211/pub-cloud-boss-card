package com.sn.online.entity.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;
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
