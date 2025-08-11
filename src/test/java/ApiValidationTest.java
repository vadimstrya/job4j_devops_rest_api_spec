import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class ApiValidationTest {

    // Указываем путь к файлу спецификации swagger.yaml
    private static final OpenApiValidationFilter validationFilter = new OpenApiValidationFilter("swagger.yaml");

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080/calc";
    }

    @Test
    public void testSummarise() {
        Response response = RestAssured.given()
                .filter(validationFilter) // Применяем фильтр для проверки соответствия спецификации
                .header("Content-type", "application/json")
                .and()
                .body(Map.of("first", 1D, "second", 2D))
                .when()
                .post("/summarise")
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(1D, response.jsonPath().getDouble("firstArg"));
        Assertions.assertEquals(2D, response.jsonPath().getDouble("secondArg"));
        Assertions.assertEquals(3D, response.jsonPath().getDouble("result"));
    }

    @Test
    public void testLogs() {
        RestAssured.given()
                .filter(validationFilter) // Применяем фильтр для проверки соответствия спецификации
                .when()
                .get("/")
                .then()
                .assertThat()
                .statusCode(200);
    }
}
