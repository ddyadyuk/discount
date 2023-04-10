package com.discount.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WithdrawPointsDto {
    @NotNull
    private Long clientId;
    @NotNull
    private BigDecimal amount;
}
