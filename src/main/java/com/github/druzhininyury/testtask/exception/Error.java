package com.github.druzhininyury.testtask.exception;

import lombok.Builder;

@Builder
public class Error {

    public final String type;
    public final String message;

}
