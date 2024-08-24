package com.github.druzhininyury.testtask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.druzhininyury.testtask.dto.OperationDto;
import com.github.druzhininyury.testtask.exception.EntityNotFoundException;
import com.github.druzhininyury.testtask.mapping.OperationMapper;
import com.github.druzhininyury.testtask.mapping.OperationMapperImpl;
import com.github.druzhininyury.testtask.mapping.WalletMapper;
import com.github.druzhininyury.testtask.mapping.WalletMapperImpl;
import com.github.druzhininyury.testtask.model.Operation;
import com.github.druzhininyury.testtask.model.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WalletController.class)
@Import(value = {OperationMapperImpl.class, WalletMapperImpl.class})
public class WalletControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private WalletMapper walletMapper;

    @MockBean
    private WalletService walletService;

    @Test
    public void getWalletBalance_whenWalletIdValid() throws Exception {
        Long walletId = 1L;
        Long balance = 1000L;
        Wallet wallet = Wallet.builder().id(walletId).balance(balance).build();

        when(walletService.getBalance(walletId)).thenReturn(wallet);

        mvc.perform(get("/api/v1/wallets/{walletId}", walletId)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId", is(walletId), Long.class))
                .andExpect(jsonPath("$.balance", is(balance), Long.class));
    }

    @Test
    public void getWalletBalance_whenWalletIdInvalid_thenExceptionThrown() throws Exception {
        Long walletId = 1L;

        when(walletService.getBalance(walletId)).thenThrow(new EntityNotFoundException("Error"));

        mvc.perform(get("/api/v1/wallets/{walletId}", walletId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void processOperation_whenInputValid() throws Exception {
        Long walletId = 1L;

        OperationDto operationDto = OperationDto.builder()
                .walletId(walletId)
                .type(Operation.OperationType.DEPOSIT)
                .amount(1000L)
                .build();

        Wallet updatedWallet = Wallet.builder().id(walletId).balance(2000L).build();

        when(walletService.processOperation(any(Operation.class), any(Long.class))).thenReturn(updatedWallet);

        mvc.perform(post("/api/v1/wallet")
                        .content(mapper.writeValueAsString(operationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId", is(walletId), Long.class))
                .andExpect(jsonPath("$.balance", is(updatedWallet.getBalance()), Long.class));
    }

    @Test
    public void processOperation_whenWalletIdInvalid_thenExceptionThrown() throws Exception {
        Long walletId = 1L;

        OperationDto operationDto = OperationDto.builder()
                .walletId(walletId)
                .type(Operation.OperationType.DEPOSIT)
                .amount(1000L)
                .build();

        when(walletService.processOperation(any(Operation.class), any(Long.class)))
                .thenThrow(new EntityNotFoundException("Error."));

        mvc.perform(post("/api/v1/wallet")
                        .content(mapper.writeValueAsString(operationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void processOperation_whenAmountIsNegative_thenExceptionThrown() throws Exception {
        Long walletId = 1L;

        OperationDto operationDto = OperationDto.builder()
                .walletId(walletId)
                .type(Operation.OperationType.DEPOSIT)
                .amount(-1000L)
                .build();

        mvc.perform(post("/api/v1/wallet")
                        .content(mapper.writeValueAsString(operationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
