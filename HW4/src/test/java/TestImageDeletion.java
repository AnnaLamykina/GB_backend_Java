import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.given;

public class TestImageDeletion extends BaseClass {
    String imageHash;

    @BeforeEach
    @Test
    void uploadFileTest() throws IOException {
        File imageFile = new File(getFilePath());
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
    @DisplayName("Test: Image Deletion")
    public void imageDeletion() {
        given(requestSpecBuilder.build(), responseSpecBuilder.build())
                .delete("https://api.imgur.com/3/image/{imageHash}", imageHash)
                .prettyPeek();
    }

    @Test
    @DisplayName("Test: make a deleted picture favorite")
    public void favoriteDeletedTest() {
        imageDeletion();
        given(requestSpecBuilder.build(), notFoundSpecBuilder.build())
                .post("https://api.imgur.com/3/image/" + imageHash + "/favorite")
                .prettyPeek();
    }

    @Test
    @DisplayName("Test: get deleted image")
    public void getDeletedImage() {
        imageDeletion();
        given(requestSpecBuilder.build(), notFoundSpecBuilder.build())
                .get("https://api.imgur.com/3/image/" + imageHash)
                .prettyPeek();
    }
}
