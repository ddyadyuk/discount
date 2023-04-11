package com.discount.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
@Component
@ConfigurationProperties("limits")
public class LimitsProperties {

    private BigDecimal lower;
    private BigDecimal higher;
}
