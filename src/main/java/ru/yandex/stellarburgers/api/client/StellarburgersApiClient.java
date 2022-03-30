package ru.yandex.stellarburgers.api.client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.yandex.stellarburgers.api.constants.Endpoints;

import static io.restassured.RestAssured.given;

public class StellarburgersApiClient {

    protected RequestSpecification getBaseReqSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(Endpoints.BASE_URL)
                .build();
    }
}
