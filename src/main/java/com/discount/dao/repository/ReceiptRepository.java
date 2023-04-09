package com.discount.dao.repository;

import com.discount.dao.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    Set<Receipt> findReceiptsByClientId(Long clientId);
}
