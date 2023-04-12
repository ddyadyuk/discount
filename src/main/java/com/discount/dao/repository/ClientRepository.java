package com.discount.dao.repository;

import com.discount.dao.model.Client;
import com.discount.exception.ClientNotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Client> findById(Long clientId);

    default Client findClientById(Long clientId) {
        return findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(
                        String.format("There is no client record with id: %S", clientId)));
    }
}
