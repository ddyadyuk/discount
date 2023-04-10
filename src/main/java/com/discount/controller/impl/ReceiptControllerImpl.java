package com.discount.controller.impl;

import com.discount.controller.ReceiptController;
import com.discount.dto.ReceiptDto;
import com.discount.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ReceiptControllerImpl implements ReceiptController {

    private final ReceiptService receiptService;

    @Override
    @PostMapping
    public void addNewReceipt(@RequestBody @Validated ReceiptDto receiptDto) {
        receiptService.save(receiptDto);
    }

    @Override
    @GetMapping
    public Set<ReceiptDto> getReceiptsByClientId(@RequestParam(name = "clientId") Long clientId) {
        return receiptService.getReceiptsByClientId(clientId);
    }
}
