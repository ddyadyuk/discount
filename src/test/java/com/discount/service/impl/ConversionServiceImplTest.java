package com.discount.service.impl;

import com.discount.properties.ConversionRatiosProperties;
import com.discount.properties.LimitsProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConversionServiceImplTest {

    @Mock
    private LimitsProperties limitsProperties;
    @Mock
    private ConversionRatiosProperties conversionRatiosProperties;

    private ConversionServiceImpl conversionService;

    @BeforeEach
    public void setup() {
        conversionService = new ConversionServiceImpl(limitsProperties, conversionRatiosProperties);
    }

    @Test
    void convertToBonusPoints_whenGrandTotalIsLessOrEqualToLowerLimit_thenLowerLimitRatioIsApplied() {
        BigDecimal unprocessedAmount = BigDecimal.valueOf(20_000);
        BigDecimal lowerLimitRatio = BigDecimal.valueOf(50);

        when(limitsProperties.getLower()).thenReturn(BigDecimal.valueOf(50_000));
        when(conversionRatiosProperties.getLowerLimitRatio()).thenReturn(lowerLimitRatio);

        BigDecimal bonusPoints = conversionService.convertToBonusPoints(BigDecimal.valueOf(40_000),
                                                                        unprocessedAmount);

        assertNotNull(bonusPoints);
        assertEquals(unprocessedAmount.divide(lowerLimitRatio, RoundingMode.HALF_UP), bonusPoints);
    }

    @Test
    void convertToBonusPoints_whenGrandTotalIsBetweenLimits_thenBetweenLimitRatioIsApplied() {
        BigDecimal unprocessedAmount = BigDecimal.valueOf(20_000);
        BigDecimal betweenLimitRatio = BigDecimal.valueOf(40);

        when(limitsProperties.getLower()).thenReturn(BigDecimal.valueOf(50_000));
        when(limitsProperties.getHigher()).thenReturn(BigDecimal.valueOf(100_000));
        when(conversionRatiosProperties.getBetweenLimitRatio()).thenReturn(betweenLimitRatio);

        BigDecimal bonusPoints = conversionService.convertToBonusPoints(BigDecimal.valueOf(60_000),
                                                                        unprocessedAmount);

        assertNotNull(bonusPoints);
        assertEquals(unprocessedAmount.divide(betweenLimitRatio, RoundingMode.HALF_UP), bonusPoints);
    }

    @Test
    void convertToBonusPoints_whenGrandTotalIsHigherThanHigherLimit_thenHigherLimitRatioIsApplied() {
        BigDecimal unprocessedAmount = BigDecimal.valueOf(20_000);
        BigDecimal higherLimitRatio = BigDecimal.valueOf(30);

        when(limitsProperties.getLower()).thenReturn(BigDecimal.valueOf(50_000));
        when(limitsProperties.getHigher()).thenReturn(BigDecimal.valueOf(100_000));
        when(conversionRatiosProperties.getHigherLimitRatio()).thenReturn(higherLimitRatio);

        BigDecimal bonusPoints = conversionService.convertToBonusPoints(BigDecimal.valueOf(100_001),
                                                                        unprocessedAmount);

        assertNotNull(bonusPoints);
        assertEquals(unprocessedAmount.divide(higherLimitRatio, RoundingMode.HALF_UP), bonusPoints);
    }

    @Test
    void convertToMoney_whenPointsNotNull_thenReturnConvertedResult() {
        BigDecimal pointsToMoneyRatio = BigDecimal.valueOf(10);
        BigDecimal pointsToConvert = BigDecimal.valueOf(111);
        when(conversionRatiosProperties.getPointsToMoneyRatio()).thenReturn(pointsToMoneyRatio);

        BigDecimal convertedMoney = conversionService.convertToMoney(pointsToConvert);

        assertNotNull(convertedMoney);
        assertEquals(pointsToConvert.multiply(pointsToMoneyRatio), convertedMoney);
    }
}