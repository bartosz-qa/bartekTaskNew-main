import Tools.ConfigFileReader;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static io.restassured.RestAssured.given;

public class GetClientDetailsTest {
    static ConfigFileReader configFileReader;
    String tokenGenerated = GetToken.authAndGetToken();
    String endpoint = "client";
    @Test
            public void getClientDetails200OK(){
        configFileReader = new ConfigFileReader();
        String url = configFileReader.getApplicationUrl();
        String client_id = ClientsTest.getclient_id();
        String firstName = ClientsTest.getfirstName();
        String lastName = ClientsTest.getlastName();
        String phone = ClientsTest.getphone();
        String tokenGenerated = GetToken.authAndGetToken();
        Response response = given()
                .header("X-API-KEY", tokenGenerated)
                .contentType("application/json")
                .when()
                .get(url + endpoint + "/" + client_id);
        System.out.println("Strzelamy do: " + url + endpoint + "/" + client_id);
        System.out.println("getClientDetails: Szczegóły klienta pobrane");
        ResponseBody body = response.getBody();
        String bodyStringValue = body.asString();
        System.out.println("Odpowiedziano" + bodyStringValue);
        Assertions.assertEquals(200,response.statusCode());
        Assert.assertTrue(bodyStringValue.contains("\"firstName\":\"" + firstName + ""));
        Assert.assertTrue(bodyStringValue.contains("\"id\":\"" + client_id + ""));
        Assert.assertTrue(bodyStringValue.contains("\"lastName\":\"" + lastName + ""));
        Assert.assertTrue(bodyStringValue.contains("\"phone\":\"" + phone + ""));
        System.out.println("getClientDetails: Szczegóły klienta mają poprawny format");
    }
    @Test
    public void getClientDetails403InvalidApiKey(){
        configFileReader = new ConfigFileReader();
        String url = configFileReader.getApplicationUrl();
        String client_id = ClientsTest.getclient_id();
        Response response = given()
                .header("X-API-KEY", "wrongToken")
                .contentType("application/json")
                .when()
                .get(url + endpoint + "/" + client_id);
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
    public void getClientDetails404ClientNotFound(){
        configFileReader = new ConfigFileReader();
        String url = configFileReader.getApplicationUrl();
        String client_id = "WrongId";
        Response response = given()
                .header("X-API-KEY", tokenGenerated)
                .contentType("application/json")
                .when()
                .get(url + endpoint + "/" + client_id);
        Assertions.assertEquals(404,response.statusCode());
        System.out.println("Client not found: Status dobry");
        ResponseBody body = response.getBody();
        String bodyStringValue = body.asString();
        System.out.println("Odpowiedziano" + bodyStringValue);
        JsonPath jsonPathEvaluator = response.jsonPath();
        String message = jsonPathEvaluator.get("message");
        Assert.assertEquals(message,"client not found");
        System.out.println("Client not found: Komunikat OK");

    }
}
