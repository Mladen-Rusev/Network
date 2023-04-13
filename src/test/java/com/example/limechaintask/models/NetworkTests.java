package com.example.limechaintask.models;

import com.example.limechaintask.services.WalletService;
import com.example.limechaintask.services.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class NetworkTests {

    private Network network;
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        network = Network.getInstance();
        walletService = new WalletServiceImpl();
    }

    @Test
    void testTransferFunds() throws InsufficientResourcesException {
        // Arrange
        Wallet senderWallet = new Wallet();
        Wallet recipientWallet = new Wallet();
        ;

        BigDecimal transferAmount = BigDecimal.valueOf(65);
        Transaction transaction = senderWallet.makeTransaction(recipientWallet, transferAmount);

        // Act
        network.processTransaction(transaction);

        // Assert
        assertEquals(BigDecimal.valueOf(34.35), senderWallet.getBalance());
        assertEquals(BigDecimal.valueOf(165), recipientWallet.getBalance());
    }

    @Test
    void testInsuffBalance() {
        // Arrange
        Wallet senderWallet = new Wallet();
        Wallet recipientWallet = new Wallet();
        ;
        BigDecimal amount = BigDecimal.valueOf(155);
        Transaction transaction = senderWallet.makeTransaction(recipientWallet, amount);

        // Act and Assert
        InsufficientResourcesException exception = org.junit.jupiter.api.Assertions.assertThrows(
                InsufficientResourcesException.class,
                () -> network.processTransaction(transaction)
        );
        assertEquals(network.insufficientBalanceErrMsg(senderWallet, transaction.getAmount(), amount.multiply(Network.FEE)), exception.getMessage());
    }
}