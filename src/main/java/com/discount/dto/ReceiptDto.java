package com.discount.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ReceiptDto {

    @NotNull
    private List<ReceiptPositionDto> receiptPositions;
}
