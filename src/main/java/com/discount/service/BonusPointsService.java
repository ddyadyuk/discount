package com.discount.service;

import com.discount.dao.model.Client;
import com.discount.dto.BonusPointsResponse;
import com.discount.dto.WithdrawPointsDto;

import java.math.BigDecimal;

public interface BonusPointsService {
    BonusPointsResponse withdrawPoints(WithdrawPointsDto withdrawPointsDto);

    BonusPointsResponse getAvailablePoints(Long clientId);

    void recalculateBonusPoints(Client client, BigDecimal receiptTotal);
}
