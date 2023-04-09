package com.discount.dao.repository;

import com.discount.dao.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    default Client findClientById(Long clientId) {
        return findById(clientId)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("There is no booking record with id: %S", clientId)));
    }
}
