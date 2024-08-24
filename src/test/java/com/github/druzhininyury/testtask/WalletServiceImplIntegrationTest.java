package com.github.druzhininyury.testtask;

import com.github.druzhininyury.testtask.exception.EntityNotFoundException;
import com.github.druzhininyury.testtask.exception.IllegalOperationException;
import com.github.druzhininyury.testtask.model.Operation;
import com.github.druzhininyury.testtask.model.Wallet;
import com.github.druzhininyury.testtask.repository.OperationRepository;
import com.github.druzhininyury.testtask.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WalletServiceImplIntegrationTest {

    @Autowired
    private WalletServiceImpl walletService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private OperationRepository operationRepository;

    private List<Long> walletsIds;

    @BeforeEach
    void fillDataBase() {
        walletsIds = new ArrayList<>();

        walletsIds.add(walletRepository.save(Wallet.builder().balance(1000L).build()).getId());
        walletsIds.add(walletRepository.save(Wallet.builder().balance(2000L).build()).getId());
        walletsIds.add(walletRepository.save(Wallet.builder().balance(3000L).build()).getId());
    }

    @AfterEach
    void clearDataBase() {
        operationRepository.deleteAll();
        walletRepository.deleteAll();
    }

    @Test
    public void getBalance_whenInputValid_thenWalletReturned() {
        Wallet result = walletService.getBalance(walletsIds.get(1));

        assertThat(result.getBalance(), equalTo(2000L));
    }

    @Test
    public void getBalance_whenWalletIdInvalid_thenExceptionThrown() {
        assertThrows(EntityNotFoundException.class, () -> walletService.getBalance(walletsIds.get(2) + 1L));
    }

    @Test
    public void processOperation_whenDeposit_thenWalletReturned() {
        Operation operation = Operation.builder().type(Operation.OperationType.DEPOSIT).amount(2000L).build();

        Wallet result = walletService.processOperation(operation, walletsIds.get(1));

        assertThat(result.getBalance(), equalTo(4000L));
    }

    @Test
    public void processOperation_whenWithdraw_thenWalletReturned() {
        Operation operation = Operation.builder().type(Operation.OperationType.WITHDRAW).amount(1500L).build();

        Wallet result = walletService.processOperation(operation, walletsIds.get(1));

        assertThat(result.getBalance(), equalTo(500L));
    }

    @Test
    public void processOperation_whenWalletIdInvalid_thenExceptionThrown() {
        Operation operation = Operation.builder().type(Operation.OperationType.DEPOSIT).amount(2000L).build();

        assertThrows(EntityNotFoundException.class, () -> walletService.processOperation(operation, walletsIds.get(2) + 1L));
    }

    @Test
    public void processOperation_whenWithdrawMoreThanBalance_thenExceptionThrown() {
        Operation operation = Operation.builder().type(Operation.OperationType.WITHDRAW).amount(4000L).build();

        assertThrows(IllegalOperationException.class, () -> walletService.processOperation(operation, walletsIds.get(1)));
    }

    @Test
    public void processOperation_whenBalanceMaxValueExceed_thenExceptionThrown() {
        Operation operation = Operation.builder().type(Operation.OperationType.DEPOSIT).amount(Long.MAX_VALUE - 1000L).build();

        assertThrows(IllegalOperationException.class, () -> walletService.processOperation(operation, walletsIds.get(1)));
    }

}
