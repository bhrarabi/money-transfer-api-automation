package com.example.moneyTransfer.service;

import com.example.moneyTransfer.config.Properties;
import com.example.moneyTransfer.model.Transaction;
import com.example.moneyTransfer.model.User;
import com.example.moneyTransfer.utils.common.RestUtil;
import com.example.moneyTransfer.utils.constants.HttpHeader;
import io.restassured.response.Response;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Data
@Service
public class MoneyTransferService {
    private RestUtil request;
    private Map<String, String> headers = new HashMap<>();
    private  Properties properties;

    public MoneyTransferService(Properties properties){
        this.properties = properties;
        headers.put("Content-Type", HttpHeader.FORM_DATA);
    }

    public Response login(User user) {
        request = new RestUtil(properties.getBaseUrl());
        return request.path("/login/")
                .multiPart("username", user.getUsername())
                .multiPart("password", user.getPassword())
                .headers(headers)
                .post().getResponse();
    }
    public Response initTransfer(String token, Transaction transaction) {
        request = new RestUtil(properties.getBaseUrl());
        headers.put("Authorization",token);
        return request.path("/init-transaction/")
                .multiPart("destination", transaction.getDestination())
                .multiPart("amount", transaction.getAmount().toString())
                .headers(headers)
                .post().getResponse();
    }

    public Response getOtp(String token, Transaction transaction) {
        request = new RestUtil(properties.getBaseUrl());
        headers.put("Authorization",token);
        return request.path("/get-otp/")
                .multiPart("recipe_id", transaction.getRecipeId())
                .headers(headers)
                .post().getResponse();
    }

    public Response commitTransaction(String token, Transaction transaction) {
        request = new RestUtil(properties.getBaseUrl());
        headers.put("Authorization",token);
        return request.path("/commit-transaction/")
                .multiPart("recipe_id", transaction.getRecipeId())
                .multiPart("code", transaction.getOtpCode())
                .headers(headers)
                .post().getResponse();
    }
}
