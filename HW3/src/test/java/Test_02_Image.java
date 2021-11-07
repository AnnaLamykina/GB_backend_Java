import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import org.json.JSONObject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.Is.is;

public class Test_02_Image extends BaseAuthAndData {
    static String encodedFile;

    @BeforeEach
    void beforeTest() throws IOException {
        byte[]   byteArray = getFileContent("src/test/resources/pinguin.jpg");
        encodedFile = Base64.getEncoder().encodeToString(byteArray);
    }

    byte[] getFileContent(String fPath) throws IOException {
        return Files.readAllBytes(Paths.get(fPath));
    }

    @Test
    @DisplayName("Test: upload image and get hash")
    public void uploadFileTest() {
        String body   = given()
                .headers(headers)
                .multiPart("image", encodedFile)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("https://api.imgur.com/3/image")
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

    @Test
    @DisplayName("Test: get image")
    public void getImage() {
        given()
                .headers(headers)
                .log()
                .all()
                .expect()
                .when()
                .get("https://api.imgur.com/3/image/{imageHash}",getImageHash())
                .prettyPeek()
                .then()
                .statusCode(200);
    }

}
