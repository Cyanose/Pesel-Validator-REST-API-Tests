# PeselValidatorApiTests

Automated tests to PeselValidator REST API endpoint (https://peselvalidatorapitest.azurewebsites.net/swagger/index.html) written in Java, using rest-assured and TestNG libraries.

ResponseCodeTests is a test suite that contains http status code tests:

* TC1: status code 200 in response to proper request
* TC2: status code 200 in response to request with invalid pesel (fine according to docs)
* TC3: status code 400 in response to request with missing pesel

ErrorTests is a test suite that tests error codes and error messages that are being returned in response to invalid reqeuest.

* Tc1: Too short pesel - error code
* Tc2: Too short pesel - error message
* Tc3: Too long pesel - error code
* Tc4: Too long pesel - error message
* Tc5: invalid characters: only letters - error code
* Tc6: invalid characters: only letters - error message
* Tc7: invalid characters: mix of digits and special characters - error code & error message
* Tc8: invalid characters: mix of digits and letters - error code & error message
* Tc9: date: invalid month (00) - error code
* Tc10: date: invalid month (00) - error message
* Tc11: date: invalid month (13) - error code
* Tc12: date: invalid month (13) - error message
* Tc13: date: invalid day (00) - error code
* Tc14: date: invalid day (00) - error message
* Tc15: date: invalid day (33) - error code
* Tc16: date: invalid day (33) - error message
* Tc17: date: 29 Feb for year that is not leap - error code
* Tc18: date: 29 Feb for year that is not leap - error message
* Tc19: check sum: invalid check sum - error code
* Tc20: check sum: invalid check sum - error message

ResponseBodyTests is a test suite that tests response body returned as a result of valid request.

* Tc1: 'pesel' json field is identical to the one passed as an parameter in request
* Tc2: 'isValid' json field is true when request contains valid pesel 
* Tc3: 'isValid' json field is false when request contains invalid pesel
* Tc4: gender is properly recognized 
* Tc5: boundary value (last month) for each month of year should be properly recognized
* Tc6: Pesel of person born in 1800 year is properly recognized
* Tc7: Pesel of person born in 1900 year is properly recognized
* Tc8: Pesel of person born in 2000 year is properly recognized
* Tc9: Pesel of person born in 2100 year is properly recognized
* Tc10: Pesel of person born in 2200 year is properly recognized
* Tc11: Pesel of person born in 2299 year  is properly recognized
