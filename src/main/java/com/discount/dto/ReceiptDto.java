package com.discount.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ReceiptDto {
    private Long clientId;
    private List<ReceiptPositionDto> receiptPositions;
}
