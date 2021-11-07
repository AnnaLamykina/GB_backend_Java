import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class BaseAuthAndData {
    static Properties properties = new Properties();
    static Map<String, String> headers = new HashMap<>();
    static String Token;
    static String Username;
    static String ImageHash;
    static String DeleteImageHash;

    public static String getImageHash() {
        return ImageHash;
    }

    public static void setImageHash(String imageHash) {
        ImageHash = imageHash;
    }

    public static String getToken() {
        return Token;
    }

    public static void setToken(String token) {
        Token = token;
    }

    public static String getUsername() {
        return Username;
    }

    public static void setUsername(String username) {
        Username = username;
    }

    public static String getDeleteImageHash() {
        return DeleteImageHash;
    }

    public static void setDeleteImageHash(String deleteImageHash) {
        DeleteImageHash = deleteImageHash;
    }

    @BeforeAll
    static void setUp() {
        setProperties();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
        headers.put("Authorization", "Bearer "+Token);
    }

    private static void setProperties(){
        try (FileInputStream fileInputStream = new FileInputStream("src/test/resources/application.properties")) {
            properties.load(fileInputStream);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        setUsername(properties.getProperty("username"));
        setToken(properties.getProperty("token"));
    }
}
