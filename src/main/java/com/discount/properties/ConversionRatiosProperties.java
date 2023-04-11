package com.discount.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "convert")
public class ConversionRatiosProperties {

    private BigDecimal lowerLimitRatio;
    private BigDecimal betweenLimitRatio;
    private BigDecimal higherLimitRatio;
    private BigDecimal pointsToMoneyRatio;
}
