package br.integrationtests.controller.yaml;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.BeforeAll;
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
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class AuthYamlTest extends AbsIntTest{

    private static YMLMapper objectMapper;
    private static TokenVO tokenVO;

    @BeforeAll
    public static void setup(){
        objectMapper = new YMLMapper();
    }

    @Test
    @Order(0)
    public void testSignin() throws JsonMappingException, JsonProcessingException {
	AccCrenditalsVO user = new AccCrenditalsVO("admin", "admin123");

    tokenVO = given()
                        .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                        .accept(TestConfigs.CONTENT_TYPE_YML)
                        .basePath("/auth/signin")
                            .port(TestConfigs.SERVER_PORT)
                            .contentType(TestConfigs.CONTENT_TYPE_YML)
                        .body(user, objectMapper)
                            .when()
                        .post()
                        .then()
                            .statusCode(200)
                                .extract()
                                .body()
                                    .as(TokenVO.class, objectMapper);

        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());
	}

    @Test
    @Order(1)
    public void testRefresh() throws JsonMappingException, JsonProcessingException {
        var newtokenVO = given()
                    .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                    .accept(TestConfigs.CONTENT_TYPE_YML)
                    .basePath("/auth/refresh")
                        .port(TestConfigs.SERVER_PORT)
                        .contentType(TestConfigs.CONTENT_TYPE_YML)
                            .pathParam("username", tokenVO.getUsername())
                            .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
                    .when()
                        .put("{username}")
                    .then()
                        .statusCode(200)
                    .extract()
                        .body()
                            .as(tokenVO.getClass(), objectMapper);

        assertNotNull(newtokenVO.getAccessToken());
        assertNotNull(newtokenVO.getRefreshToken());
	}
}