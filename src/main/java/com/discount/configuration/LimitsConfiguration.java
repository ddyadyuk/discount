package com.discount.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Getter
@Setter
@Configuration
@ConfigurationProperties("limits")
public class LimitsConfiguration {

    private BigDecimal lower;
    private BigDecimal higher;
}
