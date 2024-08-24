package com.github.druzhininyury.testtask;

import com.github.druzhininyury.testtask.exception.EntityNotFoundException;
import com.github.druzhininyury.testtask.exception.IllegalOperationException;
import com.github.druzhininyury.testtask.model.Operation;
import com.github.druzhininyury.testtask.model.Wallet;
import com.github.druzhininyury.testtask.repository.OperationRepository;
import com.github.druzhininyury.testtask.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private OperationRepository operationRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    @Test
    public void getWalletBalance_whenInputValid_thenReturnWallet() {
        Long walletId = 1L;
        Wallet wallet = Wallet.builder().id(walletId).balance(1000L).build();

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        Wallet result = walletService.getBalance(walletId);

        assertThat(result, equalTo(wallet));
        verify(walletRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void getWalletBalance_whenWalletIdInvalid_thenExceptionThrown() {
        Long walletId = 1L;

        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> walletService.getBalance(walletId));
    }

    @Test
    public void processOperation_whenInputValid_thenReturnWallet() {
        Long walletId = 1L;
        Wallet wallet = Wallet.builder().id(walletId).balance(1000L).build();
        Wallet walletUpdated = Wallet.builder().id(walletId).balance(1000L + 2000L).build();
        Operation operation = Operation.builder().type(Operation.OperationType.DEPOSIT).amount(2000L).build();
        Operation operationSaved = Operation.builder()
                .id(1L)
                .wallet(walletUpdated)
                .type(Operation.OperationType.DEPOSIT)
                .amount(2000L)
                .createdOn(LocalDateTime.now())
                .build();

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(wallet)).thenReturn(walletUpdated);
        when(operationRepository.save(any(Operation.class))).thenReturn(operationSaved);

        Wallet result = walletService.processOperation(operation, walletId);

        assertThat(result, equalTo(walletUpdated));
        verify(walletRepository, times(1)).save(any(Wallet.class));
        verify(operationRepository, times(1)).save(any(Operation.class));
    }

    @Test
    public void processOperation_whenWalletIdInvalid_thenExceptionThrown() {
        Long walletId = 1L;
        Wallet wallet = Wallet.builder().id(walletId).balance(1000L).build();
        Operation operation = Operation.builder().type(Operation.OperationType.WITHDRAW).amount(2000L).build();

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(IllegalOperationException.class, () -> walletService.processOperation(operation, walletId));
    }

    @Test
    public void processOperation_whenWithdrawMoreThanBalance_thenExceptionThrown() {
        Long walletId = 1L;
        Wallet wallet = Wallet.builder().id(walletId).balance(Long.MAX_VALUE - 1000L).build();
        Operation operation = Operation.builder().type(Operation.OperationType.DEPOSIT).amount(2000L).build();

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(IllegalOperationException.class, () -> walletService.processOperation(operation, walletId));
    }

    @Test
    public void processOperation_whenBalanceMaxValueExceed_thenExceptionThrown() {
        Long walletId = 1L;
        Operation operation = Operation.builder().type(Operation.OperationType.DEPOSIT).amount(2000L).build();

        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> walletService.processOperation(operation, walletId));
    }

}
