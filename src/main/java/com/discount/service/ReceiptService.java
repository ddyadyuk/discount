package com.discount.service;

import com.discount.dto.ReceiptDto;

import java.util.Set;

public interface ReceiptService {

    void save(ReceiptDto receiptDto);

    Set<ReceiptDto> getReceiptsByClientId(Long clientId);
}
