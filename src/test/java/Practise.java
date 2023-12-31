import POJOClasses.Location;
import POJOClasses.ToDo;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Practise {

/**
 * Task 1
 * create a request to https://jsonplaceholder.typicode.com/todos/2
 * expect status 200
 * Convert Into POJO
 */

    @Test
    void task1(){

    ToDo toDo = given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
                .extract().as(ToDo.class);

        System.out.println("todo = " + toDo);

    }
    /**
     * Task 2
     * create a get request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect title in response body to be "quis ut nam facilis et officia qui"
     */

    @Test
    void task2(){

//        given()
//
//                .when()
//                .get("https://jsonplaceholder.typicode.com/todos/2")
//                .then()
//                .statusCode(200)
//                .contentType(ContentType.JSON)
//                .body("title", equalTo("quis ut nam facilis et officia qui"));

        String title = given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().path("title");

                Assert.assertEquals(title,"quis ut nam facilis et officia qui");

    }
/**
 * Task 3
 * create a get request to https://jsonplaceholder.typicode.com/todos/2
 * expect status 200
 * expect content type JSON
 * expect response completed status to be false
 */

    @Test
    void test3(){
//        given()
//
//                .when()
//                .get("https://jsonplaceholder.typicode.com/todos/2")
//                .then()
//                .statusCode(200)
//                .contentType(ContentType.JSON)
//                .body("completed", equalTo(false));

        Boolean completed  = given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().path("completed");

        Assert.assertFalse(completed);

    }

}
