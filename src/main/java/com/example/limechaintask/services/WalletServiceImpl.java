package com.example.limechaintask.services;

import com.example.limechaintask.models.Network;
import com.example.limechaintask.models.Wallet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.hibernate.FetchNotFoundException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Acts as service and simple fetch repo.
 */
@Component
public class WalletServiceImpl implements WalletService {

    @PersistenceContext
    EntityManager entityManager;

    public WalletServiceImpl() {
    }

    @Transactional
    @Override
    public Wallet createWallet() {
        Wallet wallet = Network.getInstance().createWallet();
        entityManager.persist(wallet);
        System.out.println("New wallet created with address:" + wallet.getAddress());
        return wallet;
    }

    @Transactional
    @Override
    public com.example.limechaintask.models.Transaction initTransaction(String sender, String receiver, BigDecimal value) throws InsufficientResourcesException {
        Wallet senderWallet = getByAddress(sender);
        Wallet receiverWallet = getByAddress(receiver);
        com.example.limechaintask.models.Transaction transaction = senderWallet.makeTransaction(receiverWallet, value);
        Network.getInstance().processTransaction(transaction);
        entityManager.persist(transaction);
        System.out.println("Transaction " + transaction.getId() + " processed successfully!");
        return transaction;
    }

    @Override
    public Wallet getByAddress(String address) {
        Query q = entityManager.createQuery("from Wallet where address = :address");
        q.setParameter("address", address);
        if (q.getResultList().isEmpty()) {
            throw new FetchNotFoundException("Wallet", "address");
        }
        return (Wallet) q.getSingleResult();
    }

    @Override
    public List<Wallet> getAll() {
        Query q = entityManager.createQuery("from Wallet");
        return q.getResultList();
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void generateNetworkActivity() {
        Random rnd = new Random();
        List<Wallet> walletsList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            walletsList.add(createWallet());
        }
        for (int i = 0; i < 10; i++) {
            Wallet senderWallet = walletsList.get(i);
            Wallet receiverWallet = walletsList.get(rnd.nextInt(10));
            if (receiverWallet == senderWallet) continue;
            try {
                initTransaction(senderWallet.getAddress(), receiverWallet.getAddress(), BigDecimal.valueOf(rnd.nextInt(25)).add(BigDecimal.ONE));
            } catch (InsufficientResourcesException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
