import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
public class Test_01_Account extends BaseAuthAndData {

    @Test
    @DisplayName("Test: get account info")
    public void getAccountInfoTest() {
        given()
                .headers(headers)
                .log()
                .all()
                .when()
                .get("https://api.imgur.com/3/account/{username}",getUsername())
                .prettyPeek()
                .then()
                .statusCode(200);
    }

}
