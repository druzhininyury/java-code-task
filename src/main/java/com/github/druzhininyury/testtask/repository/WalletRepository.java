package com.github.druzhininyury.testtask.repository;

import com.github.druzhininyury.testtask.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

}
