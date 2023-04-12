package com.discount.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReceiptRequestDto extends ReceiptDto {
    @NotNull
    private Long clientId;
}
