import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.get;

public class PeselValidatorErrorTests {

    /**
     * Given too short pesel the response error code should be 'INVL' (InValidLength)
     * Expected: True
     */
    @Test
    public static void invalidLengthErrCodeTest() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=123");
        ArrayList<String> error = response.path("errors.errorCode");
        Assert.assertEquals(error.get(0), "INVL", "The error code does not match ");
    }

    /**
     * Given too short pesel the response error message should be
     * 'Invalid length. Pesel should have exactly 11 digits.
     * Expected: True
     */
    @Test
    public static void invalidLengthErrMsgTest() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=123");
        ArrayList<String> errorMessage = response.path("errors.errorMessage");
        Assert.assertEquals(errorMessage.get(0), "Invalid length. Pesel should have exactly 11 digits.", "The error messages does not match ");
    }

    /**
     * Given wrong pesel (containing 11 letters)
     * the response error code should be 'NBRQ'
     * Expected: True
     */
    @Test
    public static void invalidCharactersErrCodeTest() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=jedenaściel");
        ArrayList<String> error = response.path("errors.errorCode");
        Assert.assertEquals(error.get(0), "NBRQ", "The error codes does not match ");
    }

    /**
     * Given wrong pesel (containing 10 digits and one special char )
     * the only error code should be 'NBRQ'
     * the only error message should be 'Invalid characters. Pesel should be a number.'
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
    public static void specialCharsInPeselErrorTest(String specialChar) {
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
    public static void lettersInPeselErrorTest(String letter) {
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
    public static void invalidMonth_0_ErrCodeTest() {
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
    public static void invalidMonth_0_errMsgTest() {
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
    public static void invalidMonth_13_ErrCodeTest() {
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
    public static void invalidMonth_13_ErrMsgTest() {
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
    public static void invalidDay_0_errCodeTest(){
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
    public static void invalidDay_0_ErrMsgTest() {
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
    public static void invalidDay_33_ErrCodeTest() {
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
    public static void invalidDay_33_ErrMsgTest() {
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
    public static void inValidLeapYearErrCodeTest() {
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
    public static void inValidLeapYearErrMsgTest() {
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
    public static void invalidCheckSumErrCodeTest() {
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
    public static void invalidCheckSumErrMsgTest() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=80013131572");
        ArrayList<String> errorMessage = response.path("errors.errorMessage");
        Assert.assertEquals(errorMessage.get(0), "Check sum is invalid. Check last digit.", "The error message does not match");
        Assert.assertEquals(errorMessage.size(),1,"there are more than one error on the error list");

    }
}