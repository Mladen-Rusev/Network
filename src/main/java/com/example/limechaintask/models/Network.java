package com.example.limechaintask.models;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.util.InputMismatchException;

public class Network {
    public static final BigDecimal FEE = BigDecimal.valueOf(0.01);
    public static final String GENESIS = System.lineSeparator() + "--------------The Times 03/Jan/2009 Chancellor on brink of second bailout for banks--------------" + System.lineSeparator();
    public static final String RECEIVER_CANT_BE_SENDER = "Receiver can't be sender";
    public static final String VALUE_CANT_BE_NULL = "Value can't be null";
    public static final String VALUE_CANT_BE_0 = "Value can't be < 0";
    private static Network network;
    private BigDecimal accumulatedFee = BigDecimal.ZERO;

    public static Network getInstance() {
        if (network == null) {
            network = new Network();
        }
        return network;
    }

    private Network() {
        System.err.println(GENESIS);
    }

    public Wallet createWallet() {
        return new Wallet();
    }

    public void processTransaction(Transaction transaction) throws InsufficientResourcesException {
        validateTransactionParams(transaction);
        Wallet senderWallet = transaction.getSender();
        Wallet receiverWallet = transaction.getReceiver();
        BigDecimal currentFeeAmount = transaction.getAmount().multiply(FEE);
        BigDecimal totalAmount = currentFeeAmount.add(transaction.getAmount());
        if (senderWallet.getBalance().compareTo(totalAmount) < 0) {
            throw new InsufficientResourcesException(insufficientBalanceErrMsg(senderWallet, transaction.getAmount(), currentFeeAmount));
        }
        senderWallet.setBalance(senderWallet.getBalance().subtract(totalAmount));
        accumulatedFee = accumulatedFee.add(currentFeeAmount);
        receiverWallet.setBalance(receiverWallet.getBalance().add(transaction.getAmount()));
        senderWallet.addTransaction(transaction);
        receiverWallet.addTransaction(transaction);
    }

    private void validateTransactionParams(Transaction transaction) {
        if (transaction.getSender().equals(transaction.getReceiver())) {
            throw new InputMismatchException(RECEIVER_CANT_BE_SENDER);
        }
        if (transaction.getAmount() == null) {
            throw new InputMismatchException(VALUE_CANT_BE_NULL);
        }
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InputMismatchException(VALUE_CANT_BE_0);
        }
    }

    public String insufficientBalanceErrMsg(Wallet senderWallet, BigDecimal amount, BigDecimal currentFeeAmount) {
        return "Wallet " + senderWallet.getAddress()
                + " has insufficient balance: " + senderWallet.getBalance()
                + " to send: " + amount
                + " and fee: " + currentFeeAmount;
    }
}
