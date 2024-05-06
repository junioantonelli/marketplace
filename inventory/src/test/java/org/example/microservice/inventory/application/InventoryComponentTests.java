package org.example.microservice.inventory.application;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InventoryComponentTests {
	@LocalServerPort
	int port;

	@BeforeEach
	void setup() {
		RestAssured.port = port;
		String proxyHost = System.getProperty("http.proxyHost");
		String proxyPort = System.getProperty("http.proxyPort");
		if (Objects.nonNull(proxyHost) && Objects.nonNull(proxyPort)) {
			RestAssured.proxy(proxyHost, Integer.parseInt(proxyPort));
		}
	}

	@Test
	@Order(1)
	void shouldIngestDataSuccessfully(){
				given()
						.body("""
                    {
                        "serialNumber": "123456789",
                        "pointOfContact": "abcd",
                        "distributionCenter": "abc123",
                        "quantity": 100
                    }
                    """)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.post("/v1/inventory")
            .then()
            .statusCode(200)
            .and()
            .body("[0].sn", is("123456789"))
						.and()
						.body("[0].qtt", Matchers.greaterThanOrEqualTo(9));
	}

	@Test
	@Order(2)
	void shouldRetrieveDataSuccessfully(){
		given()
						.params(Map.of( "sn","123456789",
														"poc", "abcd",
														"from", "2023-10-18"))
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.get("/v1/inventory")
            .then()
            .statusCode(200)
            .and()
            .body("[0].poc", is("abcd"))
						.and()
						.body("[0].qtt", Matchers.greaterThanOrEqualTo(10));
	}

	@Test
	@Order(3)
	void shouldModifyDataSuccessfully(){
		given()
						.body("""
                    {
                        "serialNumber": "123456789",
                        "quantity": 6
                    }
                    """)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.put("/v1/inventory")
			.then()
            .statusCode(200)
            .and()
            .body("[0].poc", is("abcd"))
						.and()
						.body("[0].qtt", Matchers.lessThanOrEqualTo(9));
	}

	@Test
	@Order(4)
	void shouldDeleteDataSuccessfully(){
		given()
						.params(Map.of( "sn","123456789"))
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.delete("/v1/inventory")
            .then()
            .statusCode(200)
            .and()
            .body("[0].poc", is("abcd"))
						.and()
						.body("[0].qtt", Matchers.lessThanOrEqualTo(6));
	}


}
