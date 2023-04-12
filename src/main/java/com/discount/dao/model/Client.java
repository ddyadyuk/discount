package com.discount.dao.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
@Getter
@Setter
public class Client {

    @Id
    @Column(name = "client_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number", length = 20)
    private String cardNumber;

    @Column(name = "discount_points")
    private BigDecimal discountPoints;

    @Column(name = "grand_total")
    private BigDecimal grandTotal;

    @OneToMany(mappedBy = "client", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Receipt> receipts = new ArrayList<>();

    public void addReceipt(Receipt receipt) {
        receipt.setClient(this);
        receipts.add(receipt);
    }
}
