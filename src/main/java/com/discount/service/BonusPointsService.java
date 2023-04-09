package com.discount.service;

import com.discount.dto.BonusPointsResponse;
import com.discount.dto.WithdrawPointsDto;

public interface BonusPointsService {
    BonusPointsResponse withdrawPoints(WithdrawPointsDto withdrawPointsDto);

    BonusPointsResponse getAvailablePoints(Long clientId);

    void recalculateBonusPoints(Long id);
}
