package com.discount.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WithdrawPointsDto {
    private Long clientId;
    private BigDecimal amount;
}
