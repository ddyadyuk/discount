package com.discount.controller;

import com.discount.dto.BonusPointsResponse;
import com.discount.dto.WithdrawPointsDto;
import org.springframework.web.bind.annotation.PostMapping;

public interface BonusPointsController {

    BonusPointsResponse withdrawPoints(WithdrawPointsDto withdrawPointsDto);

    BonusPointsResponse getAvailablePoints(Long userId);

}
