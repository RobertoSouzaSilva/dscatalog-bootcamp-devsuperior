package com.robertosouza.descatalog.tests.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.robertosouza.descatalog.entities.Category;
import com.robertosouza.descatalog.entities.Product;
import com.robertosouza.descatalog.repositories.CategoryRepository;
import com.robertosouza.descatalog.repositories.ProductRepository;
import com.robertosouza.descatalog.tests.factory.ProductFactory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository catRepo;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;
	private long countPcGamerProducts;
	private long countProductsCategoy;
	private PageRequest page;

	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
		countPcGamerProducts = 21L;
		countProductsCategoy = 23L;
		page =  PageRequest.of(0, 10);
	}
	
	
	@Test
	public void findShouldThrowsEcptionWhenCategoryIsEmpty() {
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			String name = "";
			Optional<Category> catOpt = catRepo.findById(8L);
			List<Category> catList = new ArrayList<>();
			catList.add(catOpt.get());
			Page<Product> result = productRepository.find(catList, name, page);
			Assertions.assertTrue(result.isEmpty());
		});
	}
	
	@Test
	public void findShouldReturnProductsWhenCategoryExists() {
		String name = "";
		Optional<Category> catOpt = catRepo.findById(3L);
		List<Category> catList = new ArrayList<>();
		catList.add(catOpt.get());
		Page<Product> result = productRepository.find(catList, name, page);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countProductsCategoy, result.getTotalElements());
	}
	
	@Test
	public void findShouldReturnAllProductsWhenMameIsEmpty() {
		String name = "";
		Page<Product> result = productRepository.find(null, name, page);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
	}
	
	@Test
	public void findShouldReturnProductsWhenMameExistsIgnoringCase() {
		String name = "pc gAMeR";
		Page<Product> result = productRepository.find(null, name, page);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countPcGamerProducts, result.getTotalElements());
	}
	
	@Test
	public void findShouldReturnProductsWhenMameExists() {
		String name = "PC Gamer";
		Page<Product> result = productRepository.find(null, name, page);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countPcGamerProducts, result.getTotalElements());
	}
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Product product = ProductFactory.createProduct();
		product.setId(null);
		product = productRepository.save(product);
		Optional<Product> result = productRepository.findById(product.getId());
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1L, product.getId());
		Assertions.assertTrue(result.isPresent());
		Assertions.assertSame(result.get(), product);

	}
	

	@Test
	public void deletShouldDeletObjectWhenIdExists() {

		productRepository.deleteById(existingId);

		Optional<Product> result = productRepository.findById(existingId);

		Assertions.assertFalse(result.isPresent());

	}
	
	@Test
	public void deletShouldThrowEmptyResultDataAccessExceptionWhendIdDoesNotExists() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			productRepository.deleteById(nonExistingId);
		});
	}

}
