package com.example.limechaintask.pojos;

import java.io.Serializable;
import java.math.BigDecimal;

public class IncomingTransactionParams implements Serializable {
    private String address;
    private String recipient;
    private BigDecimal amount;

    public IncomingTransactionParams() {
    }

    public IncomingTransactionParams(String address, String recipient, BigDecimal amount) {
        this.address = address;
        this.recipient = recipient;
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public String getRecipient() {
        return recipient;
    }

    public BigDecimal getAmount() {
        return amount;
    }

}
