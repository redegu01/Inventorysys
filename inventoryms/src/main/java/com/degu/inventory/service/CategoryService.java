package com.degu.inventory.service;
import com.degu.inventory.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CategoryService {
	Logger logger = LoggerFactory.getLogger(CategoryService.class);
	@Value("${service.api.endpoint}")
	private String endpointUrl = "http://localhost:8080/api/category";

	protected static CategoryService service = null;
	public static CategoryService getService(){
		if(service == null){
			service = new CategoryService();
		}
		return service;
	}

	RestTemplate restTemplate = null;
	public RestTemplate getRestTemplate() {
		if(restTemplate == null) {
			restTemplate = new RestTemplate();
			List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
			converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
			messageConverters.add(converter);
			restTemplate.setMessageConverters(messageConverters);
		}
		return restTemplate;
	}

	public Category get(Integer id) {
		String url = endpointUrl + "/" + id; // Corrected concatenation
		logger.info("get: "  + url);
		HttpHeaders headers = new HttpHeaders();
		HttpEntity request = new HttpEntity<>(null, headers);
		final ResponseEntity<Category> response =
				getRestTemplate().exchange(url, HttpMethod.GET, request, Category.class);
		return response.getBody();
	}

	public Category[] getAll() {
		String url = endpointUrl;
		logger.info("getCategories: " + url); // Log message refined
		HttpHeaders headers = new HttpHeaders();
		HttpEntity request = new HttpEntity<>(null, headers);
		final ResponseEntity<Category[]> response =
				getRestTemplate().exchange(url, HttpMethod.GET, request, Category[].class);
		Category[] categories = response.getBody(); // Variable name refined
		return categories;
	}

	public Category create(Category category) {
		String url = endpointUrl;
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Category> request = new HttpEntity<>(category, headers);
		final ResponseEntity<Category> response =
				getRestTemplate().exchange(url, HttpMethod.PUT, request, Category.class);
		return response.getBody();
	}
	public Category update(Category category) {
		logger.info("update: " + category.toString());
		String url = endpointUrl;
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Category> request = new HttpEntity<>(category, headers);
		final ResponseEntity<Category> response =
				getRestTemplate().exchange(url, HttpMethod.POST, request, Category.class);
		return response.getBody();
	}

	public void delete(Integer id){
		logger.info("delete: " + id);
		String url = endpointUrl + "/" + id; // Corrected concatenation
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Category> request = new HttpEntity<>(null, headers);
		final ResponseEntity<Category> response =
				getRestTemplate().exchange(url, HttpMethod.DELETE, request, Category.class);
	}
}