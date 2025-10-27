package com.degu.inventory.serviceimpl;

import com.degu.inventory.entity.ProductData;
import com.degu.inventory.model.Product;
import com.degu.inventory.repository.ProductDataRepository;
import com.degu.inventory.service.ProductService;
import com.degu.inventory.transform.ProductTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

	Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	ProductDataRepository productDataRepository;

	@Override
	public Product[] getAll() throws Exception {
		List<ProductData> productsData = (List<ProductData>) productDataRepository.findAll();
		return ProductTransformer.toProductArray(productsData.toArray(new ProductData[0]));
	}

	@Override
	public Product create(Product product) throws Exception {
		logger.info("create: Input product: {}", product.toString());

		ProductData productData = ProductTransformer.toProductData(product);
		productData.setId(0);

		productData = productDataRepository.save(productData);
		logger.info("create: Saved product data: {}", productData.toString());

		return ProductTransformer.toProduct(productData);
	}

	@Override
	public Product update(Product product) throws Exception {
		int id = product.getId();
		Optional<ProductData> optional  = productDataRepository.findById(id);

		if(optional.isPresent()){
			logger.info("update: Updating product with ID: {}", id);
			ProductData existingProductData = optional.get();
			ProductData productDataToUpdate = ProductTransformer.toProductData(product);
			productDataToUpdate.setCreated(existingProductData.getCreated());

			ProductData updatedProductData = productDataRepository.save(productDataToUpdate);
			logger.info("update: Updated product data: {}", updatedProductData.toString());

			return ProductTransformer.toProduct(updatedProductData);
		}
		else {
			logger.error("update: Product record with id: {} does not exist.", id);
			throw new Exception("Product record with id: " + id + " does not exist.");
		}
	}

	@Override
	public Product get(Integer id) throws Exception {
		logger.info("get: Input ID: {}", id);
		Optional<ProductData> optional = productDataRepository.findById(id);

		if(optional.isPresent()) {
			logger.info("get: Product with ID {} found.", id);
			return ProductTransformer.toProduct(optional.get());
		}
		else {
			logger.info("get: Failed to locate product with ID: {}", id);
			throw new Exception("Unable to locate product with ID: " + id);
		}
	}

	@Override
	public void delete(Integer id) throws Exception {
		logger.info("delete: Input ID: {}", id);
		Optional<ProductData> optional = productDataRepository.findById(id);

		if( optional.isPresent()) {
			productDataRepository.delete(optional.get());
			logger.info("delete: Successfully deleted Product record with id: {}", id);
		}
		else {
			logger.error("delete: Unable to locate product with id: {}", id);
			throw new Exception("Unable to locate product with id: " + id);
		}
	}
}