package com.discount.controller.impl;

import com.discount.controller.ClientController;
import com.discount.dto.ClientRequestDto;
import com.discount.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientControllerImpl implements ClientController {

    private final ClientService clientService;

    //TODO: return Id?
    @Override
    @PostMapping
    public void addClient(@RequestBody @Valid ClientRequestDto client) {
        clientService.addClient(client);
    }

    @Override
    @DeleteMapping("/{clientId}")
    public void removeClient(@PathVariable(name = "clientId") Long clientId) {
        clientService.removeClient(clientId);
    }
}
