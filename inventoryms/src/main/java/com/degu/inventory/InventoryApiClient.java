package com.degu.inventory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.degu.inventory.model.Category;
import com.degu.inventory.model.InventoryItem;
import com.degu.inventory.model.InventoryStatus;
import com.degu.inventory.model.Product;
import com.degu.inventory.model.Warehouse; // Corrected model

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class InventoryApiClient {

    private static final String BASE_URL = "http://localhost:8080/api";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public InventoryApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public Optional<InventoryItem[]> getAllInventoryItems() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/inventory-items"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return Optional.of(objectMapper.readValue(response.body(), InventoryItem[].class));
        } else {
            throw new RuntimeException("Failed to get inventory items: " + response.body());
        }
    }

    public Optional<InventoryItem> getInventoryItem(Integer id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/inventory-items/" + id))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return Optional.of(objectMapper.readValue(response.body(), InventoryItem.class));
        } else if (response.statusCode() == 404) {
            return Optional.empty();
        } else {
            throw new RuntimeException("Failed to get inventory item " + id + ": " + response.body());
        }
    }

    public Optional<InventoryItem> createInventoryItem(InventoryItem item) throws Exception {
        String requestBody = objectMapper.writeValueAsString(item);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/inventory-items"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 201) { // 201 Created
            return Optional.of(objectMapper.readValue(response.body(), InventoryItem.class));
        } else {
            throw new RuntimeException("Failed to create inventory item: " + response.body());
        }
    }

    public Optional<InventoryItem> updateInventoryItem(InventoryItem item) throws Exception {
        String requestBody = objectMapper.writeValueAsString(item);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/inventory-items"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return Optional.of(objectMapper.readValue(response.body(), InventoryItem.class));
        } else if (response.statusCode() == 404) {
            return Optional.empty();
        } else {
            throw new RuntimeException("Failed to update inventory item: " + response.body());
        }
    }

    public void deleteInventoryItem(Integer id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/inventory-items/" + id))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 204) { // 204 No Content
            throw new RuntimeException("Failed to delete inventory item " + id + ": " + response.body());
        }
    }

    // --- Category Endpoints ---
    public Optional<Category[]> getAllCategories() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/categories"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return Optional.of(objectMapper.readValue(response.body(), Category[].class));
        } else {
            throw new RuntimeException("Failed to get categories: " + response.body());
        }
    }

    public Optional<Category> getCategory(Integer id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/categories/" + id))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return Optional.of(objectMapper.readValue(response.body(), Category.class));
        } else if (response.statusCode() == 404) {
            return Optional.empty();
        } else {
            throw new RuntimeException("Failed to get category " + id + ": " + response.body());
        }
    }

    // --- Product Endpoints ---
    public Optional<Product[]> getAllProducts() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/products"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return Optional.of(objectMapper.readValue(response.body(), Product[].class));
        } else {
            throw new RuntimeException("Failed to get products: " + response.body());
        }
    }

    public Optional<Product> getProduct(Integer id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/products/" + id))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return Optional.of(objectMapper.readValue(response.body(), Product.class));
        } else if (response.statusCode() == 404) {
            return Optional.empty();
        } else {
            throw new RuntimeException("Failed to get product " + id + ": " + response.body());
        }
    }

    // --- InventoryStatus Endpoints ---
    public Optional<InventoryStatus[]> getAllInventoryStatuses() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/inventory-statuses"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return Optional.of(objectMapper.readValue(response.body(), InventoryStatus[].class));
        } else {
            throw new RuntimeException("Failed to get inventory statuses: " + response.body());
        }
    }

    public Optional<InventoryStatus> getInventoryStatus(Integer id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/inventory-statuses/" + id))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return Optional.of(objectMapper.readValue(response.body(), InventoryStatus.class));
        } else if (response.statusCode() == 404) {
            return Optional.empty();
        } else {
            throw new RuntimeException("Failed to get inventory status " + id + ": " + response.body());
        }
    }

    // --- Warehouse Endpoints ---
    public Optional<Warehouse[]> getAllWarehouses() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/warehouses"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return Optional.of(objectMapper.readValue(response.body(), Warehouse[].class));
        } else {
            throw new RuntimeException("Failed to get warehouses: " + response.body());
        }
    }

    public Optional<Warehouse> getWarehouse(Integer id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/warehouses/" + id))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return Optional.of(objectMapper.readValue(response.body(), Warehouse.class));
        } else if (response.statusCode() == 404) {
            return Optional.empty();
        } else {
            throw new RuntimeException("Failed to get warehouse " + id + ": " + response.body());
        }
    }
}
