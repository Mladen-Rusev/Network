package com.example.limechaintask.controllers;

import com.example.limechaintask.models.Transaction;
import com.example.limechaintask.models.Wallet;
import com.example.limechaintask.pojos.IncomingTransactionParams;
import com.example.limechaintask.services.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransactionControllerTests {

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    private Wallet testWalletSender;
    private Wallet testWalletReceiver;

    private List<Wallet> testWalletList = new ArrayList<>();

    private IncomingTransactionParams testIncomingTransactionParams;

    private Transaction testTransaction;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        testWalletSender = new Wallet();
        testWalletReceiver = new Wallet();
        testWalletList.add(testWalletSender);
        testWalletList.add(testWalletReceiver);

        testIncomingTransactionParams = new IncomingTransactionParams("test_address", "test_recipient", BigDecimal.TEN);

        testTransaction = new Transaction();
        testTransaction.setAmount(BigDecimal.TEN);
        testTransaction.setReceiver(testWalletReceiver);
        testTransaction.setSender(testWalletSender);
    }

    @Test
    public void testCreateWallet() {
        when(walletService.createWallet()).thenReturn(testWalletSender);
        ResponseEntity<Wallet> responseEntity = walletController.getNewWallet();
        assert (responseEntity.getStatusCode().equals(HttpStatus.CREATED));
        assert (responseEntity.getBody().getAddress().equals(testWalletSender.getAddress()));
        assert (responseEntity.getBody().getBalance().equals(BigDecimal.valueOf(100)));
    }

    @Test
    public void testGetWalletByAddress() {
        when(walletService.getByAddress(any(String.class))).thenReturn(testWalletSender);
        ResponseEntity<Wallet> responseEntity = walletController.getWalletByAddress("test_address");
        assert (responseEntity.getStatusCode().equals(HttpStatus.FOUND));
        assert (responseEntity.getBody().getAddress().equals(testWalletSender.getAddress()));
        assert (responseEntity.getBody().getBalance().equals(BigDecimal.valueOf(100)));
    }

    @Test
    void testInitTransaction() throws InsufficientResourcesException {
        // Arrange
        String senderAddress = testWalletSender.getAddress();
        String recipientAddress = testWalletReceiver.getAddress();
        BigDecimal amount = BigDecimal.TEN;

        Transaction expectedTransaction = testWalletSender.makeTransaction(testWalletReceiver, BigDecimal.TEN);
        when(walletService.initTransaction(senderAddress, recipientAddress, amount)).thenReturn(expectedTransaction);

        // Act
        ResponseEntity<String> response = walletController.initTransaction(testIncomingTransactionParams);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Transfer succeeded!", response.getBody());
    }

    @Test
    public void testGetAllWallets() {
        when(walletService.getAll()).thenReturn(testWalletList);
        ResponseEntity<List<Wallet>> responseEntity = walletController.getAllWallets();
        assert (responseEntity.getStatusCode().equals(HttpStatus.OK));
        assert (responseEntity.getBody().size() == 2);
        assert (responseEntity.getBody().get(0).getAddress().equals(testWalletSender.getAddress()));
        assert (responseEntity.getBody().get(0).getBalance().equals(BigDecimal.valueOf(100)));
    }
}

