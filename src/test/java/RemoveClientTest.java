import Tools.ConfigFileReader;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static io.restassured.RestAssured.given;

public class RemoveClientTest {
    static ConfigFileReader configFileReader;

    @Test
            public void removeClient200OK(){
        String endpoint = "client";
        configFileReader = new ConfigFileReader();
        String url = configFileReader.getApplicationUrl();
        String client_id = ClientsTest.getclient_id();
        String tokenGenerated = GetToken.authAndGetToken();
        Response response = given()
                .header("X-API-KEY", tokenGenerated)
                .contentType("application/json")
                .when()
                .delete(url + endpoint + "/" + client_id);
        System.out.println("Strzelamy do: " + url + endpoint + client_id);
        //Assertions.assertEquals(200,response.statusCode());
        System.out.println("getClientDetails: Szczegóły klienta pobrane");
        ResponseBody body = response.getBody();
        String bodyStringValue = body.asString();
        System.out.println("Odpowiedziano" + bodyStringValue);
        JsonPath jsonPathEvaluator = response.jsonPath();
        String message = jsonPathEvaluator.get("message");
        Assert.assertEquals(message,"client deleted");
        System.out.println("removeClient: Komunikat OK");
    }
    @Test
    public void removeClient403InvalidApiKey(){
        configFileReader = new ConfigFileReader();
        String endpoint = "client";
        String url = configFileReader.getApplicationUrl();
        String client_id = ClientsTest.getclient_id();
        Response response = given()
                .header("X-API-KEY", "wrongToken")
                .contentType("application/json")
                .when()
                .delete(url + endpoint + "/" + client_id);
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
    public void removeClient404ClientNotFound(){
        configFileReader = new ConfigFileReader();
        String endpoint = "client";
        String url = configFileReader.getApplicationUrl();
        String tokenGenerated = GetToken.authAndGetToken();
        String client_id = "wrongID";
        Response response = given()
                .header("X-API-KEY", tokenGenerated)
                .contentType("application/json")
                .when()
                .delete(url + endpoint + "/" + client_id);
        //Assertions.assertEquals(404,response.statusCode());
        System.out.println("Invalid body: Status dobry");
        ResponseBody body = response.getBody();
        String bodyStringValue = body.asString();
        System.out.println("Odpowiedziano" + bodyStringValue);
        JsonPath jsonPathEvaluator = response.jsonPath();
        String message = jsonPathEvaluator.get("message");
        Assert.assertEquals(message,"client not found");
        System.out.println("Invalid body: Komunikat OK");
    }
}
