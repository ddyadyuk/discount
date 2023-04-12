package com.discount.service.impl;

import com.discount.properties.ConversionRatiosProperties;
import com.discount.properties.LimitsProperties;
import com.discount.service.ConversionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
@Service
@Slf4j
public class ConversionServiceImpl implements ConversionService {

    private final LimitsProperties limitsProperties;
    private final ConversionRatiosProperties conversionRatiosProperties;

    @Override
    public BigDecimal convertToBonusPoints(BigDecimal receiptsGrandTotal, BigDecimal unprocessedAmount) {
        log.info("Converting to bonus points by total = [{}], and unprocessed = [{}]", receiptsGrandTotal,
                 unprocessedAmount);
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
        log.info("Converting points = [{}] to money", points);
        return points.multiply(conversionRatiosProperties.getPointsToMoneyRatio());
    }
}
