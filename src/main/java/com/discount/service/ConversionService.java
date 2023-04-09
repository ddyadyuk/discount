package com.discount.service;

import java.math.BigDecimal;

public interface ConversionService {
    BigDecimal convertToBonusPoints(BigDecimal receiptsGrandTotal, BigDecimal unprocessedAmount);
    BigDecimal convertToMoney(BigDecimal points);
}
