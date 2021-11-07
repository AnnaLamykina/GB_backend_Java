import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;


import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class Test_06_ImageWithoutAuth extends BaseDataWithoutAuth{
    @Test
    @DisplayName("Test: upload image and get hash without auth")
    public void uploadFileTest() {

        String body   = given()
                .multiPart("image", new File("src/test/resources/pinguin.jpg"))
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .body().asString();
        JSONObject jsonObject=new JSONObject(body);
        JSONObject jsonData = jsonObject.getJSONObject("data");
        setImageHash(jsonData.getString("id"));
        setDeleteImageHash(jsonData.getString("deletehash"));
    }
}
