package com.discount.dao.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "client")
@Getter
@Setter
public class Client {

    @Id
    @Column(name = "client_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //todo: add validation
    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "discount_points")
    private BigDecimal discountPoints;

    @OneToMany(mappedBy = "client")
    private List<Receipt> receipts = new ArrayList<>();

    public void addReceipt(Receipt receipt) {
        receipt.setClient(this);
        receipts.add(receipt);
    }
}
