package com.cn.school.entity.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LineColor {

    @JsonProperty("r")
    private Integer red;

    @JsonProperty("g")
    private Integer green;

    @JsonProperty("b")
    private Integer blue;
}