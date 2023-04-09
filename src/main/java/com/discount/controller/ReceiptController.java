package com.discount.controller;

import com.discount.dto.ReceiptDto;

import java.util.Set;

public interface ReceiptController {
    void addNewReceipt(ReceiptDto receiptDto);

    Set<ReceiptDto> getReceiptsByClientId(Long clientId);

}
