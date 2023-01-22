import model.CourierGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Login;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import model.Login;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import model.Courier;
import model.CourierClient;

public class LoggedCourierTest {
    private  Courier courier;
    private Login login;
    private CourierClient courierClient;
    private String id;

    @Before
    public void setUp() {
        courier = CourierGenerator.random();
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Авторизации курьера, корректные данные")
    public void LoginCourierTest() {
        ValidatableResponse response = courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(Login.from(courier));
        id = loginResponse.extract().path("id").toString();
        response.assertThat()
                .statusCode(201)
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Попытка авторизации курьера без логина")
    public void LoginCourierTestWithoutLogin() {
        courier.setLogin("");
        ValidatableResponse response = courierClient.login(Login.from(courier));
        response.assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .statusCode(400);
    }

    @Test
    @DisplayName("Попытка авторизации курьера без пароля")
    public void LoginCourierTestWithoutPassword() {
        courier.setPassword("");
        ValidatableResponse response = courierClient.login(Login.from(courier));
        response.assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and().statusCode(400);
    }

    @Test
    @DisplayName("Попытка авторизации курьера несуществующим логином")
    public void LoginCourierTestInvalidLogin() {
        courier.setLogin("Qwerty_admin");
        ValidatableResponse response = courierClient.login(Login.from(courier));
        response.assertThat().body("message", equalTo("Учетная запись не найдена"))
                .statusCode(404);
    }

    @After
    public void cleanUp () {
        if (id != null) {
            courierClient.delete(id);
        }
    }
}





