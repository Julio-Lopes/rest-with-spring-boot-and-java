package br.integrationtests.controller.json;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.configs.TestConfigs;
import br.intTest.testContainers.AbsIntTest;
import br.integrationtests.vo.AccCrenditalsVO;
import br.integrationtests.vo.TokenVO;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class AuthJsonTest extends AbsIntTest{

    private static TokenVO tokenVO;

    @Test
    @Order(0)
    public void testSignin() throws JsonMappingException, JsonProcessingException {
	AccCrenditalsVO user = new AccCrenditalsVO("admin", "admin123");

    tokenVO = given()
                        .basePath("/auth/signin")
                            .port(TestConfigs.SERVER_PORT)
                            .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .body(user)
                            .when()
                        .post()
                        .then()
                            .statusCode(200)
                                .extract()
                                .body()
                                    .as(TokenVO.class);

        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());
	}

    @Test
    @Order(1)
    public void testRefresh() throws JsonMappingException, JsonProcessingException {
        var newtokenVO = given()
                    .basePath("/auth/refresh")
                        .port(TestConfigs.SERVER_PORT)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                            .pathParam("username", tokenVO.getUsername())
                            .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
                    .when()
                        .put("{username}")
                    .then()
                        .statusCode(200)
                    .extract()
                        .body()
                            .as(tokenVO.getClass());

        assertNotNull(newtokenVO.getAccessToken());
        assertNotNull(newtokenVO.getRefreshToken());
	}
}