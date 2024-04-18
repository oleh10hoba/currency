package com.example.currency;

import com.example.currency.domain.GridResult;
import com.example.currency.ui.CurrencyApplication;
import com.example.currency.ui.dto.request.AddCurrencyRequestDto;
import com.example.currency.ui.dto.response.CurrencyResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CurrencyApplication.class)
class ListCurrencyTest {
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value("${server.port}")
	String port;
	@Value("${server.address}")
	String address;
	boolean isActive = true;
	String ADD_CURRENCY_ENDPOINT = "/currency/add";
	String LIST_CURRENCY_ENDPOINT = "/currency/list";

	@AfterEach
	@BeforeEach
	void cleanTables() {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, "currency");
	}

	@Test
	@DisplayName("Should be empty list on project start")
	void emptyListTest() {
		try {
			ResponseEntity<GridResult> response = restTemplate.getForEntity(createURL(LIST_CURRENCY_ENDPOINT), GridResult.class);
			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertEquals(0, response.getBody().getTotalSize());
		} catch (HttpClientErrorException e) {
			fail(e.getMessage());
		}
	}


	@Test
	@DisplayName("Add few currencies should return same currencies")
	void listCurrencyTest() {
		try {
			AddCurrencyRequestDto dto = addCurrency("USD", "United States Dollar");
			ResponseEntity<String> response = restTemplate.postForEntity(createURL(ADD_CURRENCY_ENDPOINT), createRequest(dto), String.class);
			assertEquals(HttpStatus.OK, response.getStatusCode());
			dto = addCurrency("CAD", "Canada Dollar");
			response = restTemplate.postForEntity(createURL(ADD_CURRENCY_ENDPOINT), createRequest(dto), String.class);
			assertEquals(HttpStatus.OK, response.getStatusCode());
			dto = addCurrency("PLN", "Polish Zloty");
			response = restTemplate.postForEntity(createURL(ADD_CURRENCY_ENDPOINT), createRequest(dto), String.class);
			assertEquals(HttpStatus.OK, response.getStatusCode());
			ResponseEntity<GridResult> response2 = restTemplate.getForEntity(createURL(LIST_CURRENCY_ENDPOINT), GridResult.class);
			assertEquals(HttpStatus.OK, response2.getStatusCode());
			assertEquals(3, response2.getBody().getTotalSize());
		} catch (HttpClientErrorException e) {
			fail(e.getMessage());
		}
	}

	private AddCurrencyRequestDto addCurrency(String symbol, String name) {
		AddCurrencyRequestDto dto = new AddCurrencyRequestDto();
		dto.setName(name);
		dto.setSymbol(symbol);
		dto.setIsActive(isActive);
		return dto;
	}

	private String createURL(String path) {
		return "http://" + address + ":" + port + path;
	}

	private HttpEntity<AddCurrencyRequestDto> createRequest(AddCurrencyRequestDto dto) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<>(dto, headers);
	}
}
