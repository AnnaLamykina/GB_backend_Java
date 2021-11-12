import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class TestAccount extends BaseClass {

    @Test
    @DisplayName("Test: get account info")
    public void getAccountInfoTest() {
        given(requestSpecBuilder.build(), responseSpecBuilder.build())
                .get("https://api.imgur.com/3/account/{username}", getUsername())
                .prettyPeek();
    }

}
