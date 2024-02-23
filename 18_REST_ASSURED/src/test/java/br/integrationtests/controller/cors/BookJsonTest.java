package br.integrationtests.controller.cors;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.configs.TestConfigs;
import br.intTest.testContainers.AbsIntTest;
import br.integrationtests.vo.AccCrenditalsVO;
import br.integrationtests.vo.BookVO;
import br.integrationtests.vo.TokenVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class BookJsonTest extends AbsIntTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	
	private static BookVO book;

	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		book = new BookVO();
	}

	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		AccCrenditalsVO user = new AccCrenditalsVO("admin", "admin123");

		var accessToken = given()
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
							.as(TokenVO.class)
						.getAccessToken();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
        
        mockBook();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(book)
                    .when()
                    .post()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .asString();
        
        book = objectMapper.readValue(content, BookVO.class);
        
        assertNotNull(book.getId());
        assertNotNull(book.getTitle());
        assertNotNull(book.getAuthor());
        assertNotNull(book.getPrice());
        assertTrue(book.getId() > 0);
        assertEquals("TestTitle", book.getTitle());
        assertEquals("TestAuthor", book.getAuthor());
        assertEquals(12.00, book.getPrice());
    }

	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		book.setAuthor("TestUpdateAuthor");

		var content = 
			given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.body(book)
					.when()
					.put()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();

		BookVO persistedbook = objectMapper.readValue(content, BookVO.class);
		book = persistedbook;
		
        assertNotNull(book.getId());
        assertNotNull(book.getTitle());
        assertNotNull(book.getAuthor());
        assertNotNull(book.getPrice());

        assertTrue(book.getId() > 0);

        assertEquals("TestTitle", book.getTitle());
        assertEquals("TestUpdateAuthor", book.getAuthor());
        assertEquals(12.00, book.getPrice());
	}

	@Test
	@Order(3)
    public void testFindById() throws JsonMappingException, JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .pathParam("id", book.getId())
                    .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .asString();
        
        BookVO persistedbook = objectMapper.readValue(content, BookVO.class);
        
        assertNotNull(persistedbook.getId());
        assertNotNull(persistedbook.getTitle());
        assertNotNull(persistedbook.getAuthor());
        assertNotNull(persistedbook.getPrice());
        assertEquals(persistedbook.getId(), book.getId());
        assertEquals("TestTitle", persistedbook.getTitle());
        assertEquals("TestUpdateAuthor", persistedbook.getAuthor());
        assertEquals(12.00, persistedbook.getPrice());
    }

	@Test
	@Order(4)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
		given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.pathParam("id", book.getId())
					.when()
					.delete("{id}")
				.then()
					.statusCode(204);
	}

	@Test
	@Order(5)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {
		mockBook();

		var content = 
			given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.when()
					.get()
				.then()
					.statusCode(200)
						.extract()
						.body()
						.asString();

		List<BookVO> people = objectMapper.readValue(content, new TypeReference<List<BookVO>>() {});

		BookVO foundbookOne = people.get(0);
		
		assertNotNull(foundbookOne.getId());
		assertNotNull(foundbookOne.getTitle());
		assertNotNull(foundbookOne.getAuthor());
		assertNotNull(foundbookOne.getPrice());

		assertTrue(foundbookOne.getId() > 0);

		assertEquals("Working effectively with legacy code", foundbookOne.getTitle());
		assertEquals("Michael C. Feathers", foundbookOne.getAuthor());
		assertEquals(49.00, foundbookOne.getPrice());

		BookVO foundbookSix = people.get(4);
		
		assertNotNull(foundbookSix.getId());
		assertNotNull(foundbookSix.getTitle());
		assertNotNull(foundbookSix.getAuthor());
		assertNotNull(foundbookSix.getPrice());

		assertTrue(foundbookOne.getId() > 0);

		assertEquals("Code complete", foundbookSix.getTitle());
		assertEquals("Steve McConnell", foundbookSix.getAuthor());
		assertEquals(58.0, foundbookSix.getPrice());
	}

	@Test
	@Order(6)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {
		mockBook();

		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
		.setBasePath("/api/book/v1")
		.setPort(TestConfigs.SERVER_PORT)
			.addFilter(new RequestLoggingFilter(LogDetail.ALL))
			.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
		.build();

		given().spec(specificationWithoutToken)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.when()
				.get()
			.then()
				.statusCode(403);
	}

	private void mockBook() {
        book.setTitle("TestTitle");
        book.setAuthor("TestAuthor");
        book.setPrice(Double.valueOf(12.00));
        book.setLaunchDate(new Date());
	}
}