package com.degu.inventory.service;
import com.degu.inventory.model.Product;
public interface ProductService {
	Product[] getAll() throws Exception;
	Product get(Integer id) throws Exception;
	Product create(Product product) throws Exception;
	Product update(Product product) throws Exception;
	void delete(Integer id) throws Exception;
}
