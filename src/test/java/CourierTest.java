import io.qameta.allure.junit4.DisplayName;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
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
    public void CreateNewCourier() {
        ValidatableResponse response = courierClient.create(courier);
        response.statusCode(201);
        ValidatableResponse loginResponse = courierClient.login(Login.from(courier));
        id = loginResponse.extract().path("id").toString();
    }

    @Test
    @DisplayName("Попытка создания курьера без логина")
    public void CreateNewCourierWithoutLogin() {
        courier.setLogin("");
        ValidatableResponse response = courierClient.create(courier);
        response.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and().statusCode(400);
    }

    @Test
    @DisplayName("Попытка создания курьера без пароля")
    public void CreateNewCourierWithoutPassword() {
        courier.setPassword("");
        ValidatableResponse response = courierClient.create(courier);
        response.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and().statusCode(400);
    }

    @Test
    @DisplayName("Попытка дублирования курьера с одинаковым логином")
    public void CreatureDoubleNewCourier() {
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
