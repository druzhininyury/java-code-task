package com.github.druzhininyury.testtask;

import com.github.druzhininyury.testtask.model.Operation;
import com.github.druzhininyury.testtask.model.WalletBalance;

import java.util.List;

public interface WalletService {

    WalletBalance processOperation(Operation operation, long walletId);

    WalletBalance getBalance(long walletId);

    WalletBalance addWallet();

    List<WalletBalance> getAllWallets();

}
