package com.example.moneyTransfer.model;

import lombok.Data;

@Data
public class Transaction {
    private String destination;
    private Long amount;
    private String recipeId;
    private String otpCode;
}
