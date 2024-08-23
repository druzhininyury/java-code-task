package com.github.druzhininyury.testtask.mapping;

import com.github.druzhininyury.testtask.dto.WalletBalanceDto;
import com.github.druzhininyury.testtask.model.WalletBalance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletBalanceDto toWalletBalanceDto(WalletBalance walletBalance);

    List<WalletBalanceDto> toWalletBalanceDto(List<WalletBalance> walletsBalances);

}
