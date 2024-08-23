package com.github.druzhininyury.testtask.mapping;

import com.github.druzhininyury.testtask.dto.WalletBalanceDto;
import com.github.druzhininyury.testtask.model.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    @Mapping(target = "walletId", source = "id")
    WalletBalanceDto toWalletBalanceDto(Wallet wallet);

    List<WalletBalanceDto> toWalletBalanceDto(List<Wallet> wallets);

}
