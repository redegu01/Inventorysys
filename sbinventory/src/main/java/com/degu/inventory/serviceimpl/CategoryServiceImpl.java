package com.degu.inventory.serviceimpl;

import com.degu.inventory.entity.CategoryData;
import com.degu.inventory.model.Category;
import com.degu.inventory.repository.CategoryDataRepository;
import com.degu.inventory.service.CategoryService;
import com.degu.inventory.transform.CategoryTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

	Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Autowired
	CategoryDataRepository categoryDataRepository;

	@Override
	public Category[] getAll() throws Exception {
		List<CategoryData> categoriesData = (List<CategoryData>) categoryDataRepository.findAll();
		return CategoryTransformer.toCategoryArray(categoriesData.toArray(new CategoryData[0]));
	}

	@Override
	public Category create(Category category) throws Exception {
		logger.info("create: Input category: {}", category.toString());
		CategoryData categoryData = CategoryTransformer.toCategoryData(category);
		categoryData.setId(0);
		categoryData = categoryDataRepository.save(categoryData);
		logger.info("create: Saved category data: {}", categoryData.toString());

		return CategoryTransformer.toCategory(categoryData);
	}

	@Override
	public Category update(Category category) throws Exception {
		int id = category.getId();
		Optional<CategoryData> optional  = categoryDataRepository.findById(id);

		if(optional.isPresent()){
			logger.info("update: Updating category with ID: {}", id);
			CategoryData existingCategoryData = optional.get();

			CategoryData categoryDataToUpdate = CategoryTransformer.toCategoryData(category);
			categoryDataToUpdate.setCreated(existingCategoryData.getCreated());

			CategoryData updatedCategoryData = categoryDataRepository.save(categoryDataToUpdate);
			logger.info("update: Updated category data: {}", updatedCategoryData.toString());

			return CategoryTransformer.toCategory(updatedCategoryData);
		}
		else {
			logger.error("update: Category record with id: {} does not exist.", id);
			throw new Exception("Category record with id: " + id + " does not exist.");
		}
	}

	@Override
	public Category get(Integer id) throws Exception {
		logger.info("get: Input ID: {}", id);
		Optional<CategoryData> optional = categoryDataRepository.findById(id);

		if(optional.isPresent()) {
			logger.info("get: Category with ID {} found.", id);
			return CategoryTransformer.toCategory(optional.get());
		}
		else {
			logger.info("get: Failed to locate category with ID: {}", id);
			throw new Exception("Unable to locate category with ID: " + id);
		}
	}

	@Override
	public void delete(Integer id) throws Exception {
		logger.info("delete: Input ID: {}", id);
		Optional<CategoryData> optional = categoryDataRepository.findById(id);

		if( optional.isPresent()) {
			categoryDataRepository.delete(optional.get());
			logger.info("delete: Successfully deleted Category record with id: {}", id);
		}
		else {
			logger.error("delete: Unable to locate category with id: {}", id);
			throw new Exception("Unable to locate category with ID: " + id);
		}
	}
}
