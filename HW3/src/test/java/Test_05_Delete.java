import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class Test_05_Delete extends BaseAuthAndData{
    String imageHash = getImageHash();

    @Test
    @DisplayName("Test: Image Deletion")
    public void imageDeletion() {
        given()
                .headers(headers)
                .log()
                .all()
                .expect()
                .body("success", is(true))
                .when()
                .delete("https://api.imgur.com/3/image/{imageHash}",imageHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Test: make a deleted picture favorite")
    public void favoriteDeletedTest() throws InterruptedException {
        given()
                .headers(headers)
                .log()
                .all()
                .expect()
                .when()
                .post("https://api.imgur.com/3/image/{imageHash}/favorite",imageHash)
                .prettyPeek()
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Test: get deleted image")
    public void getDeletedImage() {
        given()
                .headers(headers)
                .log()
                .all()
                .expect()
                .when()
                .get("https://api.imgur.com/3/image/{imageHash}",imageHash)
                .prettyPeek()
                .then()
                .statusCode(404);
    }

}
