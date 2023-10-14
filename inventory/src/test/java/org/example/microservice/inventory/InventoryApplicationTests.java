package org.example.microservice.inventory;


import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;


class InventoryComponentTests {


	@Test
	void testDefaultSettings(){
		given()
						.headers()
						.body()
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.post()
	}


}
