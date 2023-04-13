package com.example.limechaintask.models;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Entity
public class Wallet {
    public static final BigDecimal STARTING_BALANCE = BigDecimal.valueOf(100);
    public static final String START_CHARS = "0x";
    @Id
    private final String address;
    @Column(name = "balance")
    private BigDecimal balance = STARTING_BALANCE;
    @OneToMany(mappedBy = "id", fetch = FetchType.EAGER)
    private List<Transaction> transactions = new ArrayList<>();

    public Wallet() {
        this.address = generateWalletAddress();
    }

    private String generateWalletAddress() {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append(START_CHARS);
        for (int i = 0; i < 40; i++) {
            int randomInt = rnd.nextInt(2);
            if (randomInt == 0) {
                sb.append(rnd.nextInt(10));
            } else {
                Character currChar = (char) ('a' + rnd.nextInt(6));
                sb.append(i % 2 == 0 ? currChar : currChar.toString().toUpperCase());
            }
        }
        return sb.toString();
    }

    public Transaction makeTransaction(Wallet receiver, BigDecimal value) {
        return new Transaction(this, receiver, value);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public String getAddress() {
        return this.address;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wallet wallet)) return false;
        return Objects.equals(address, wallet.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
