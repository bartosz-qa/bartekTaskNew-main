import Tools.ConfigFileReader;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static io.restassured.RestAssured.given;

public class AuthTest {
  static ConfigFileReader configFileReader;
    String endpoint = "token";

    @Test
        public void authAndGetToken() {
        configFileReader = new ConfigFileReader();
        String url = configFileReader.getApplicationUrl();
        String username = configFileReader.getUsername();
        String password = configFileReader.getPassword();
        Response response = given()
            .auth()
                .preemptive()
                .basic(username, password)
            .when()
                .post(url + endpoint);
        Assertions.assertEquals(200,response.statusCode());
        JsonPath jsonPathEvaluator = response.jsonPath();
        String token = jsonPathEvaluator.get("key");
        System.out.println("Magic word:" + token);

//            .then()
//                .assertThat()
//                .statusCode(200);

        //System.out.println(response);
//  Assertions.assertEquals(200,response.statusCode());
//  System.out.println(response);
  }
    @Test
  public void methodNotAllowed(){
      configFileReader = new ConfigFileReader();
      String url = configFileReader.getApplicationUrl();
      String username = configFileReader.getUsername();
      String password = configFileReader.getPassword();
      Response response = given()
              .auth()
              .preemptive()
              .basic(username, password)
              .when()
              .get(url + endpoint);
      Assertions.assertEquals(405,response.statusCode());

  }
    @Test
  public void invalidCredentials(){
      configFileReader = new ConfigFileReader();
      String url = configFileReader.getApplicationUrl();
      Response response = given()
              .auth()
              .preemptive()
              .basic("wrong", "notsogoodeither")
              .when()
              .post(url + endpoint);
      Assertions.assertEquals(400,response.statusCode());
        System.out.println("Invalid credentials: Status dobry");
      ResponseBody body = response.getBody();
      String bodyStringValue = body.asString();
        System.out.println("Odpowiedziano" + bodyStringValue);
      JsonPath jsonPathEvaluator = response.jsonPath();
      String message = jsonPathEvaluator.get("message");
      Assert.assertEquals(message,"invalid username or password");
        System.out.println("Invalid credentials: Komunikat OK");
  }
}

