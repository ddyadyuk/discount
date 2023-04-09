package com.discount.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "convert")
public class ConversionRatiosConfiguration {

    private BigDecimal lowerLimitRatio;
    private BigDecimal betweenLimitRatio;
    private BigDecimal higherLimitRatio;
    private BigDecimal pointsToMoneyRatio;
}
