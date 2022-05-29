import io.restassured.response.Response;

import static io.restassured.RestAssured.get;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PeselValidatorResponseCodeTests {

    /**
     * send REST GET request containing valid pesel format
     * Expected code: 200 (OK)
     */
    @Test
    public static void validPeselGetRequest_ResponseCodeTest() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=97022137850");
        Assert.assertEquals(response.statusCode(), 200, "Status code does not equals 200");
        System.out.println(response.asPrettyString().contains("Male"));
    }

    /**
     * send REST GET request containing invalid pesel format
     * Expected code: 200 (OK)
     */
    @Test
    public static void inValidPeselGetRequest_ResponseCodeTest() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=97o22137850");
        Assert.assertEquals(response.statusCode(), 200, "Status code does not equals 200");
    }

    /**
     * send REST GET request with missing pesel argument
     * Expected code: 400
     */
    @Test
    public static void missingPeselArgumentGetRequest_ResponseCodeTest() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel");
        Assert.assertEquals(response.statusCode(), 400, "Status code does not equals 400");
    }
}