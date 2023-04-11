package com.discount.service.impl;

import com.discount.properties.ConversionRatiosProperties;
import com.discount.properties.LimitsProperties;
import com.discount.service.ConversionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
@Service
public class ConversionServiceImpl implements ConversionService {

    private final LimitsProperties limitsProperties;
    private final ConversionRatiosProperties conversionRatiosProperties;

    @Override
    public BigDecimal convertToBonusPoints(BigDecimal receiptsGrandTotal, BigDecimal unprocessedAmount) {
        if (receiptsGrandTotal.compareTo(limitsProperties.getLower()) <= 0) {
            return unprocessedAmount.divide(conversionRatiosProperties.getLowerLimitRatio(), RoundingMode.HALF_UP);
        }
        if (receiptsGrandTotal.compareTo(limitsProperties.getLower()) > 0
            && receiptsGrandTotal.compareTo(limitsProperties.getHigher()) <= 0) {
            return unprocessedAmount.divide(conversionRatiosProperties.getBetweenLimitRatio(), RoundingMode.HALF_UP);
        }
        return unprocessedAmount.divide(conversionRatiosProperties.getHigherLimitRatio(), RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal convertToMoney(BigDecimal points) {
        return points.multiply(conversionRatiosProperties.getPointsToMoneyRatio());
    }
}
