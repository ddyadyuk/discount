package com.discount.dao.repository;

import com.discount.dao.model.Client;
import com.discount.exception.ClientNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    default Client findClientById(Long clientId) {
        return findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(
                        String.format("There is no client record with id: %S", clientId)));
    }
}
