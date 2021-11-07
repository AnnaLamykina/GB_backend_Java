import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class Test_04_Favorite extends BaseAuthAndData {
    String imageHash = getImageHash();

    @Test
    @DisplayName("Test: make a picture favorite")
    public void getFavoriteTest() {
        given()
                .headers(headers)
                .log()
                .all()
                .expect()
                .body("data", is("favorited"))
                .when()
                .post("https://api.imgur.com/3/image/{imageHash}/favorite",imageHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
