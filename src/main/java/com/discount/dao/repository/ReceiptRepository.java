package com.discount.dao.repository;

import com.discount.dao.model.Receipt;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    @EntityGraph(type = EntityGraphType.FETCH, value = "receipt_entity_graph")
    Set<Receipt> findReceiptsByClientId(Long clientId);
}
