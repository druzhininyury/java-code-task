package com.github.druzhininyury.testtask.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.druzhininyury.testtask.model.Operation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class OperationDto {


    @NotNull(message = "Wallet uuid must be provided.")
    private final Long walletId;

    @JsonProperty("operationType")
    @NotNull(message = "Operation type must be provided.")
    private final Operation.OperationType type;

    @NotNull(message = "Amount must be provided.")
    @Positive(message = "Amount must be positive.")
    private final Long amount;

}
