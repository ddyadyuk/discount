package com.discount.controller;

import com.discount.dto.ClientRequestDto;

public interface ClientController {
    //TODO: add swagger api docs

    void addClient(ClientRequestDto client);

    void removeClient(Long clientId);
}
