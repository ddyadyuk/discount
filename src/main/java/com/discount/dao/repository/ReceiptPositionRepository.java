package com.discount.dao.repository;

import com.discount.dao.model.ReceiptPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptPositionRepository extends JpaRepository<ReceiptPosition, Long> {
}
