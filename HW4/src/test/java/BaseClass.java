import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public abstract class BaseClass {
    static Properties properties = new Properties();
    static String Token;
    static String Username;
    static String FilePath;
    static RequestSpecBuilder requestSpecBuilder;
    static ResponseSpecBuilder responseSpecBuilder;
    static ResponseSpecBuilder notFoundSpecBuilder;

    public static String getFilePath() {
        return FilePath;
    }

    public static void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public static String getUsername() {
        return Username;
    }

    @BeforeAll
    static void setUp() {
        setProperties();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());

        responseSpecBuilder = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200);

        requestSpecBuilder = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer " + Token);

        notFoundSpecBuilder = new ResponseSpecBuilder()
                .expectStatusCode(404);
    }

    private static void setProperties() {
        try (FileInputStream fileInputStream = new FileInputStream("src/test/resources/application.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Username = properties.getProperty("username");
        Token = properties.getProperty("token");
        setFilePath(properties.getProperty("image"));
    }
}
