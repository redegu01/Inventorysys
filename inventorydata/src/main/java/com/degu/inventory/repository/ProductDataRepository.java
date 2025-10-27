package com.degu.inventory.repository;
import com.degu.inventory.entity.ProductData;
import org.springframework.data.repository.CrudRepository;
public interface ProductDataRepository extends CrudRepository<ProductData,Integer> {}