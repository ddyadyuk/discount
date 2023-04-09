package com.discount.service;

import com.discount.dto.ClientRequestDto;
import com.discount.dto.BonusPointsResponse;
import com.discount.dto.WithdrawPointsDto;

import java.math.BigDecimal;

public interface ClientService {
    void addClient(ClientRequestDto client);

    void removeClient(Long clientId);
}
