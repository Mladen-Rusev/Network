package com.example.limechaintask.controllers;

import com.example.limechaintask.models.Wallet;
import com.example.limechaintask.pojos.IncomingTransactionParams;
import com.example.limechaintask.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.InsufficientResourcesException;
import java.util.List;

@RestController
public class WalletController {
    @Autowired
    private WalletService walletService;

    @GetMapping("/wallets/all")
    public ResponseEntity<List<Wallet>> getAllWallets() {
        List<Wallet> wallets = walletService.getAll();
        return new ResponseEntity<>(wallets, HttpStatus.OK);
    }

    @GetMapping("/wallet/{address}")
    public ResponseEntity<Wallet> getWalletByAddress(@PathVariable String address) {
        Wallet wallet = walletService.getByAddress(address);
        if (wallet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(wallet, HttpStatus.FOUND);
    }

    @GetMapping("/wallet/new")
    public ResponseEntity<Wallet> getNewWallet() {
        Wallet wallet = walletService.createWallet();
        if (wallet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(wallet, HttpStatus.CREATED);
    }

    @PostMapping("/")
    public ResponseEntity<String> initTransaction(@RequestBody IncomingTransactionParams params) {
        try {
            walletService.initTransaction(params.getAddress(), params.getRecipient(), params.getAmount());
        } catch (InsufficientResourcesException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>("Transfer succeeded!", HttpStatus.CREATED);
    }

}
