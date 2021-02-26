package com.robertosouza.descatalog.tests.factory;

import java.time.Instant;

import com.robertosouza.descatalog.dto.ProductDTO;
import com.robertosouza.descatalog.entities.Category;
import com.robertosouza.descatalog.entities.Product;

public class ProductFactory {
	
	public static Product createProduct() {
		Product product =  new Product(1L, "Phone", "Good phone", 800.0, "http://localhost.com", Instant.parse("2020-10-20T03:00:00Z"));
		product.getCategories().add(new Category(1L,null));
		return product;

	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}
	
	public static ProductDTO createProductDTO(Long id) {
		ProductDTO productDTO = createProductDTO();
		productDTO.setId(id);
		return productDTO;
	}

}
