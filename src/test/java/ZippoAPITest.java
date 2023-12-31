import POJOClasses.Location;
import POJOClasses.User;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.codehaus.groovy.control.messages.Message;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class ZippoAPITest {

    @Test
    void test1() {
        given()  // preparation(token, request body, parameters, cookies...)

                .when() //for url, request method(get, post, put, patch, delete)
                //get, post, put, patch, delete don't belong to postman.
                // they are known as http methods. All programming languages use these methods

                .then(); // response(response body, tests, extract data from the response...)

    }

    @Test
    void statusCodeTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210") // set up request method and url

                .then()
                .log().body() // prints response body to the console
                .log().status() // prints status code
                .statusCode(200); // testing the status code

    }

    @Test
    void contentTypeTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .contentType(ContentType.JSON); // tests if the response is in JSON format

    }

    @Test
    void countryInformationTest() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("country", equalTo("United States")); // test if the country value is US
    }                                                                  // we used hamcrest methods to write tests

    @Test
    void stateInformationTest() {

        // send a request to "http://api.zippopotam.us/us/90210"
        //and check if the state is "California"

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places[0].state", equalTo("California"));

    }
// Send a request to "http://api.zippopotam.us/us/90210"
    // and check if the state abbreviation is "CA"

    @Test
    void stateAbbreviationTest() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places[0].'state abbreviation'", equalTo("CA"));

    }

    // Postman                               Rest Assured
    //pm.response.json()                     body()
    //pm.response.json().country             body("country")
    //pm.response.json().places[0].state     body("places[0].state")


// Send a request to "http://api.zippopotam.us/tr/01000"
    // and check if the body has "Büyükdikili Köyü"

    @Test
    void bodyHasItemTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .body("places.'place name'", hasItem("Büyükdikili Köyü"));

        // When we don't use index it gets all place names from the response and creates an array with them.
        // hasItem checks if that array contains "Büyükdikili Köyü" value in it

    }

    @Test
    void arrayHasSizeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .body("places.'place name'", hasSize(71));
    }

    @Test
    void arrayHasSizeTest2() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places.'place name'", hasSize(1));
    }

    @Test
    void multipleTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .statusCode(200)
                .body("places", hasSize(71))
                .body("places.'place name'", hasItem("Büyükdikili Köyü"))
                .body("country", equalTo("Turkey"))
                .body("places.state", hasItem("Adana"));
    }
    // parameters
    // There are 2 types of parameters:
    // 1) Path Parameters -> http://api.zippopotam.us/us/90210  (/us/90210 is path parameter)
    // 2) Query Parameters -> https://gorest.co.in/public/v1/users?page=3&city=Buffalo (until ? its url, after ? its parameter)


    @Test
    void pathParametersTest1() {
        String countryCode = "us";
        String zipCode = "90210";

        given()
                .pathParam("countryCode", countryCode)
                .pathParam("zipCode", zipCode)
                .log().uri() // prints the requested url
                .when()
                .get("http://api.zippopotam.us/{countryCode}/{zipCode}")
                .then()
                .log().body()
                .statusCode(200);
    }
    // send a get request for zipcodes between 90210 and 90213 and verify that in all responses the size
    // of the places array is 1

    @Test
    void zipCodeTest() {

        for (int i = 90210; i <= 90213; i++) {

            given()
                    .pathParam("countryCode", "us")
                    .pathParam("zipCode", i)
                    .log().uri() // prints the requested url
                    .when()
                    .get("http://api.zippopotam.us/{countryCode}/{zipCode}")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("places", hasSize(1))
                    .body("'post code'", equalTo(String.valueOf(i)));
        }

    }

    @Test
    void queryParametersTest1() {
        given()
                .param("page", 3)
                .pathParam("APIName", "users")
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/{APIName}")
                .then()
                .log().body()
                .statusCode(200);

    }
    // send the same request for the pages between 1-10 and check if
    // the page number we send from request and page number we get from response are the same

    @Test
    void queryParameterTest2() {
        for (int i = 1; i <= 10; i++) {


            given()
                    .param("page", i)
                    .pathParam("APIName", "users")
                    .log().uri()
                    .when()
                    .get("https://gorest.co.in/public/v1/{APIName}")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("meta.pagination.page", equalTo(i));

        }
    }

    // Write the same test with Data Provider

    @Test(dataProvider = "parameters")
    void queryParameterTestWithDataProvider(int pageNumber, String apiName) {

        given()
                .param("page", pageNumber)
                .pathParam("APIName", apiName)
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/{APIName}")
                .then()
                .log().body()
                .statusCode(200)
                .body("meta.pagination.page", equalTo(pageNumber));

    }

    @DataProvider
    public Object[][] parameters() {


        Object[][] parameters = {
                {1, "users"},
                {2, "users"},
                {3, "users"},
                {4, "users"},
                {5, "users"},
                {6, "users"},
                {7, "users"},
                {8, "users"},
                {9, "users"},
                {10, "users"}


        };
        return parameters;

    }

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void setUp() {
        baseURI = "https://gorest.co.in/public/v1";
        // if the request url in the request method doesn't have http part
        // rest assured puts baseURI to the beginning of the url in the request method

        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .log(LogDetail.BODY)
                .addPathParam("APIName", "users")
                .addParam("page", 3)
                .setContentType(ContentType.JSON)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.BODY)
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }

    @Test
    void baseURITest() {
        given()
                .param("page", 3)
                .pathParam("APIName", "users")
                .log().uri()
                .when()
                .get("/{APIName}")
                .then()
                .log().body()
                .statusCode(200)
                .body("meta.pagination.page", equalTo(3));
    }

    @Test
    void requestAndResponseSpecTest() {

        given()

                .spec(requestSpecification)
                .when()
                .get("/{APIName}")
                .then()
                .spec(responseSpecification)
                .body("meta.pagination.page", equalTo(3));

    }

    @Test
    void extractStringTest() {

        String placeName = given()
                .pathParam("countryCode", "us")
                .pathParam("zipCode", "90210")
                .when()
                .get("http://api.zippopotam.us/{countryCode}/{zipCode}")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().path("places[0].'place name'");

        System.out.println("place name: " + placeName);

    }

    @Test
    void extractIntValue() {
        int page = given()
                .spec(requestSpecification)
                .when()
                .get("/{APIName}")
                .then()
                .spec(responseSpecification)
                .body("meta.pagination.page", equalTo(3))
                .extract().path("meta.pagination.page");
        // we re not allowed to assign an int to a string(cannot assign a type to another type)

        System.out.println("page = " + page);

    }

    @Test
    void extractListTest() {
        List<Integer> listOfIds = given()
                .spec(requestSpecification)
                .when()
                .get("/{APIName}")
                .then()
                .spec(responseSpecification)
                .body("data.id", hasSize(10))
                .extract().path("data.id");
        System.out.println("listOfIds.size() = " + listOfIds);
        System.out.println("listOfIds.get(3) = " + listOfIds);
        System.out.println("listOfIds.contains(5507746) = " + listOfIds.contains(5507746));

        Assert.assertTrue(listOfIds.contains(5507746));
    }

    @Test
    void extractListTest2() {
        List<String> listOfNames = given()
                .spec(requestSpecification)
                .when()
                .get("/{APIName}")
                .then()
                .spec(responseSpecification)
                .body("data.name", hasSize(10))
                .extract().path("data.name");

        for (String name : listOfNames) {
            System.out.println("name = " + name);
        }

    }

    @Test
    void extractResponse() {
        Response response = given()
                .spec(requestSpecification)
                .when()
                .get("/{APIName}")
                .then()
                .spec(responseSpecification)
                .extract().response();

        int page = response.path("meta.pagination.page");
        System.out.println("page = " + page);

        String nextUrl = response.path("meta.pagination.links.next");
        System.out.println("nextUrl = " + nextUrl);

        String name = response.path("data[1].name");
        System.out.println("name = " + name);

        List<String> nameList = response.path("data.name");
        System.out.println("nameList = " + nameList);

        // returns the entire response and assigns it to a Response object.
        // By using this object we are able to reach any part of the response

        // extract.path vs extract.response
        // extract.path() can only give us one part of the response. If you need different values from different parts of the response (names and page)
        // you need to write two different request.
        // extract.response() gives us the entire response as an object so if you need different values from different parts of the response (names and page)
        // you can get them with only one request
    }

    // POJO (Plain Old Java Object)

    @Test
    void extractJsonPOJO() {

      Location location =  given()
                .pathParam("countryCode", "us")
                .pathParam("zipCode", "90210")
                .when()
                .get("http://api.zippopotam.us/{countryCode}/{zipCode}")
                .then()
                .log().body()
                .extract().as(Location.class);

        // This request extracts the entire response and assigns it to Location class as a Location object
        // We cannot extract the body partially (e.g. cannot extract place object separately)

        System.out.println("location = " + location.getPostCode());
        System.out.println("location = " + location.getCountry());
        System.out.println("location.getCountryAbbreviation() = " + location.getCountryAbbreviation());
        System.out.println("location.getPlaces().get(0) = " + location.getPlaces().get(0));
        System.out.println("location.getPlaces().get(0).getPlaceName = " + location.getPlaces().get(0).getPlaceName());
        System.out.println("location.getPlaces().get(0).getState = " + location.getPlaces().get(0).getState());

    }

//    @Test
//    void extractWithJsonPath1(){
//        given()
//                .spec(requestSpecification)
//                .when()
//                .get("/{APIName}")
//                .then()
//                .spec(responseSpecification)
//                .extract().jsonPath().getObject("data[0]", User.class);
//
//        System.out.println("user.getId() = " + user.getId());
//        System.out.println("user.getName() = " + user.getName());
//        System.out.println("user.getEmail() = " + user.getEmail());
//    }

    // extract.path() ==> we can extract only one value or list of that values
    //                 String name = extract.path(data[0].name)
    //                 List<String> nameList extract.path(data.name)
    //
    // extract.as() ==> We can extract the entire response body. It doesn't let us to extract one part of the body separately.
    //                  So we need to create classes for the entire body.
    //                  extract.as(Location.class)
    //                  extract.as(Place.class) cannot extract like this
    //                  extract.as(User.class) cannot extract like this
    //
    // extract.jsonPath() ==> We can extract the entire body as well as any part of the body. So if we need only one part of the
    //                        body we don't need to create classes for the entire body
    //                        extract.jsonPath().getObject(Location.class)
    //                        extract.jsonPath().getObject(Place.class)
    //                        extract.jsonPath().getObject(User.class)

    @Test
    void extractWithJsonPath2() {

        List<User> usersList = given()
                .spec(requestSpecification)
                .when()
                .get("/{APIName}")
                .then()
                .spec(responseSpecification)
                .extract().jsonPath().getList("data", User.class);

        System.out.println("usersList.size() = " + usersList.size());
        System.out.println("usersList.get(0).getName() = " + usersList.get(0).getName());
        System.out.println("usersList.get(5).getId() = " + usersList.get(5).getId());
    }

    @Test
    void extractWithJsonPath3() {

        Response response = given()
                .spec(requestSpecification)
                .when()
                .get("/{APIName}")
                .then()
                .spec(responseSpecification)
                .extract().response();

        System.out.println("response.jsonPath().getInt(\"meta.pagination.page\") = " + response.jsonPath().getInt("meta.pagination.page"));
        System.out.println("response.jsonPath().getString(\"data[2].name\") = " + response.jsonPath().getString("data[2].name"));

        List<User> userList = response.jsonPath().getList("data", User.class);
        System.out.println("userList.size() = " + userList.size());
        System.out.println("userList.get(6).getName() = " + userList.get(6).getName());
    }
}

