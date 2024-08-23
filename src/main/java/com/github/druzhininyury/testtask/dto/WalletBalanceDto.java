package com.github.druzhininyury.testtask.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class WalletBalanceDto {

    private final long walletId;
    private final long balance;

}
