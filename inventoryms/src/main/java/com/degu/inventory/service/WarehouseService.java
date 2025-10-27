package com.degu.inventory.service;
import com.degu.inventory.model.Warehouse; // Changed from Inventory
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
public class WarehouseService { // Class name changed
	Logger logger = LoggerFactory.getLogger(WarehouseService.class); // Logger class changed
	@Value("${service.api.endpoint}")
	private String endpointUrl = "http://localhost:8080/api/warehouse"; // Endpoint changed

	protected static WarehouseService service= null; // Service type changed
	public static WarehouseService getService(){ // Method return type and name changed
		if(service == null){
			service = new WarehouseService(); // Object instantiation changed
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

	public Warehouse get(Integer id) { // Return type changed
		String url = endpointUrl + "/" + id; // Corrected concatenation
		logger.info("get: "  + url);
		HttpHeaders headers = new HttpHeaders();
		HttpEntity request = new HttpEntity<>(null, headers);
		final ResponseEntity<Warehouse> response = // Type changed
				getRestTemplate().exchange(url, HttpMethod.GET, request, Warehouse.class); // Type changed
		return response.getBody();
	}

	public Warehouse[] getAll() { // Return type changed
		String url = endpointUrl;
		logger.info("getWarehouses: " + url); // Log message changed
		HttpHeaders headers = new HttpHeaders();
		HttpEntity request = new HttpEntity<>(null, headers);
		final ResponseEntity<Warehouse[]> response = // Type changed
				getRestTemplate().exchange(url, HttpMethod.GET, request, Warehouse[].class); // Type changed
		Warehouse[] warehouses = response.getBody(); // Variable type and name changed
		return warehouses;
	}

	public Warehouse create(Warehouse warehouse) { // Parameter type and name changed, return type changed
		String url = endpointUrl;
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Warehouse> request = new HttpEntity<>(warehouse, headers); // Type changed
		final ResponseEntity<Warehouse> response = // Type changed
				getRestTemplate().exchange(url, HttpMethod.PUT, request, Warehouse.class); // Type changed
		return response.getBody();
	}
	public Warehouse update(Warehouse warehouse) { // Parameter type and name changed, return type changed
		logger.info("update: " + warehouse.toString()); // Log message changed
		String url = endpointUrl;
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Warehouse> request = new HttpEntity<>(warehouse, headers); // Type changed
		final ResponseEntity<Warehouse> response = // Type changed
				getRestTemplate().exchange(url, HttpMethod.POST, request, Warehouse.class); // Type changed
		return response.getBody();
	}

	public void delete(Integer id){
		logger.info("delete: " + id);
		String url = endpointUrl + "/" + id; // Corrected concatenation
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Warehouse> request = new HttpEntity<>(null, headers); // Type changed
		final ResponseEntity<Warehouse> response = // Type changed
				getRestTemplate().exchange(url, HttpMethod.DELETE, request, Warehouse.class); // Type changed
	}
}