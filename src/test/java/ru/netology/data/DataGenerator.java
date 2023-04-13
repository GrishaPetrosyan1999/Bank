package ru.netology.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("en"));

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private DataGenerator() {
    }

    private static void makeRequest(RegistrationUsers registrationUsers) {
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(registrationUsers) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    public static String getRandomLogin() {
        return faker.name().username();
    }

    public static String getRandomPassword() {
        return faker.internet().password();
    }

    public static class Registration {
        private Registration() {
        }
    }

    public static RegistrationUsers getActiveUser() {
        RegistrationUsers registrationUsers = new RegistrationUsers(getRandomLogin(), getRandomPassword(), "active");
        makeRequest(registrationUsers);
        return registrationUsers;
    }

    public static RegistrationUsers getBlockedUser() {
        RegistrationUsers registrationUsers = new RegistrationUsers(getRandomLogin(), getRandomPassword(), "blocked");
        makeRequest(registrationUsers);
        return registrationUsers;
    }

    public static RegistrationUsers getInvalidPasswordUser(String status) {
        String login = getRandomLogin();
        makeRequest(new RegistrationUsers(login, getRandomPassword(), status));
        return new RegistrationUsers(login, getRandomPassword(), status);
    }

    public static RegistrationUsers getInvalidLoginUser(String status) {
        String password = getRandomPassword();
        makeRequest(new RegistrationUsers(getRandomLogin(), password, status));
        return new RegistrationUsers(getRandomLogin(), password, status);
    }
}

