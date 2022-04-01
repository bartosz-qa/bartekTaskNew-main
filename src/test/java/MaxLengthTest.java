import Tools.ConfigFileReader;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static io.restassured.RestAssured.given;

public class MaxLengthTest {
    static ConfigFileReader configFileReader;
    String tokenGenerated = GetToken.authAndGetToken();
    String endpoint = "client";

    @Test
    public void addNewClient_MaxLength_50_200OK(){
        configFileReader = new ConfigFileReader();
        String url = configFileReader.getApplicationUrl();
        String firstName = "01234567890123456789012345678901234567890123456789AB"; //52 znaki
        String lastName = "01234567890123456789012345678901234567890123456789AB"; //52 znaki
        String phone = "01234567890123456789012345678901234567890123456789AB"; //52 znaki
        String firstName50 = "01234567890123456789012345678901234567890123456789"; //50 znaków
        String lastName50 = "01234567890123456789012345678901234567890123456789"; //50 znaków
        String phone50 = "01234567890123456789012345678901234567890123456789"; //50 znaków
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
        Assert.assertFalse(bodyStringValue.contains(firstName));
        Assert.assertFalse(bodyStringValue.contains(lastName));
        Assert.assertFalse(bodyStringValue.contains(phone));
        Assert.assertTrue(bodyStringValue.contains(firstName50));
        Assert.assertTrue(bodyStringValue.contains(lastName50));
        Assert.assertTrue(bodyStringValue.contains(phone50));
        Assert.assertTrue(bodyStringValue.contains("id"));
        System.out.println("MaxLength 50 przyjęto tylko 50 znaków");
    }

}
