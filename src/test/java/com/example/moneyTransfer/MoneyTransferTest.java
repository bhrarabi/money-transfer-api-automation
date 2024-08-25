package com.example.moneyTransfer;

import com.example.moneyTransfer.config.Properties;
import com.example.moneyTransfer.model.Transaction;
import com.example.moneyTransfer.model.User;
import com.example.moneyTransfer.service.MoneyTransferService;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@SpringBootTest
@Feature("Money Transfer Test")
public class MoneyTransferTest extends AbstractTestNGSpringContextTests {

    @Autowired
    MoneyTransferService moneyTransferService;

    @Autowired
    private Properties properties;

    private Response response;
    private String token;
    private String code;
    private String otp;
    private User source;
    private User destination;
    private Transaction transaction;

    @BeforeClass
    public void setup(){
        source = new User();
        source.setUsername(properties.getFirstUsername());
        source.setPassword(properties.getFirstPassword());

        destination = new User();
        destination.setUsername(properties.getSecondUsername());
        destination.setPassword(properties.getSecondPassword());

        transaction = new Transaction();
        transaction.setDestination(destination.getUsername());
        transaction.setAmount(0L);

    }

    @Test(description = "To test the money transfer is successful", priority = 0)
    @Story("Get list of branch")
    @Severity(SeverityLevel.CRITICAL)
    public void successfulMoneyTransfer(){
        // User authentication
        response = moneyTransferService.login(source);
        token = response.getBody().jsonPath().getString("token");
        Assert.assertEquals(response.getStatusCode(),200);
        Assert.assertEquals(response.getBody().jsonPath().getString("message"),"توکن احراز هویت در فرایند پرداخت");

        // Init transaction
        response = moneyTransferService.initTransfer(token, transaction);
        Assert.assertEquals(response.getStatusCode(),200);
        Assert.assertEquals(response.getBody().jsonPath().getString("message"),"شناسه ی پرداخت");
        code = response.getBody().jsonPath().getString("code");
        transaction.setRecipeId(code);

        // Get otp
        response = moneyTransferService.getOtp(token, transaction);
        Assert.assertEquals(response.getStatusCode(),200);
        Assert.assertEquals(response.getBody().jsonPath().getString("message"),"کد OTP جهت تکمیل پرداخت");
        otp = response.getBody().jsonPath().getString("code");
        transaction.setOtpCode(otp);

        // Commit transaction
        response = moneyTransferService.commitTransaction(token, transaction);
        Assert.assertEquals(response.getStatusCode(),200);
        Assert.assertEquals(response.getBody().jsonPath().getString("message"),"تراکنش انجام شد");

    }

}
