package com.degu.inventory.service;
import com.degu.inventory.model.InventoryStatus; // Changed from Status
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
public class InventoryStatusService { // Class name changed
	Logger logger = LoggerFactory.getLogger(InventoryStatusService.class); // Logger class changed
	@Value("${service.api.endpoint}")
	private String endpointUrl = "http://localhost:8080/api/inventory-status"; // Endpoint changed

	protected static InventoryStatusService service= null; // Service type changed
	public static InventoryStatusService getService(){ // Method return type and name changed
		if(service == null){
			service = new InventoryStatusService(); // Object instantiation changed
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

	public InventoryStatus get(Integer id) { // Return type changed
		String url = endpointUrl + "/" + id; // Corrected concatenation
		logger.info("get: "  + url);
		HttpHeaders headers = new HttpHeaders();
		HttpEntity request = new HttpEntity<>(null, headers);
		final ResponseEntity<InventoryStatus> response = // Type changed
				getRestTemplate().exchange(url, HttpMethod.GET, request, InventoryStatus.class); // Type changed
		return response.getBody();
	}

	public InventoryStatus[] getAll() { // Return type changed
		String url = endpointUrl;
		logger.info("getInventoryStatuses: " + url); // Log message changed
		HttpHeaders headers = new HttpHeaders();
		HttpEntity request = new HttpEntity<>(null, headers);
		final ResponseEntity<InventoryStatus[]> response = // Type changed
				getRestTemplate().exchange(url, HttpMethod.GET, request, InventoryStatus[].class); // Type changed
		InventoryStatus[] inventoryStatuses = response.getBody(); // Variable type and name changed
		return inventoryStatuses;
	}

	public InventoryStatus create(InventoryStatus inventoryStatus) { // Parameter type and name changed, return type changed
		String url = endpointUrl;
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<InventoryStatus> request = new HttpEntity<>(inventoryStatus, headers); // Type changed
		final ResponseEntity<InventoryStatus> response = // Type changed
				getRestTemplate().exchange(url, HttpMethod.PUT, request, InventoryStatus.class); // Type changed
		return response.getBody();
	}
	public InventoryStatus update(InventoryStatus inventoryStatus) { // Parameter type and name changed, return type changed
		logger.info("update: " + inventoryStatus.toString()); // Log message changed
		String url = endpointUrl;
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<InventoryStatus> request = new HttpEntity<>(inventoryStatus, headers); // Type changed
		final ResponseEntity<InventoryStatus> response = // Type changed
				getRestTemplate().exchange(url, HttpMethod.POST, request, InventoryStatus.class); // Type changed
		return response.getBody();
	}

	public void delete(Integer id){
		logger.info("delete: " + id);
		String url = endpointUrl + "/" + id; // Corrected concatenation
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<InventoryStatus> request = new HttpEntity<>(null, headers); // Type changed
		final ResponseEntity<InventoryStatus> response = // Type changed
				getRestTemplate().exchange(url, HttpMethod.DELETE, request, InventoryStatus.class); // Type changed
	}
}