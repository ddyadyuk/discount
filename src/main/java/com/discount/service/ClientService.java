package com.discount.service;

import com.discount.dto.ClientRequestDto;

public interface ClientService {
    Long save(ClientRequestDto client);

    void removeClient(Long clientId);
}
