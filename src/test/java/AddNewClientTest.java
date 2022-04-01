import Tools.ConfigFileReader;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static io.restassured.RestAssured.given;

public class AddNewClientTest {
    static ConfigFileReader configFileReader;
    String tokenGenerated = GetToken.authAndGetToken();
    String endpoint = "client";

    @Test
    public void addNewClient_200OK(){
        configFileReader = new ConfigFileReader();
        String url = configFileReader.getApplicationUrl();
        String firstName = "Roy";
        String lastName = "Batty";
        String phone = "+48 500 190 590";
        String requestBody ="{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\",\"phone\":\"" + phone + "\"}";
        Response response = given()
                .header("X-API-KEY", tokenGenerated)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(url + endpoint);
        System.out.println("Strzelamy do: " + url + endpoint);
        System.out.println("Wysłano: " + requestBody);
        ResponseBody body = response.getBody();
        String bodyStringValue = body.asString();
        System.out.println("Odpowiedziano" + bodyStringValue);
        Assertions.assertEquals(200,response.statusCode());
        Assert.assertTrue(bodyStringValue.contains(firstName));
        Assert.assertTrue(bodyStringValue.contains(lastName));
        Assert.assertTrue(bodyStringValue.contains(phone));
        Assert.assertTrue(bodyStringValue.contains("id"));
    }
    @Test
    public void addNewClient_nophone_200OK(){
        configFileReader = new ConfigFileReader();
        String url = configFileReader.getApplicationUrl();
        String firstName = "Roy";
        String lastName = "Batty";
        String phone = "";
        String requestBody ="{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\",\"phone\":\"" + phone + "\"}";
        Response response = given()
                .header("X-API-KEY", tokenGenerated)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(url + endpoint);
        System.out.println("Strzelamy do: " + url + endpoint);
        System.out.println("Wysłano: " + requestBody);
        ResponseBody body = response.getBody();
        String bodyStringValue = body.asString();
        System.out.println("Odpowiedziano" + bodyStringValue);
        Assertions.assertEquals(200,response.statusCode());
        Assert.assertTrue(bodyStringValue.contains(firstName));
        Assert.assertTrue(bodyStringValue.contains(lastName));
        Assert.assertTrue(bodyStringValue.contains(phone));
        Assert.assertTrue(bodyStringValue.contains("id"));
    }
    @Test
    public void addNewClient_403InvalidApiKey(){
        configFileReader = new ConfigFileReader();
        String url = configFileReader.getApplicationUrl();
        Response response = given()
                .header("X-API-KEY", "wrongToken")
                .contentType("application/json")
                .when()
                .post(url + endpoint);
        Assertions.assertEquals(403,response.statusCode());
        System.out.println("Invalid API key: Status dobry");
        ResponseBody body = response.getBody();
        String bodyStringValue = body.asString();
        System.out.println("Odpowiedziano" + bodyStringValue);
        JsonPath jsonPathEvaluator = response.jsonPath();
        String message = jsonPathEvaluator.get("message");
        Assert.assertEquals(message,"invalid or missing api key");
        System.out.println("Invalid API key: Komunikat OK");
    }
    @Test
    public void addNewClient_400InvalidBody_firstName(){
        configFileReader = new ConfigFileReader();
        String url = configFileReader.getApplicationUrl();
        String firstName = "";
        String lastName = "Batty";
        String phone = "+48 500 190 590";
        String requestBody ="{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\",\"phone\":\"" + phone + "\"}";
        Response response = given()
                .header("X-API-KEY", tokenGenerated)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(url + endpoint);
        System.out.println("Strzelamy do: " + url + endpoint);
        System.out.println("Wysłano: " + requestBody);
        Assertions.assertEquals(400,response.statusCode());
        System.out.println("Invalid body: Status dobry");
        ResponseBody body = response.getBody();
        String bodyStringValue = body.asString();
        System.out.println("Odpowiedziano" + bodyStringValue);
        JsonPath jsonPathEvaluator = response.jsonPath();
        String message = jsonPathEvaluator.get("message");
        Assert.assertEquals(message,"firstName is required");
        System.out.println("Invalid body: Komunikat OK");
    }
    @Test
    public void addNewClient_400InvalidBody_lastName(){
        configFileReader = new ConfigFileReader();
        String url = configFileReader.getApplicationUrl();
        String firstName = "Roy";
        String lastName = "";
        String phone = "+48 500 190 590";
        String requestBody ="{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\",\"phone\":\"" + phone + "\"}";
        Response response = given()
                .header("X-API-KEY", tokenGenerated)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(url + endpoint);
        System.out.println("Strzelamy do: " + url + endpoint);
        System.out.println("Wysłano: " + requestBody);
        Assertions.assertEquals(400,response.statusCode());
        System.out.println("Invalid body: Status dobry");
        ResponseBody body = response.getBody();
        String bodyStringValue = body.asString();
        System.out.println("Odpowiedziano" + bodyStringValue);
        JsonPath jsonPathEvaluator = response.jsonPath();
        String message = jsonPathEvaluator.get("message");
        Assert.assertEquals(message,"lastName is required");
        System.out.println("Invalid body: Komunikat OK");
    }
}
