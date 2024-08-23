package com.github.druzhininyury.testtask;

import com.github.druzhininyury.testtask.exception.EntityNotFoundException;
import com.github.druzhininyury.testtask.exception.IllegalOperationException;
import com.github.druzhininyury.testtask.model.Operation;
import com.github.druzhininyury.testtask.model.WalletBalance;
import com.github.druzhininyury.testtask.repository.OperationRepository;
import com.github.druzhininyury.testtask.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final OperationRepository operationRepository;

    @Override
    @Transactional
    public WalletBalance processOperation(Operation operation, long walletId) {
        Optional<WalletBalance> walletBalanceContainer = walletRepository.findById(walletId);
        if (walletBalanceContainer.isEmpty()) {
            throw new EntityNotFoundException("Wallet with id=" + walletId + " not found.");
        }
        operation.setWalletBalance(walletBalanceContainer.get());

        return switch (operation.getType()) {
            case DEPOSIT -> {
                if (Long.MAX_VALUE - operation.getWalletBalance().getBalance() < operation.getAmount()) {
                    throw new IllegalOperationException("Can't deposit because reached max value for balance.");
                }

                operation.getWalletBalance().setBalance(operation.getWalletBalance().getBalance() + operation.getAmount());

                WalletBalance walletBalance = walletRepository.save(operation.getWalletBalance());
                operation = operationRepository.save(operation);

                log.info("Processed operation: " + operation);

                yield walletBalance;
            }
            case WITHDRAW -> {
                if (operation.getWalletBalance().getBalance() < operation.getAmount()) {
                    throw new IllegalOperationException("Can't withdraw more than exists on balance.");
                }

                operation.getWalletBalance().setBalance(operation.getWalletBalance().getBalance() - operation.getAmount());

                WalletBalance walletBalance = walletRepository.save(operation.getWalletBalance());
                operation = operationRepository.save(operation);

                log.info("Processed operation: " + operation);

                yield walletBalance;
            }
        };
    }

    @Override
    public WalletBalance getBalance(long walletId) {
        Optional<WalletBalance> walletBalanceContainer = walletRepository.findById(walletId);
        if (walletBalanceContainer.isEmpty()) {
            throw new EntityNotFoundException("Wallet with id=" + walletId + " not found.");
        }

        log.info("Got balance for wallet with id=" + walletId);

        return walletBalanceContainer.get();
    }

    @Override
    @Transactional
    public WalletBalance addWallet() {
        WalletBalance walletBalance = walletRepository.save(new WalletBalance());

        log.info("Added new wallet with id=" + walletBalance.getWalletId());

        return walletBalance;
    }

    @Override
    public List<WalletBalance> getAllWallets() {
        List<WalletBalance> walletsBalances = walletRepository.findAll();

        log.info("Got balanced for all wallets.");

        return walletsBalances;
    }
}
