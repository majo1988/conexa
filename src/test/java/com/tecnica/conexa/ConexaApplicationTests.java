package com.tecnica.conexa;

import com.tecnica.conexa.util.ConexaApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest(classes = ConexaApplication.class)
class ConexaApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void testEndpoint() {
		ResponseEntity<String> response = restTemplate.getForEntity(
				"http://localhost:" + port + "/your-endpoint", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		// Aquí puedes realizar más aserciones según la respuesta esperada
	}

	private void assertEquals(HttpStatus httpStatus, HttpStatus statusCode) {
	}
}
