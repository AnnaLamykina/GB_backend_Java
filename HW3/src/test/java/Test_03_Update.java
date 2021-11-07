import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class Test_03_Update extends BaseAuthAndData {
    String imageHash = getImageHash();

    @Test
    @DisplayName("Test: update Title for image")
    public void getUpdateInfoTitleTest() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("title", "Pinguin");
        given()
                .headers(headers)
                .body(requestParams.toString())
                .log()
                .all()
                .expect()
                .body("data", is(true))
                .when()
                .post("https://api.imgur.com/3/image/{imageHash}",imageHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Test: update Description for image")
    public void getUpdateInfoDescriptionTest() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("description", "This is pinguin");
        given()
                .headers(headers)
                .body(requestParams.toString())
                .log()
                .all()
                .expect()
                .body("data", is(true))
                .when()
                .post("https://api.imgur.com/3/image/{imageHash}",imageHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
