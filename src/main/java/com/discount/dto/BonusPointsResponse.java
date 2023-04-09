package com.discount.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BonusPointsResponse {

    private BigDecimal withdrawnMoney;
    private BigDecimal withdrawnPoints;
    private BigDecimal availablePoints;

}
