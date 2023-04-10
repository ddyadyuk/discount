package com.discount.service.impl;

import com.discount.dao.model.Client;
import com.discount.dao.repository.ClientRepository;
import com.discount.dto.ClientRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    private ClientServiceImpl clientService;

    @BeforeEach
    public void setup() {
        clientService = new ClientServiceImpl(clientRepository);
    }

    @Test
    void addClient_whenClientRequestDtoIsCorrect_thenReturnClientId() {
        Client client = new Client();
        client.setId(1L);

        when(clientRepository.save(any())).thenReturn(client);

        ClientRequestDto clientRequestDto = new ClientRequestDto();
        clientRequestDto.setCardNumber("1234567");

        Long clientId = clientService.save(clientRequestDto);

        assertNotNull(clientId);
    }

    @Test
    void removeClient_whenClientIdProvided_thenDeleteByIdIsTriggered() {
        clientService.removeClient(1L);

        verify(clientRepository, times(1)).deleteById(any());
    }
}