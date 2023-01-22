import io.qameta.allure.junit4.DisplayName;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import model.CourierGenerator;
import model.Courier;
import model.CourierClient;
import model.Login;

public class CourierTest {
    private  Courier courier;
    private  CourierClient courierClient;
    private String id;

    @Before
    public void setUp() {
        courier = CourierGenerator.random();
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Создание нового курьера")
    public void сreateNewCourier() {
        ValidatableResponse response = courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(Login.from(courier));
        id = loginResponse.extract().path("id").toString();
        response.statusCode(201);
    }

    @Test
    @DisplayName("Попытка создания курьера без логина")
    public void сreateNewCourierWithoutLogin() {
        courier.setLogin("");
        ValidatableResponse response = courierClient.create(courier);
        response.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and().statusCode(400);
    }

    @Test
    @DisplayName("Попытка создания курьера без пароля")
    public void сreateNewCourierWithoutPassword() {
        courier.setPassword("");
        ValidatableResponse response = courierClient.create(courier);
        response.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and().statusCode(400);
    }

    @Test
    @DisplayName("Попытка дублирования курьера с одинаковым логином")
    public void сreatureDoubleNewCourier() {
        courierClient.create(courier);
        ValidatableResponse response = courierClient.create(courier);
        response.assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and().statusCode(409);
    }

    @After
    public void cleanUp() {
        if (id != null) {
            courierClient.delete(id);
        }
    }
}
