package com.discount.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class IncorrectWithdrawalAmountException extends RuntimeException {
    public IncorrectWithdrawalAmountException(String message) {
        super(message);
    }
}
