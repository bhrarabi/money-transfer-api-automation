package com.example.moneyTransfer.utils.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Data;

import java.io.File;
import java.util.Map;

import static org.apache.commons.io.FileUtils.getFile;

@Data
public class RestUtil {
    private RequestSpecBuilder requestSpecBuilder;
    private RequestSpecification requestSpecification;
    private Response response;

    public RestUtil(String url) {
        requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(url);
    }

    public RestUtil path(String path) {
        requestSpecBuilder.setBasePath(path);
        return this;
    }

    public RestUtil multiPart(String controlNale, String filePath, String mimeType) {
        requestSpecBuilder.addMultiPart(controlNale, new File(filePath), mimeType);
        return this;
    }

    public RestUtil pathParam(String key, String value) {
        requestSpecBuilder.addPathParam(key, value);
        return this;
    }

    public RestUtil queryParam(String key, String value) {
        requestSpecBuilder.addQueryParam(key, value);
        return this;
    }

    public RestUtil queryParams(Map<String,String> queryParams) {
        requestSpecBuilder.addQueryParams(queryParams);
        return this;
    }

    public RestUtil contentType(ContentType contentType) {
        requestSpecBuilder.setContentType(contentType);
        return this;
    }

    public RestUtil headers(Map<String, String> headers) {
        requestSpecBuilder.addHeaders(headers);
        return this;
    }

    public RestUtil cookies(Map<String, String> cookies) {
        requestSpecBuilder.addCookies(cookies);
        return this;
    }

    public RestUtil cookies(Cookies cookies) {
        requestSpecBuilder.addCookies(cookies);
        return this;
    }

    public RestUtil cookie(Cookie cookie) {
        requestSpecBuilder.addCookie(cookie);
        return this;
    }

    public RestUtil body(Object body) {
        requestSpecBuilder.setBody(body);
        return this;
    }

    public RestUtil multiPart(String key, String value) {
        requestSpecBuilder.addMultiPart(key, value);
        return this;
    }
    public RestUtil formParam(String key, String value) {
        requestSpecBuilder.addFormParam(key,value);
        return this;
    }

    public RestUtil post() {
        requestSpecification = requestSpecBuilder.build();
        response = RestAssured.given()
                .filter(new AllureRestAssured())
                .log().all()
                .spec(requestSpecification)
                .when()
                .post()
                .then()
                .log().all()
                .extract()
                .response();

        return this;
    }

    public RestUtil get() {
        requestSpecification = requestSpecBuilder.build();
        response = RestAssured.given()
                .filter(new AllureRestAssured())
                .log().all()
                .spec(requestSpecification)
                .when()
                .get()
                .then()
                .log().all()
                .extract()
                .response();

        return this;
    }

    public RestUtil put() {
        requestSpecification = requestSpecBuilder.build();
        response = RestAssured.given()
                .filter(new AllureRestAssured())
                .log().all()
                .spec(requestSpecification)
                .when()
                .put()
                .then()
                .log().all()
                .extract()
                .response();

        return this;
    }

    public RestUtil delete() {
        requestSpecification = requestSpecBuilder.build();
        response = RestAssured.given()
                .filter(new AllureRestAssured())
                .log().all()
                .spec(requestSpecification)
                .when()
                .delete()
                .then()
                .log().all()
                .extract()
                .response();

        return this;
    }

    public String getApiResponseAsString() {
        return response.asString();
    }

    public JsonPath getApiResponseAsJson() {
        return response.jsonPath();
    }

    public Integer getStatusCode() {
        return response.getStatusCode();
    }


    public <T> T responseToPojo(Class<T> type) {
        try {
            return new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                    .readValue(getApiResponseAsString(), type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
