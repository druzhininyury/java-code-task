package com.github.druzhininyury.testtask.repository;

import com.github.druzhininyury.testtask.model.WalletBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletBalance, Long> {

}
