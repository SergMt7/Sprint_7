import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import model.Order;
import model.Client;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderTest extends Client {
    private static final String CREATE_ORDER = "/api/v1/orders";
    private static final String CANCEL_ORDER = "/api/v1/orders/cancel";
    int track;
    private String firstName = "Naruto";
    private String lastName = "Uchiha";
    private String address = "Konoha, 142 apt.";
    private String metroStation = "4";
    private String phone = "+7 800 355 35 35";
    private int rentTime = 5;
    private String deliveryDate = "2020-06-06";
    private String comment = "Saske, come back to Konoha";
    private String[] color;

    public OrderTest(String[]color) {
        this.color = color;
    }
    @Parameterized.Parameters
    public static Object[][] orderColor() {
        return new Object[][]{
                {"BLACK", null},
                {null, "GRAY"},
                {"BLACK", "GRAY"},
                {null, null}
        };
    }
    @Test
    @DisplayName("Создание заказов с разным цветом")
    public void orderTest(){
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response response = given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(CREATE_ORDER);
        response.then().log().all()
                .assertThat()
                .statusCode(201)
                .and()
                .body("track", notNullValue());
        track = response.path("track");
    }
    @After
    public ValidatableResponse tearDown(int track) {
        return given().log().all()
                .spec(getSpec())
                .body(track)
                .when()
                .put(CANCEL_ORDER)
                .then();
    }
}