package com.discount.dao.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "receipt")
@Getter
@Setter
@NamedEntityGraph(name = "receipt_entity_graph", attributeNodes = @NamedAttributeNode("receiptPositions"))
public class Receipt {

    @Id
    @Column(name = "receipt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "receipt", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<ReceiptPosition> receiptPositions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    private Client client;

    public void addPosition(ReceiptPosition receiptPosition) {
        this.receiptPositions.add(receiptPosition);
        receiptPosition.setReceipt(this);
    }
}
