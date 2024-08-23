package com.github.druzhininyury.testtask;

import com.github.druzhininyury.testtask.exception.EntityNotFoundException;
import com.github.druzhininyury.testtask.exception.IllegalOperationException;
import com.github.druzhininyury.testtask.model.Operation;
import com.github.druzhininyury.testtask.model.Wallet;
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
    public Wallet processOperation(Operation operation, long walletId) {
        Optional<Wallet> walletBalanceContainer = walletRepository.findById(walletId);
        if (walletBalanceContainer.isEmpty()) {
            throw new EntityNotFoundException("Wallet with id=" + walletId + " not found.");
        }
        operation.setWallet(walletBalanceContainer.get());

        return switch (operation.getType()) {
            case DEPOSIT -> {
                if (Long.MAX_VALUE - operation.getWallet().getBalance() < operation.getAmount()) {
                    throw new IllegalOperationException("Can't deposit because reached max value for balance.");
                }

                operation.getWallet().setBalance(operation.getWallet().getBalance() + operation.getAmount());

                Wallet wallet = walletRepository.save(operation.getWallet());
                operation = operationRepository.save(operation);

                log.info("Processed operation: " + operation);

                yield wallet;
            }
            case WITHDRAW -> {
                if (operation.getWallet().getBalance() < operation.getAmount()) {
                    throw new IllegalOperationException("Can't withdraw more than exists on balance.");
                }

                operation.getWallet().setBalance(operation.getWallet().getBalance() - operation.getAmount());

                Wallet wallet = walletRepository.save(operation.getWallet());
                operation = operationRepository.save(operation);

                log.info("Processed operation: " + operation);

                yield wallet;
            }
        };
    }

    @Override
    public Wallet getBalance(long walletId) {
        Optional<Wallet> walletBalanceContainer = walletRepository.findById(walletId);
        if (walletBalanceContainer.isEmpty()) {
            throw new EntityNotFoundException("Wallet with id=" + walletId + " not found.");
        }

        log.info("Got balance for wallet with id=" + walletId);

        return walletBalanceContainer.get();
    }

    @Override
    @Transactional
    public Wallet addWallet() {
        Wallet wallet = walletRepository.save(new Wallet());

        log.info("Added new wallet with id=" + wallet.getId());

        return wallet;
    }

    @Override
    public List<Wallet> getAllWallets() {
        List<Wallet> wallets = walletRepository.findAll();

        log.info("Got balanced for all wallets.");

        return wallets;
    }
}
