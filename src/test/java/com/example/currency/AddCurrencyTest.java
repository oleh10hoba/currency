package com.example.currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import com.example.currency.domain.Currency;
import com.example.currency.ui.CurrencyApplication;
import com.example.currency.ui.dto.request.AddCurrencyRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.UUID;

import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CurrencyApplication.class)
class AddCurrencyTest {
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value("${server.port}")
	String port;
	@Value("${server.address}")
	String address;

	Currency currency = new Currency();
	String name = "US Dollar";
	String symbol = "USD";
	String NAME = "name";
	String SYMBOL = "symbol";
	boolean isActive = true;
	Instant createdAt = Instant.now();
	String ADD_CURRENCY_ENDPOINT = "/currency/add";
	@AfterEach
	@BeforeEach
	void cleanTables() {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, "currency");
	}

	@Test
	@DisplayName("Add currency to the project should work")
	void addCurrencyTest() {
		currency = currency.add(UUID.randomUUID(), name, symbol, isActive, createdAt);
		assertEquals(name, currency.getName());
		assertEquals(symbol, currency.getSymbol());
		assertEquals(createdAt, currency.getCreatedAt());
	}

	@Test
	@DisplayName("Add currency with empty symbol length should return symbol required")
	void emptySymbolTest() {
		AddCurrencyRequestDto dto = addCurrencyCustomSymbol("");
		try {
			restTemplate.postForEntity(createURL(ADD_CURRENCY_ENDPOINT), createRequest(dto), String.class);
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals(requiredMessage(SYMBOL), e.getMessage());
		}
	}

	@Test
	@DisplayName("Add currency with null symbol length should return symbol required")
	void nullSymbolTest() {
		AddCurrencyRequestDto dto = addCurrencyCustomSymbol(null);
		try {
			restTemplate.postForEntity(createURL(ADD_CURRENCY_ENDPOINT), createRequest(dto), String.class);
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals(requiredMessage(SYMBOL), e.getMessage());
		}
	}

	@Test
	@DisplayName("Add currency with too short symbol length should return invalid length")
	void tooShortSymbolLengthTest() {
		AddCurrencyRequestDto dto = addCurrencyCustomSymbol("US");
		try {
			restTemplate.postForEntity(createURL(ADD_CURRENCY_ENDPOINT), createRequest(dto), String.class);
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals(invalidLengthMessage(SYMBOL), e.getMessage());
		}
	}

	@Test
	@DisplayName("Add currency with too long symbol should return invalid length")
	void tooLongSymbolTest() {
		AddCurrencyRequestDto dto = addCurrencyCustomSymbol("UUUU");
		try {
			restTemplate.postForEntity(createURL(ADD_CURRENCY_ENDPOINT), createRequest(dto), String.class);
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals(invalidLengthMessage(SYMBOL), e.getMessage());
		}
	}

	@Test
	@DisplayName("Add currency with invalid symbol should return invalid symbol")
	void invalidSymbolTest() {
		AddCurrencyRequestDto dto = addCurrencyCustomSymbol("AAA");
		try {
			restTemplate.postForEntity(createURL(ADD_CURRENCY_ENDPOINT), createRequest(dto), String.class);
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals(invalidMessage(SYMBOL), e.getMessage());
		}
	}

	@Test
	@DisplayName("Add existing currency symbol should return already exists")
	void existingCurrencySymbolTest() {
		AddCurrencyRequestDto dto = addCurrency();
		ResponseEntity<String> response;
		try {
			 response = restTemplate.postForEntity(createURL(ADD_CURRENCY_ENDPOINT), createRequest(dto), String.class);
			 assertEquals(HttpStatus.OK, response.getStatusCode());
			 dto.setName("NewName");
			 try {
				 restTemplate.postForEntity(createURL(ADD_CURRENCY_ENDPOINT), createRequest(dto), String.class);
			 } catch (HttpClientErrorException e) {
				 assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
				 assertEquals(alreadyExistsMessage(SYMBOL), e.getMessage());
			 }
		} catch (HttpClientErrorException e) {
			fail(e.getMessage());
		}
	}

	@Test
	@DisplayName("Add currency with empty name length should return name required")
	void emptyNameTest() {
		AddCurrencyRequestDto dto = addCurrencyCustomName("");
		try {
			restTemplate.postForEntity(createURL(ADD_CURRENCY_ENDPOINT), createRequest(dto), String.class);
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals(requiredMessage(NAME), e.getMessage());
		}
	}

	@Test
	@DisplayName("Add currency with null name length should return name required")
	void nullNameTest() {
		AddCurrencyRequestDto dto = addCurrencyCustomName(null);
		try {
			restTemplate.postForEntity(createURL(ADD_CURRENCY_ENDPOINT), createRequest(dto), String.class);
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals(requiredMessage(NAME), e.getMessage());
		}
	}

	@Test
	@DisplayName("Add currency with too short name should return invalid length")
	void tooShortNameTest() {
		AddCurrencyRequestDto dto = addCurrencyCustomName("U");
		try {
			restTemplate.postForEntity(createURL(ADD_CURRENCY_ENDPOINT), createRequest(dto), String.class);
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals(invalidLengthMessage(NAME), e.getMessage());
		}
	}

	@Test
	@DisplayName("Add currency with too long name should return invalid length")
	void tooLongNameTest() {
		AddCurrencyRequestDto dto = addCurrencyCustomName("NameField1NameField2NameField3NameField4NameField5NameField6Name");
		try {
			restTemplate.postForEntity(createURL(ADD_CURRENCY_ENDPOINT), createRequest(dto), String.class);
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals(invalidLengthMessage(NAME), e.getMessage());
		}
	}

	@Test
	@DisplayName("Add existing currency name should return already exists")
	void existingCurrencyNameLengthTest() {
		AddCurrencyRequestDto dto = addCurrency();
		ResponseEntity<String> response;
		try {
			response = restTemplate.postForEntity(createURL(ADD_CURRENCY_ENDPOINT), createRequest(dto), String.class);
			assertEquals(HttpStatus.OK, response.getStatusCode());
			dto.setSymbol("CAD");
			try {
				restTemplate.postForEntity(createURL(ADD_CURRENCY_ENDPOINT), createRequest(dto), String.class);
			} catch (HttpClientErrorException e) {
				assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
				assertEquals(alreadyExistsMessage(NAME), e.getMessage());
			}
		} catch (HttpClientErrorException e) {
			fail(e.getMessage());
		}
	}

	private String invalidLengthMessage(String fieldName) {
		return "400 : \"{\"" + fieldName + "\":\"error." + fieldName + "_invalid_length\"}\"";
	}

	private String invalidMessage(String fieldName) {
		return "400 : \"{\"" + fieldName + "\":\"error." + fieldName + "_invalid\"}\"";
	}

	private String requiredMessage(String fieldName) {
		return "400 : \"{\"" + fieldName + "\":\"error." + fieldName + "_required\"}\"";
	}

	private String alreadyExistsMessage(String fieldName) {
		return "400 : \"{\"" + fieldName + "\":\"error." + fieldName + "_already_exists\"}\"";
	}

	private String createURL(String path) {
		return "http://" + address + ":" + port + path;
	}

	private AddCurrencyRequestDto addCurrency() {
		AddCurrencyRequestDto dto = new AddCurrencyRequestDto();
		dto.setName(name);
		dto.setSymbol(symbol);
		dto.setIsActive(isActive);
		return dto;
	}

	private AddCurrencyRequestDto addCurrencyCustomSymbol(String symbol) {
		AddCurrencyRequestDto dto = new AddCurrencyRequestDto();
		dto.setName(name);
		dto.setSymbol(symbol);
		dto.setIsActive(isActive);
		return dto;
	}

	private AddCurrencyRequestDto addCurrencyCustomName(String name) {
		AddCurrencyRequestDto dto = new AddCurrencyRequestDto();
		dto.setName(name);
		dto.setSymbol(symbol);
		dto.setIsActive(isActive);
		return dto;
	}

	private HttpEntity<AddCurrencyRequestDto> createRequest(AddCurrencyRequestDto dto) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<>(dto, headers);
	}
}
