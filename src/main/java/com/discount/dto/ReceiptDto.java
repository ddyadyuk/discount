package com.discount.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReceiptDto {
    @NotNull
    private Long clientId;
    @NotNull
    private List<ReceiptPositionDto> receiptPositions;
}
