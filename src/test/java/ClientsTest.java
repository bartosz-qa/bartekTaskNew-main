import Tools.ConfigFileReader;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static io.restassured.RestAssured.given;

public class ClientsTest {
    static ConfigFileReader configFileReader;

    public static String getDetail(String detail){
        configFileReader = new ConfigFileReader();
        String newUrl = allClients308GetNewUrl();
        String tokenGenerated = GetToken.authAndGetToken();
        Response response = given()
                .header("X-API-KEY", tokenGenerated)
                .contentType("application/json")
                .when()
                .get(newUrl);
        //Assertions.assertEquals(200, response.statusCode());
        ResponseBody body = response.getBody();
        String bodyStringValue = body.asString();
        System.out.println("Odpowiedziano" + bodyStringValue);
        JsonPath jsonPathEvaluator = response.jsonPath();
        String client_id = jsonPathEvaluator.get("clients[0].id");
        String firstName = jsonPathEvaluator.get("clients[0].firstName");
        String lastName = jsonPathEvaluator.get("clients[0].lastName");
        String phone = jsonPathEvaluator.get("clients[0].phone");
        String clientDetail = jsonPathEvaluator.get("clients[0]." + detail);
        return clientDetail;
    }
    public static String getclient_id(){
        return getDetail("id");
    }
    public static String getfirstName(){
        return getDetail("firstName");
    }
    public static String getlastName(){
        return getDetail("lastName");
    }
    public static String getphone(){
        return getDetail("phone");
    }

    public static String allClients308GetNewUrl() {
        String endpoint = "clients";
        String tokenGenerated = GetToken.authAndGetToken();
        configFileReader = new ConfigFileReader();
        String url = configFileReader.getApplicationUrl();
            Response response = given()
                    .header("X-API-KEY", tokenGenerated)
                    .contentType("application/json")
                    .when()
                    .redirects().follow(false)
                    .get(url + endpoint);
            Assertions.assertEquals(308, response.statusCode());
            String urlRedirected = response.then().extract().header("Location");
            System.out.println("Nowy adres: " + urlRedirected);
            return urlRedirected;
        }
    @Test
        public void listAllClients200OK() {
        String newUrl = allClients308GetNewUrl();
        String client_id = ClientsTest.getclient_id();
        String firstName = ClientsTest.getfirstName();
        String lastName = ClientsTest.getlastName();
        String phone = ClientsTest.getphone();
        String tokenGenerated = GetToken.authAndGetToken();
        Response response = given()
                        .header("X-API-KEY", tokenGenerated)
                        .contentType("application/json")
                        .when()
                        .get(newUrl);
        System.out.println("Strzelamy do: " + newUrl);
                Assertions.assertEquals(200, response.statusCode());
                ResponseBody body = response.getBody();
                String bodyStringValue = body.asString();
                System.out.println("Odpowiedziano" + bodyStringValue);
        Assert.assertTrue(bodyStringValue.contains("\"clients\""));
        Assert.assertTrue(bodyStringValue.contains("\"firstName\":\"" + firstName + ""));
        Assert.assertTrue(bodyStringValue.contains("\"id\":\"" + client_id + ""));
        Assert.assertTrue(bodyStringValue.contains("\"lastName\":\"" + lastName + ""));
        Assert.assertTrue(bodyStringValue.contains("\"phone\":\"" + phone + ""));
        System.out.println("listAllClients: Szczegóły klienta mają poprawny format");
            }
    @Test
    public void allClients403InvalidApiKey(){
        configFileReader = new ConfigFileReader();
        String newUrl = allClients308GetNewUrl();
        Response response = given()
                .header("X-API-KEY", "wrongToken")
                .contentType("application/json")
                .when()
                .get(newUrl);
        System.out.println("Strzelamy do: " + newUrl);
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
        }

