import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class TestImage extends BaseClass {
    static String encodedFile;
    static File imageFile;

    static String imageHash;

    static void beforeTest() throws IOException {
        byte[] byteArray = getFileContent(getFilePath());
        encodedFile = Base64.getEncoder().encodeToString(byteArray);
        imageFile = new File(getFilePath());
    }

    static byte[] getFileContent(String fPath) throws IOException {
        return Files.readAllBytes(Paths.get(fPath));
    }

    @BeforeAll
    @Test
    @DisplayName("Test: upload image and get hash")
    static void uploadFileTest() throws IOException {
        beforeTest();
        RequestSpecification requestSpecification = requestSpecBuilder.addMultiPart("image", imageFile).build();
        String body = given(requestSpecification, responseSpecBuilder.build())
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .body().asString();
        JSONObject jsonObject = new JSONObject(body);
        JSONObject jsonData = jsonObject.getJSONObject("data");
        imageHash = jsonData.getString("id");
    }

    @Test
    @DisplayName("Test: upload image64 and get hash")
    public void uploadFileTest64() {
        RequestSpecification requestSpecification = requestSpecBuilder.addMultiPart("image", encodedFile).build();
        given(requestSpecification, responseSpecBuilder.build())
                .post("https://api.imgur.com/3/image")
                .prettyPeek();
    }

    @Test
    @DisplayName("Test: get image")
    public void getImage() {
        ResponseSpecification responseSpecification = responseSpecBuilder.expectBody("data.id", is(notNullValue())).build();
        given(requestSpecBuilder.build(), responseSpecification)
                .get("https://api.imgur.com/3/image/{imageHash}", imageHash)
                .prettyPeek();
    }

    @Test
    @DisplayName("Test: upload image without auth")
    public void uploadFileTestWithoutAuth() {
        RequestSpecification requestSpecification = new RequestSpecBuilder().addMultiPart("image", imageFile).build();
        given(requestSpecification, responseSpecBuilder.build())
                .post("https://api.imgur.com/3/upload")
                .prettyPeek();
    }

    @Test
    @DisplayName("Test: make a picture favorite")
    public void favoriteTest() {
        given(requestSpecBuilder.build(), responseSpecBuilder.build())
                .post("https://api.imgur.com/3/image/{imageHash}/favorite", imageHash)
                .prettyPeek();
    }

    @Test
    @DisplayName("Test: update Title for image")
    public void updateInfoTitleTest() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("title", "Pinguin");
        RequestSpecification rsp = requestSpecBuilder.setBody(requestParams.toString()).build();
        given(rsp, responseSpecBuilder.build())
                .post("https://api.imgur.com/3/image/" + imageHash)
                .prettyPeek();
    }

    @Test
    @DisplayName("Test: update Description for image")
    public void updateInfoDescriptionTest() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("description", "This is pinguin");
        RequestSpecification rsp = requestSpecBuilder.setBody(requestParams.toString()).build();
        given(rsp, responseSpecBuilder.build())
                .post("https://api.imgur.com/3/image/{imageHash}", imageHash)
                .prettyPeek();
    }


}
