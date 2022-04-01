import Tools.ConfigFileReader;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static io.restassured.RestAssured.given;

public class UpdateClientTest {
    static ConfigFileReader configFileReader;
    String endpoint = "client";
    @Test
            public void updateClient_200OK(){
        configFileReader = new ConfigFileReader();
        String url = configFileReader.getApplicationUrl();
        String client_id = ClientsTest.getclient_id();
        String firstName = "Roy";
        String lastName = "Batty";
        String phone = "+48 500 190 590";
        String requestBody ="{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\",\"phone\":\"" + phone + "\"}";
        String tokenGenerated = GetToken.authAndGetToken();
        Response response = given()
                .header("X-API-KEY", tokenGenerated)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(url + endpoint + "/" + client_id);
        System.out.println("Strzelamy do: " + url + endpoint + "/" + client_id);
        System.out.println("Wysłano: " + requestBody);
        Assertions.assertEquals(200,response.statusCode());
        System.out.println("getClientDetails: Szczegóły klienta pobrane");
        ResponseBody body = response.getBody();
        String bodyStringValue = body.asString();
        System.out.println("Odpowiedziano" + bodyStringValue);
        Assert.assertTrue(bodyStringValue.contains("\"id\":\"" + client_id + ""));
        Assert.assertTrue(bodyStringValue.contains("\"firstName\":\"" + firstName + ""));
        Assert.assertTrue(bodyStringValue.contains("\"lastName\":\"" + lastName + ""));
        Assert.assertTrue(bodyStringValue.contains("\"phone\":\"" + phone + ""));
        System.out.println("updateClient: Szczegóły klienta mają poprawny format");
    }
    @Test
    public void updateClient_403InvalidApiKey(){
        configFileReader = new ConfigFileReader();
        String url = configFileReader.getApplicationUrl();
        String client_id = ClientsTest.getclient_id();
        Response response = given()
                .header("X-API-KEY", "wrongToken")
                .contentType("application/json")
                .when()
                .put(url + endpoint + "/" + client_id);
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
    public void updateClient_400InvalidBody_firstName(){
        configFileReader = new ConfigFileReader();
        String url = configFileReader.getApplicationUrl();
        String client_id = ClientsTest.getclient_id();
        String firstName = "";
        String lastName = "A";
        String phone = "+48 121813659";
        String requestBody ="{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\",\"phone\":\"" + phone + "\"}";
        String tokenGenerated = GetToken.authAndGetToken();
        Response response = given()
                .header("X-API-KEY", tokenGenerated)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(url + endpoint + "/" + client_id);
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
    public void updateClient_400InvalidBody_lastName(){
        configFileReader = new ConfigFileReader();
        String url = configFileReader.getApplicationUrl();
        String client_id = ClientsTest.getclient_id();
        String firstName = "Roy";
        String lastName = "";
        String phone = "+48 121813659";
        String requestBody ="{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\",\"phone\":\"" + phone + "\"}";
        String tokenGenerated = GetToken.authAndGetToken();
        Response response = given()
                .header("X-API-KEY", tokenGenerated)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(url + endpoint + "/" + client_id);
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
