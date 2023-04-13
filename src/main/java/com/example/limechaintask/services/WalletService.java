package com.example.limechaintask.services;

import com.example.limechaintask.models.Transaction;
import com.example.limechaintask.models.Wallet;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.util.List;

public interface WalletService {
    Wallet getByAddress(String address);

    List<Wallet> getAll();

    Wallet createWallet();

    Transaction initTransaction(String sender, String recipient, BigDecimal value) throws InsufficientResourcesException;

}
