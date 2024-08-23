package com.github.druzhininyury.testtask;

import com.github.druzhininyury.testtask.dto.OperationDto;
import com.github.druzhininyury.testtask.dto.WalletBalanceDto;
import com.github.druzhininyury.testtask.mapping.OperationMapper;
import com.github.druzhininyury.testtask.mapping.WalletMapper;
import com.github.druzhininyury.testtask.model.WalletBalance;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
public class WalletController {

    private final WalletService walletService;

    private final OperationMapper operationMapper;
    private final WalletMapper walletMapper;

    @PostMapping("/api/v1/wallet")
    @ResponseStatus(HttpStatus.OK)
    public WalletBalanceDto processOperation(@RequestBody @Valid OperationDto operationDto) {
        log.info("Got POST request: " + operationDto);
        WalletBalance walletBalance = walletService.processOperation(operationMapper.toOperation(operationDto),
                                                                     operationDto.getWalletId());
        return walletMapper.toWalletBalanceDto(walletBalance);
    }

    @GetMapping("/api/v1/wallets/{walletId}")
    @ResponseStatus(HttpStatus.OK)
    public WalletBalanceDto getBalance(@PathVariable long walletId) {
        log.info("Got GET request for walletId = " + walletId);
        WalletBalance walletBalance = walletService.getBalance(walletId);
        return walletMapper.toWalletBalanceDto(walletBalance);
    }

    // endpoint for external testing
    @PostMapping("/api/v1/wallet/add")
    @ResponseStatus(HttpStatus.CREATED)
    public WalletBalanceDto addWallet() {
        log.info("Got POST request for adding wallet.");
        WalletBalance walletBalance = walletService.addWallet();
        return walletMapper.toWalletBalanceDto(walletBalance);
    }

    // endpoint for external testing
    @GetMapping("/api/v1/wallets")
    @ResponseStatus(HttpStatus.OK)
    public List<WalletBalanceDto> getAllWallets() {
        log.info("Got GET request for getting all wallets.");
        List<WalletBalance> walletsBalances = walletService.getAllWallets();
        return walletMapper.toWalletBalanceDto(walletsBalances);
    }

}
