import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;

public class PeselValidatorResponseBodyTests {

    /**
    Given correct pesel format, check if response 'pesel' field equals
    the one given in request url
     */
    @Test
    public static void shouldBeIdentical(){
        String adress = "https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=97022153388";
        String givenPesel =adress.split("=")[1];
        Response response=get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=97022153388");
        String pesel = response.path("pesel");
        Assert.assertEquals(pesel,givenPesel,"pesel in response body does not equals the one given in request");
    }

    /**
    Given correct pesel format, check if response 'isValid' field equals true
     */
    @Test
    public static void shouldBeTrue(){
        Response response=get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=97022153388");
        boolean isValid  = response.path("isValid");
        Assert.assertEquals(isValid, true, "Pesel format is not valid(should be)");
    }

    /**
    Given wrong pesel format, check if response 'isValid' field equals false
     */
    @Test
    public static void shouldBeFalse(){
        Response response=get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=9702215d3388");
        boolean isValid  = response.path("isValid");
        Assert.assertEquals(isValid, false, "Pesel format is valid (should not be)");
    }
    @DataProvider
    public static Object[][] maleOrFemalePesel(){
        return new Object[][]{
                {"97022137812","Male"},   //gender digit: 1
                {"97022137836","Male"},   //gender digit: 3
                {"97022137850","Male"},   //gender digit: 5
                {"97022137874","Male"},   //gender digit: 7
                {"97022137898","Male"},   //gender digit: 9

                {"97022153302","Female"}, //gender digit: 0
                {"97022153326","Female"}, //gender digit: 2
                {"97022153340","Female"}, //gender digit: 4
                {"97022153364","Female"}, //gender digit: 6
                {"97022153388","Female"}  //gender digit: 8
        };
    }

    /**
    Given the male or female pesel the response should be adequate
     */
    @Test(dataProvider = "maleOrFemalePesel")
    public static void shouldRecognizeGender(String pesel, String expGender){
        String url = "https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=%s";

        Response response=get(String.format(url,pesel));
        String actGender = response.path("gender");
        Assert.assertEquals(actGender, expGender, "Pesel gender is not valid in case of:"+pesel+" pesel");
    }

    @DataProvider
    public static Object[][] correctDates(){
        return new Object[][]{
                //1998 is not a leap year
                {"98013116872", "1998-01-31"},
                {"98022889471", "1998-02-28"},
                {"98033170135", "1998-03-31"},
                {"98043033956", "1998-04-30"},
                {"98053169812", "1998-05-31"},
                {"98063087377", "1998-06-30"},
                {"98073168172", "1998-07-31"},
                {"98083100399", "1998-08-31"},
                {"98093053438", "1998-09-30"},
                {"98103136771", "1998-10-31"},
                {"98113082639", "1998-11-30"},
                {"98123159932", "1998-12-31"}
        };
    }
    @Test(dataProvider = "correctDates")
    public static void shouldRecognizeBoundaryDays(String pesel,String expDate){
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel="+pesel);
        String outputDate = response.path("dateOfBirth");
        outputDate = outputDate.split("T")[0];
        Assert.assertEquals(outputDate, expDate, "Displayed Date of Birth is not correct");
    }

    /**
     Corner case: person born in 1800year
     Given the month number in range 80 - 92 the app should find out that the person's
     birth date is in range 1800 - 1899 year
     Year: 1800
     Month: 01
     Day: 30
     pesel: 00813000019
     */
    @Test
    public static void shouldRecognizeSbdyBornIn1800() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=00813000019");
        String outputDate = response.path("dateOfBirth");
        outputDate = outputDate.split("T")[0];
        System.out.println(outputDate);
        String expectedDate = "1800-01-30";
        Assert.assertEquals(outputDate, expectedDate, "Displayed Date of Birth is not correct");
    }

    /**
     Corner case: person born in 1900year
     Month number in range 01-12
     Year: 1900
     Month: 01
     Day: 30
     pesel: 00013020813
     */
    @Test
    public static void shouldRecognizeSbdyBornIn1900(){
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=00013020813");
        String outputDate = response.path("dateOfBirth");
        outputDate = outputDate.split("T")[0];
        System.out.println(outputDate);
        String expectedDate = "1900-01-30";
        Assert.assertEquals(outputDate, expectedDate, "Displayed Date of Birth is not correct");
    }
    /**
     Corner case: person born in 2000year
     Month number in range 21-32
     Year: 2000
     Month: 01
     Day: 30
     pesel: 00213073091
     */
    @Test
    public static void shouldRecognizeSbdyBornIn2000(){
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=00213073091");
        String outputDate = response.path("dateOfBirth");
        outputDate = outputDate.split("T")[0];
        System.out.println(outputDate);
        String expectedDate = "2000-01-30";
        Assert.assertEquals(outputDate, expectedDate, "Displayed Date of Birth is not correct");
    }

    /**
     Corner case: person born in 2100year
     Month number in range 41-52
     Year: 2100
     Month: 01
     Day: 30
     pesel: 004130000017
     */
    @Test
    public static void shouldRecognizeSbdyBornIn2100(){
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=00413000017");
        String outputDate = response.path("dateOfBirth");
        outputDate = outputDate.split("T")[0];
        System.out.println(outputDate);
        String expectedDate = "2100-01-30";
        Assert.assertEquals(outputDate, expectedDate, "Displayed Date of Birth is not correct");
    }

    /**
     Corner case: person born in 2200year
     Month number in range 61-72
     Year: 2200
     Month: 01
     Day: 30
     pesel: 00613000013
     */
    @Test
    public static void shouldRecognizeSbdyBornIn2200(){
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=00613000013");
        String outputDate = response.path("dateOfBirth");
        outputDate = outputDate.split("T")[0];
        System.out.println(outputDate);
        String expectedDate = "2200-01-30";
        Assert.assertEquals(outputDate, expectedDate, "Displayed Date of Birth is not correct");
    }

    /**
     Just to be sure, the person born in 2299year
     Month number in range 61-72
     Year: 2299
     Month: 01
     Day: 30
     pesel: 99613000017
     */
    @Test
    public static void shouldRecognizeSbdyBornIn2299(){

        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=99613000017");
        String outputDate = response.path("dateOfBirth");
        outputDate = outputDate.split("T")[0];
        System.out.println(outputDate);
        String expectedDate = "2299-01-30";
        Assert.assertEquals(outputDate, expectedDate, "Displayed Date of Birth is not correct");
    }
}