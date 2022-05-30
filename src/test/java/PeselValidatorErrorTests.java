import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.get;

public class PeselValidatorErrorTests {

    /**
     * Too short pesel.
     * result:
     * only: 'INVL' (InValidLength)
     */
    @Test
    public static void shouldReturnINVL1() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=123");
        ArrayList<String> error = response.path("errors.errorCode");
        Assert.assertEquals(error.get(0), "INVL", "The error code does not match ");
        Assert.assertEquals(error.size(),1,"There is more than one error");
    }

    /**
     * Too short pesel.
     * result:
     * 'Invalid length. Pesel should have exactly 11 digits.
     *
     */
    @Test
    public static void shouldReturnINVLmsg1() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=123");
        ArrayList<String> errorMessage = response.path("errors.errorMessage");
        Assert.assertEquals(errorMessage.get(0), "Invalid length. Pesel should have exactly 11 digits.", "The error messages does not match ");
    }

    /**
     * Too short pesel.
     * result:
     * only: 'INVL' (InValidLength)
     */
    @Test
    public static void shouldReturnINVL2() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=123123123123");
        ArrayList<String> error = response.path("errors.errorCode");
        Assert.assertEquals(error.get(0), "INVL", "The error code does not match ");
        Assert.assertEquals(error.size(),1,"There is more than one error");
    }

    /**
     * Too short pesel.
     * result:
     * 'Invalid length. Pesel should have exactly 11 digits.
     *
     */
    @Test
    public static void shouldReturnINVLmsg2() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=123123123123");
        ArrayList<String> errorMessage = response.path("errors.errorMessage");
        Assert.assertEquals(errorMessage.get(0), "Invalid length. Pesel should have exactly 11 digits.", "The error messages does not match ");
        Assert.assertEquals(errorMessage.size(),1,"There is more than one error");
    }

    /**
     * pesel: containing 11 letters
     * result:
     * 'NBRQ' error code
     */
    @Test
    public static void shouldReturnNBRQ() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=jedenaściel");
        ArrayList<String> error = response.path("errors.errorCode");
        Assert.assertEquals(error.get(0), "NBRQ", "The error codes does not match ");
    }

    /**
     * pesel: containing 11 letters
     * result:
     * 'NBRQ' error code
     */
    @Test
    public static void shouldReturnNBRQmsg() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=jedenaściel");
        ArrayList<String> error = response.path("errors.errorMessage");
        Assert.assertEquals(error.get(0), "Invalid characters. Pesel should be a number.", "The error codes does not match ");
    }

    /**
     * pesel: containing 10 digits and one special char
     * result:
     * 'NBRQ' error code
     * 'Invalid characters. Pesel should be a number.' error message
     */
    @DataProvider
    public static Object[][] specialCharacters() {
        return new Object[][]{
                {"!"}, {"@"},
                {"#"}, {"$"},
                {"^"}, {"%"},
                {"*"}, {"("},
                {")"}, {"_"},
                {"+"}, {"-"},
                {"="}, {"\\"},
                {"["}, {"]"},
                {"{"}, {"}"},
                {"|"}, {";"},
                {"'"}, {":"},
                {"\""}, {","},
                {"<"}, {"."},
                {">"}, {"/"},
                {"`"}, {"~"},
                {"~"}
        };
    }

    @Test(dataProvider = "specialCharacters")
    public static void shouldReturnNBRQAndMsg1(String specialChar) {
        String url = "https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=1231231231";

        Response response = get(url.concat(specialChar));
        ArrayList<String> errorCode = response.path("errors.errorCode");
        ArrayList<String> errorMessage = response.path("errors.errorMessage");
        Assert.assertEquals(errorCode.get(0), "NBRQ", "error code does not equal NBRQ for " + specialChar);
        Assert.assertEquals(errorMessage.get(0), "Invalid characters. Pesel should be a number.", "The error message does not match for: " + specialChar);
        Assert.assertEquals(errorCode.size(),1,"There is more than 1 error code returned");
        Assert.assertEquals(errorMessage.size(),1,"There is more than 1 error message displayed");
    }

    @DataProvider
    public static Object[][] letters() {
        return new Object[][]{
                {"a"}, {"n"},
                {"b"}, {"o"},
                {"c"}, {"p"},
                {"d"}, {"q"},
                {"e"}, {"r"},
                {"f"}, {"s"},
                {"g"}, {"t"},
                {"h"}, {"u"},
                {"i"}, {"v"},
                {"j"}, {"w"},
                {"k"}, {"x"},
                {"l"}, {"y"},
                {"ł"}, {"z"},
                {"m"},
        };
    }

    /**
     * Given wrong pesel (containing 10 digits and one letter)
     * the only error code should be 'NBRQ'
     * the only error message should be 'Invalid characters. Pesel should be a number.'
     */
    @Test(dataProvider = "letters")
    public static void shouldReturnNBRQAndMsg2(String letter) {
        String url = "https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=1231231231";

        Response response = get(url.concat(letter));
        ArrayList<String> errorCode = response.path("errors.errorCode");
        ArrayList<String> errorMessage = response.path("errors.errorMessage");
        Assert.assertEquals(errorCode.get(0), "NBRQ", "The error code does not match for: " + letter);
        Assert.assertEquals(errorMessage.get(0), "Invalid characters. Pesel should be a number.", "The error message does not match for: " + letter);
        Assert.assertEquals(errorCode.size(),1,"There is more than 1 error code returned");
        Assert.assertEquals(errorMessage.size(),1,"There is more than 1 error message displayed");
    }

    /**
     * Year: 1998
     * Month: 0
     * Day: 1
     * Error messages that should be triggered:
     * 'INVY'
     * 'INVM'
     */
    @Test
    public static void shouldFailMonth0() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=98000100011");
        ArrayList<String> errorCode = response.path("errors.errorCode");
        Assert.assertEquals(errorCode.get(0), "INVY", "The error code does not match");
        Assert.assertEquals(errorCode.get(1), "INVM", "The error code does not match");
    }

    /**
     * Year: 1998
     * Month: 0
     * Day: 1
     * Error messages that should be triggered:
     * 'Invalid year.'
     * 'Invalid month.'
     */
    @Test
    public static void shouldFailMonth0Msg() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=98000100011");
        ArrayList<String> errorMessage = response.path("errors.errorMessage");
        Assert.assertEquals(errorMessage.get(0), "Invalid year.", "The error message does not match");
        Assert.assertEquals(errorMessage.get(1), "Invalid month.", "The error message does not match");
    }

    /**
     * Year: 1998
     * Month: 13
     * Day: 01
     * Error codes that should be printed:
     * 'INVY'
     * 'INVM'
     */
    @Test
    public static void shouldFailMonth13() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=98130100017");
        ArrayList<String> errorCode = response.path("errors.errorCode");
        Assert.assertEquals(errorCode.get(0), "INVY", "The error code does not match");
        Assert.assertEquals(errorCode.get(1), "INVM", "The error code does not match");
    }

    /**
     * Year : 1998
     * Month: 13
     * Day: 01
     * Error messages that should be triggered:
     * 'Invalid year.'
     * 'Invalid month.'
     */
    @Test
    public static void shouldFailMonth13Msg() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=98130100017");
        ArrayList<String> errorMessage = response.path("errors.errorMessage");
        Assert.assertEquals(errorMessage.get(0), "Invalid year.", "The error message does not match");
        Assert.assertEquals(errorMessage.get(1), "Invalid month.", "The error message does not match");
    }

    /**
     * Year: 1998
     * Month: 12
     * Day: 00
     * only the 'INVD' error code should appear
     */
    @Test
    public static void shouldFailDay0(){
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=98123323122");
        ArrayList<String> errorCode = response.path("errors.errorCode");
        Assert.assertEquals(errorCode.get(0), "INVD", "The error code does not match");
        Assert.assertEquals(errorCode.size(),1,"there are more than one error on the error list");
    }

    /**
     * Year: 1998
     * Month: 12
     * Day: 0
     * 'Invalid day.' error message should appear
     */
    @Test
    public static void shouldFailDay0Msg() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=98123323122");
        ArrayList<String> errorMessage = response.path("errors.errorMessage");
        Assert.assertEquals(errorMessage.get(0), "Invalid day.", "The error message does not match");
        Assert.assertEquals(errorMessage.size(),1,"there are more than one error on the error list");
    }

    /**
     * Year: 1998
     * Month: 12
     * Day: 33
     * only the 'INVD' error code should appear
     */
    @Test
    public static void shouldFailDay33() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=98123323122");
        ArrayList<String> errorCode = response.path("errors.errorCode");
        Assert.assertEquals(errorCode.get(0), "INVD", "The error code does not match");
        Assert.assertEquals(errorCode.size(),1,"there are more than one error on the error list");
    }

    /**
     * Year: 1998
     * Month: 12
     * Day: 33
     * 'Invalid day.' error message should appear
     */
    @Test
    public static void shouldFailDay33Msg() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=98123323122");
        ArrayList<String> errorMessage = response.path("errors.errorMessage");
        Assert.assertEquals(errorMessage.get(0), "Invalid day.", "The error message does not match");
        Assert.assertEquals(errorMessage.size(),1,"there are more than one error on the error list");

    }

    /**
     * Year: 2017
     * Month: 02
     * Day: 29
     * 2017 is not a leap year, there is no 29 of February in this year,
     * the INVD error should be displayed
     */
    @Test
    public static void shouldFailItIsNotLeapYear() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=17222900014");
        ArrayList<String> errorCode = response.path("errors.errorCode");

        Assert.assertEquals(errorCode.get(0), "INVD", "The error code does not match");
    }

    /**
     * Year: 2017
     * Month: 02
     * Day: 29
     * 2017 is not a leap year, there is no 29 of February in this year,
     * the  error should be displayed
     */
    @Test
    public static void shouldFailItIsNotLeapYearMsg() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=17222900014");
        ArrayList<String> errorMsg = response.path("errors.errorMessage");
        Assert.assertEquals(errorMsg.get(0), "Invalid day.", "The error messages do not match");
        Assert.assertEquals(errorMsg.size(),1,"there are more than one error on the error list");

    }

    /**
     * Given the incorrect last digit,
     * 1 error code is returned:
     * 'INVC'
     */
    @Test
    public static void shouldReturnINVC() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=80013131572");
        ArrayList<String> errorCode = response.path("errors.errorCode");
        Assert.assertEquals(errorCode.get(0), "INVC", "The error code does not match");
        Assert.assertEquals(errorCode.size(),1,"there are more than one error on the error list");

    }

    /**
     * Given the incorrect last digit, the
     * 1 error message is displayed:
     * 'Check sum is invalid. Check last digit.'
     */
    @Test
    public static void shouldReturnINVCMsg() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=80013131572");
        ArrayList<String> errorMessage = response.path("errors.errorMessage");
        Assert.assertEquals(errorMessage.get(0), "Check sum is invalid. Check last digit.", "The error message does not match");
        Assert.assertEquals(errorMessage.size(),1,"there are more than one error on the error list");

    }
}