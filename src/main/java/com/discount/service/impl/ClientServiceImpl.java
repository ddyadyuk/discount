package com.discount.service.impl;

import com.discount.dao.model.Client;
import com.discount.dao.repository.ClientRepository;
import com.discount.dto.ClientRequestDto;
import com.discount.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Long save(ClientRequestDto client) {
        log.info("Saving new client");

        Client clientModel = new Client();
        clientModel.setCardNumber(client.getCardNumber());
        clientModel.setDiscountPoints(BigDecimal.ZERO);
        clientModel.setGrandTotal(BigDecimal.ZERO);

        Client savedClient = clientRepository.save(clientModel);
        log.info("Client with id: {} was saved", savedClient.getId());

        return savedClient.getId();
    }

    @Override
    public void removeClient(Long clientId) {
        log.info("Removing client by id = [{}]", clientId);
        clientRepository.deleteById(clientId);
    }
}
