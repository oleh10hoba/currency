package com.example.currency;

import com.example.currency.ui.CurrencyApplication;
import com.example.currency.ui.dto.request.AddCurrencyRequestDto;
import com.example.currency.ui.dto.request.SymbolRequestDto;
import com.example.currency.ui.dto.response.ExchangeRateResponseDto;
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
class ExchangeRateTest {
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value("${server.port}")
	String port;
	@Value("${server.address}")
	String address;
	String name = "US Dollar";
	boolean isActive = true;
	String SYMBOL = "symbol";
	String ADD_CURRENCY_ENDPOINT = "/currency/add";
	String LIST_EXCHANGE_RATE_ENDPOINT = "/currency/exchange-rate/list";

	@AfterEach
	@BeforeEach
	void cleanTables() {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, "currency");
	}

	@Test
	@DisplayName("Get exchange rates for empty symbol should return symbol required")
	void emptySymbolTest() {
		SymbolRequestDto dto = addExchangeRatesSymbol("");
		try {
			restTemplate.postForEntity(createURL(LIST_EXCHANGE_RATE_ENDPOINT), createRequest(dto), String.class);
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals(requiredMessage(SYMBOL), e.getMessage());
		}
	}

	@Test
	@DisplayName("Get exchange rates for null symbol should return symbol required")
	void nullSymbolTest() {
		SymbolRequestDto dto = addExchangeRatesSymbol(null);
		try {
			restTemplate.postForEntity(createURL(LIST_EXCHANGE_RATE_ENDPOINT), createRequest(dto), String.class);
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals(requiredMessage(SYMBOL), e.getMessage());
		}
	}

	@Test
	@DisplayName("Get exchange rates for too short symbol should return symbol invalid length")
	void tooShortSymbolTest() {
		SymbolRequestDto dto = addExchangeRatesSymbol("US");
		try {
			restTemplate.postForEntity(createURL(LIST_EXCHANGE_RATE_ENDPOINT), createRequest(dto), String.class);
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals(invalidLengthMessage(SYMBOL), e.getMessage());
		}
	}

	@Test
	@DisplayName("Get exchange rates for too long symbol should return symbol invalid length")
	void tooLongSymbolTest() {
		SymbolRequestDto dto = addExchangeRatesSymbol("UUUU");
		try {
			restTemplate.postForEntity(createURL(LIST_EXCHANGE_RATE_ENDPOINT), createRequest(dto), String.class);
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals(invalidLengthMessage(SYMBOL), e.getMessage());
		}
	}

	@Test
	@DisplayName("Get exchange rates for not available symbol should return symbol invalid length")
	void notAvailableSymbolTest() {
		SymbolRequestDto dto = addExchangeRatesSymbol("USD");
		try {
			restTemplate.postForEntity(createURL(LIST_EXCHANGE_RATE_ENDPOINT), createRequest(dto), String.class);
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals(notAvailableMessage(SYMBOL), e.getMessage());
		}
	}

	@Test
	@DisplayName("Get exchange rates for available symbol should return exchange rates")
	void availableSymbolTest() {
		String symbol = "CAD";
		AddCurrencyRequestDto currencyDto = addCurrencyCustomSymbol(symbol);
		ResponseEntity<String> response = restTemplate.postForEntity(createURL(ADD_CURRENCY_ENDPOINT), createRequest(currencyDto), String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		SymbolRequestDto symbolDto = addExchangeRatesSymbol(symbol);
		ResponseEntity<ExchangeRateResponseDto> response2 = restTemplate.postForEntity(createURL(LIST_EXCHANGE_RATE_ENDPOINT), createRequest(symbolDto), ExchangeRateResponseDto.class);
		assertEquals(HttpStatus.OK, response2.getStatusCode());
		assertEquals(1.0, response2.getBody().getConversion_rates().get(symbol));
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

	private String notAvailableMessage(String fieldName) {
		return "400 : \"{\"" + fieldName + "\":\"error." + fieldName + "_not_available\"}\"";
	}



	private AddCurrencyRequestDto addCurrencyCustomSymbol(String symbol) {
		AddCurrencyRequestDto dto = new AddCurrencyRequestDto();
		dto.setName(name);
		dto.setSymbol(symbol);
		dto.setIsActive(isActive);
		return dto;
	}

	private SymbolRequestDto addExchangeRatesSymbol(String symbol) {
		SymbolRequestDto dto = new SymbolRequestDto();
		dto.setSymbol(symbol);
		return dto;
	}

	private String createURL(String path) {
		return "http://" + address + ":" + port + path;
	}

	private HttpEntity<SymbolRequestDto> createRequest(SymbolRequestDto dto) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<>(dto, headers);
	}

	private HttpEntity<AddCurrencyRequestDto> createRequest(AddCurrencyRequestDto dto) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<>(dto, headers);
	}
}
