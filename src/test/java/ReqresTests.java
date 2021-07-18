import com.github.javafaker.Faker;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ReqresTests {

    Faker faker = new Faker();

    String firstName = faker.name().firstName(),
            job = faker.job().title(),
            newFirstName = faker.name().firstName(),
            newJob = faker.job().title();

    @Test
    void checkFirstUserEmail() {
        get("https://reqres.in/api/users?page=1")
                .then()
                .statusCode(200)
                .body("data.email[0]", is("george.bluth@reqres.in"));
    }

    @Test
    void userNotFound() {
        get("https://reqres.in/api/users/404")
                .then()
                .statusCode(404);
    }

    @Test
    void createUser() {
        given()
                .contentType(JSON)
                .body("{\"name\": " + "\"" + firstName + "\"" + "," +
                        "\"job\": " + "\"" + job + "\"}")
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .body("name", is(firstName))
                .body("job", is(job));
    }

    @Test
    void deleteUser() {
        String response =
                given()
                        .contentType(JSON)
                        .body("{\"name\": " + "\"" + firstName + "\"" + "," +
                                "\"job\": " + "\"" + job + "\"}")
                        .when()
                        .post("https://reqres.in/api/users")
                        .then()
                        .statusCode(201)
                        .extract().path("id");

        delete("https://reqres.in/api/users/" + response)
                .then()
                .statusCode(204);
    }

    @Test
    void updateUser() {
        String response =
                given()
                        .contentType(JSON)
                        .body("{\"name\": " + "\"" + firstName + "\"" + "," +
                                "\"job\": " + "\"" + job + "\"}")
                        .when()
                        .post("https://reqres.in/api/users")
                        .then()
                        .statusCode(201)
                        .extract().path("id");

        given()
                .contentType(JSON)
                .body("{\"name\": " + "\"" + newFirstName + "\"" + "," +
                        "\"job\": " + "\"" + newJob + "\"}")
                .when()
                .put("https://reqres.in/api/users" + response)
                .then()
                .statusCode(200)
                .body("name", is(newFirstName))
                .body("job", is(newJob));
    }

}
