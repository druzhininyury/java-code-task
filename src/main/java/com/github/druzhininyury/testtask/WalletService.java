package com.github.druzhininyury.testtask;

import com.github.druzhininyury.testtask.model.Operation;
import com.github.druzhininyury.testtask.model.Wallet;

import java.util.List;

public interface WalletService {

    Wallet processOperation(Operation operation, long walletId);

    Wallet getBalance(long walletId);

    Wallet addWallet();

    List<Wallet> getAllWallets();

}
