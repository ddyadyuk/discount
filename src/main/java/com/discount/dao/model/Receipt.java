package com.discount.dao.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "receipt")
@Getter
@Setter
public class Receipt {

    @Id
    @Column(name = "receipt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//
//    @Column(name = "grand_total")
//    private BigDecimal grandTotal;

    //TODO: verify that receiptPositions are set
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "receipt_id")
    private List<ReceiptPosition> receiptPositions;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    private Client client;
}
