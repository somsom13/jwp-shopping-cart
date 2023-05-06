package cart.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.controller.dto.ProductRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Sql("/customer_initialize.sql")
class AdminControllerIntegrationTest {

    @LocalServerPort
    int port;

    private final String email = "baron@gmail.com";
    private final String password = "password";

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("전체 상품 조회 API 호출 시 전체 상품이 반환된다.")
    @Test
    void showAllProducts() {
        given().log().all()
                .when()
                .auth().preemptive().basic(email, password)
                .get("/admin")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("상품 등록 API 호출 시 상품을 등록한다.")
    @Test
    void registerProduct() {
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(email, password)
                .body(new ProductRequest("https://avatars.githubusercontent.com/u/95729738?v=4",
                        "CuteSeonghaDollFromController",
                        25000))
                .when()
                .post("/admin/product")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("상품 정보 수정 API 호출 시 상품 정보가 수정된다.")
    @Test
    void modifyProduct() {
        String baseUrl = "/admin/product/";
        //given
        String redirectURI = given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(email, password)
                .body(new ProductRequest("https://avatars.githubusercontent.com/u/95729738?v=4",
                        "CuteSeonghaDoll", 25000))
                .when()
                .post(baseUrl)
                .then()
                .extract().response().getHeader("Location");
        long savedId = Long.parseLong(redirectURI.replace(baseUrl, ""));

        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(email, password)
                .body(new ProductRequest("https://avatars.githubusercontent.com/u/70891072?v=4",
                        "CuteBaronDollFromController", 2500))
                .when()
                .put("/admin/product/" + savedId)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("상품 삭제 API 호출 시 상품이 삭제된다.")
    @Test
    void deleteProduct() {
        String baseUrl = "/admin/product/";
        //given
        String redirectURI = given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(email, password)
                .body(new ProductRequest("https://avatars.githubusercontent.com/u/95729738?v=4",
                        "CuteSeonghaDoll", 25000))
                .when()
                .post(baseUrl)
                .then()
                .extract().response().getHeader("Location");
        long savedId = Long.parseLong(redirectURI.replace(baseUrl, ""));

        given().log().all()
                .auth().preemptive().basic(email, password)
                .when()
                .delete("/admin/product/" + savedId)
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("가격이 0이하의 값이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void exceptionWhenPriceNotPositive(int price) {
        // when
        Response response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(email, password)
                .body(new ProductRequest("https://avatars.githubusercontent.com/u/95729738?v=4", "CuteSeonghaDoll",
                        price))
                .when()
                .post("/admin/product")
                .then()
                .log().all()
                .extract().response();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.getBody().asString()).isEqualTo("가격은 0보다 커야합니다.")
        );
    }

    @DisplayName("이름이 50글자보다 크면 예외가 발생한다.")
    @Test
    void exceptionWhenNameWrongLength() {
        // when
        String name = "dskjgfdsvesvurevhjdsbvehsbvhjesbvhjesbvfhvsdhvhdsvhfdshv";
        Response response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(email, password)
                .body(new ProductRequest("https://avatars.githubusercontent.com/u/95729738?v=4", name,
                        10000))
                .when()
                .post("/admin/product")
                .then()
                .log().all()
                .extract().response();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.getBody().asString()).isEqualTo("상품명은 1글자 이상, 50글자 이하여야합니다.")
        );
    }

    @DisplayName("이미지 URL이 없으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void exceptionWhenBlankImgUrl(String imgUrl) {
        // when
        Response response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(email, password)
                .body(new ProductRequest(imgUrl, "cuteSeongHa", 10000))
                .when()
                .post("/admin/product")
                .then()
                .log().all()
                .extract().response();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.getBody().asString()).isEqualTo("이미지 URL은 필수입니다.")
        );
    }

    @DisplayName("이름이 입력되지 않으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void exceptionWhenBlankName(String name) {
        // when
        Response response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(email, password)
                .body(new ProductRequest("tmpImg", name, 2000))
                .when()
                .post("/admin/product")
                .then()
                .log().all()
                .extract().response();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.getBody().asString()).isEqualTo("상품명은 필수입니다.")
        );
    }
}
