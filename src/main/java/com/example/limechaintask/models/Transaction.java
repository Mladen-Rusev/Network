package com.example.limechaintask.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Transaction implements Serializable {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "sender_address", referencedColumnName = "address")
    @JsonBackReference
    private Wallet sender;

    @ManyToOne
    @JoinColumn(name = "receiver_address", referencedColumnName = "address")
    @JsonBackReference
    private Wallet receiver;

    @Column(name = "amount")
    private BigDecimal amount;

    public Transaction() {
    }

    public Transaction(Wallet sender, Wallet receiver, BigDecimal amount) {
        this.id = UUID.randomUUID().toString();
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public Wallet getSender() {
        return this.sender;
    }

    public Wallet getReceiver() {
        return this.receiver;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setSender(Wallet sender) {
        this.sender = sender;
    }

    public void setReceiver(Wallet receiver) {
        this.receiver = receiver;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
