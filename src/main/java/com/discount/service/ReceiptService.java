package com.discount.service;

import com.discount.dto.ReceiptDto;
import com.discount.dto.ReceiptRequestDto;

import java.util.Set;

public interface ReceiptService {

    void save(ReceiptRequestDto receiptDto);

    Set<ReceiptDto> getReceiptsByClientId(Long clientId);
}
