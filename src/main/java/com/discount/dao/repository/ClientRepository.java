package com.discount.dao.repository;

import com.discount.dao.model.Client;
import com.discount.exception.UserIsMissingException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    default Client findClientById(Long clientId) {
        return findById(clientId)
                .orElseThrow(() -> new UserIsMissingException(
                        String.format("There is no booking record with id: %S", clientId)));
    }
}
