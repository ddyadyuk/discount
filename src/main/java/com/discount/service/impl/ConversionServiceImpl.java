package com.discount.service.impl;

import com.discount.configuration.ConversionRatiosConfiguration;
import com.discount.configuration.LimitsConfiguration;
import com.discount.service.ConversionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
@Service
public class ConversionServiceImpl implements ConversionService {

    private final LimitsConfiguration limitsConfiguration;
    private final ConversionRatiosConfiguration conversionRatiosConfiguration;

    @Override
    public BigDecimal convertToBonusPoints(BigDecimal receiptsGrandTotal, BigDecimal unprocessedAmount) {
        //TODO: refactor if possible

        // receiptsGrandTotal <= 50000
        if (receiptsGrandTotal.compareTo(limitsConfiguration.getLower()) <= 0) {
            return unprocessedAmount.divide(conversionRatiosConfiguration.getLowerLimitRatio(), RoundingMode.HALF_UP);
        }
        // 50000 < receiptsGrandTotal <= 100000
        if (receiptsGrandTotal.compareTo(limitsConfiguration.getLower()) > 0
            && receiptsGrandTotal.compareTo(limitsConfiguration.getHigher()) <= 0) {
            return unprocessedAmount.divide(conversionRatiosConfiguration.getBetweenLimitRatio(), RoundingMode.HALF_UP);
        }
        // receiptsGrandTotal > 100000
        return unprocessedAmount.divide(conversionRatiosConfiguration.getHigherLimitRatio(), RoundingMode.HALF_UP);

    }

    @Override
    public BigDecimal convertToMoney(BigDecimal points) {
        return points.multiply(conversionRatiosConfiguration.getPointsToMoneyRatio());
    }
}
