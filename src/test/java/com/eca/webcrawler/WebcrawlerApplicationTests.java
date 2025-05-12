package com.eca.webcrawler;

import com.eca.webcrawler.model.WebCrawlerResult;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebcrawlerApplicationTests {

	@LocalServerPort
	private int port;

	private final TestRestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void testCrawlEndpoint_validUrl() {
		//remove the test url as suggested in guidelines
		String baseUrl = "";
		ResponseEntity<WebCrawlerResult> response = restTemplate.getForEntity(baseUrl, WebCrawlerResult.class);

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		WebCrawlerResult body = response.getBody();
		assertThat(body).isNotNull();
		assertThat(body.getPages()).isNotEmpty();
	}
	@Test
	public void testCrawlEndpoint_invalidUrl() {
		String baseUrl = "http://localhost:" + port + "/pages?target=invalid-url";
		ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);
		assertThat(response.getStatusCode().is4xxClientError()).isTrue();
	}
	@Test
	public void testCrawlEndpoint_recursionLimit() {
		//remove the test url as suggested in guidelines
		String baseUrl = "";
		ResponseEntity<WebCrawlerResult> response = restTemplate.getForEntity(baseUrl, WebCrawlerResult.class);

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		WebCrawlerResult body = response.getBody();
		assertThat(body).isNotNull();
		assertThat(body.getPages().size()).isEqualTo(5); // only root page due to depth 0
	}
}
