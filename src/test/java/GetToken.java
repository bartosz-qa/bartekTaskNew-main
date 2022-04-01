import Tools.ConfigFileReader;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import static io.restassured.RestAssured.given;

public class GetToken {
    static ConfigFileReader configFileReader;
    public static String authAndGetToken() {
        configFileReader = new ConfigFileReader();
        String url = configFileReader.getApplicationUrl();
        String username = configFileReader.getUsername();
        String password = configFileReader.getPassword();
        Response response = given()
                .auth()
                .preemptive()
                .basic(username, password)
                .when()
                .post(url + "token");
        Assertions.assertEquals(200, response.statusCode());
        JsonPath jsonPathEvaluator = response.jsonPath();
        String token = jsonPathEvaluator.get("key");
        System.out.println("Magic word:" + token);
        return token;
    }
}