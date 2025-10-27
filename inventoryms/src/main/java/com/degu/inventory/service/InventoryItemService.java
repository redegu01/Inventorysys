package com.degu.inventory.service;
import com.degu.inventory.model.InventoryItem; // Changed from Ecommerce
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.web.client.RestTemplate;

@Service
public class InventoryItemService { // Class name changed
	Logger logger = LoggerFactory.getLogger(InventoryItemService.class); // Logger class changed
	@Value("${service.api.endpoint}")
	private String endpointUrl = "http://localhost:8080/api/inventory-item"; // Endpoint changed

	protected static InventoryItemService service= null; // Service type changed
	public static InventoryItemService getService(){ // Method return type and name changed
		if(service == null){
			service = new InventoryItemService(); // Object instantiation changed
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

	public InventoryItem get(Integer id) { // Return type changed
		String url = endpointUrl + "/" + id; // Corrected concatenation
		logger.info("get: "  + url);
		HttpHeaders headers = new HttpHeaders();
		HttpEntity request = new HttpEntity<>(null, headers);
		final ResponseEntity<InventoryItem> response = // Type changed
				getRestTemplate().exchange(url, HttpMethod.GET, request, InventoryItem.class); // Type changed
		return response.getBody();
	}

	public InventoryItem[] getAll() { // Return type changed
		String url = endpointUrl;
		logger.info("getInventoryItems: " + url); // Log message changed
		HttpHeaders headers = new HttpHeaders();
		HttpEntity request = new HttpEntity<>(null, headers);
		final ResponseEntity<InventoryItem[]> response = // Type changed
				getRestTemplate().exchange(url, HttpMethod.GET, request, InventoryItem[].class); // Type changed
		InventoryItem[] inventoryItems = response.getBody(); // Variable type and name changed
		return inventoryItems;
	}

	public InventoryItem create(InventoryItem inventoryItem) { // Parameter type and name changed, return type changed
		String url = endpointUrl;
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<InventoryItem> request = new HttpEntity<>(inventoryItem, headers); // Type changed
		final ResponseEntity<InventoryItem> response = // Type changed
				getRestTemplate().exchange(url, HttpMethod.PUT, request, InventoryItem.class); // Type changed
		return response.getBody();
	}
	public InventoryItem update(InventoryItem inventoryItem) { // Parameter type and name changed, return type changed
		logger.info("update: " + inventoryItem.toString()); // Log message changed
		String url = endpointUrl;
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<InventoryItem> request = new HttpEntity<>(inventoryItem, headers); // Type changed
		final ResponseEntity<InventoryItem> response = // Type changed
				getRestTemplate().exchange(url, HttpMethod.POST, request, InventoryItem.class); // Type changed
		return response.getBody();
	}

	public void delete(Integer id){
		logger.info("delete: " + id);
		String url = endpointUrl + "/" + id; // Corrected concatenation
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<InventoryItem> request = new HttpEntity<>(null, headers); // Type changed
		final ResponseEntity<InventoryItem> response = // Type changed
				getRestTemplate().exchange(url, HttpMethod.DELETE, request, InventoryItem.class); // Type changed
	}
}