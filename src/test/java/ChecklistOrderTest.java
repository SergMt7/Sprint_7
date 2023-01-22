import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import model.Client;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class ChecklistOrderTest extends Client {
    static private String checkOrder = "/api/v1/orders";

    @Test
    @DisplayName("Проверяем список заказов")
    public void ChecklistOrderTest() {
        given()
                .spec(getSpec())
                .when()
                .get(checkOrder)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("orders", notNullValue());
    }
}
