package com.robertosouza.descatalog.tests.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.robertosouza.descatalog.dto.ProductDTO;
import com.robertosouza.descatalog.services.ProductService;
import com.robertosouza.descatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIT {

	@Autowired
	private ProductService service;


	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;
	private long countPcGamerProducts;
	private PageRequest page;
	//private long dependetId;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
		countPcGamerProducts = 21L;
		page =  PageRequest.of(0, 10);

		//dependetId = 4L;

	}
	
//	@Test
//	public void deleteShoulThrowDatabaseExceptionWhenDependentId() {
//
//		Assertions.assertThrows(DatabaseException.class, () -> {
//			service.delete(dependetId);
//		});
//
//	}
	
	@Test
	public void findAllPagedfShouldReturnAllProductsWhenMameIsEmpty() {
		String name = "";
		Page<ProductDTO> result = service.findAllPaged(0L, name, page);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
	}
	
	@Test
	public void findAllPagedShouldReturnProductsWhenMameExistsIgnoringCase() {
		String name = "pc gAMeR";
		Page<ProductDTO> result = service.findAllPaged(0L, name, page);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countPcGamerProducts, result.getTotalElements());
	}
	
	@Test
	public void findAllPagedShouldReturnProductsWhenMameExists() {
		String name = "PC Gamer";
		Page<ProductDTO> result = service.findAllPaged(0L, name, page);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countPcGamerProducts, result.getTotalElements());
	}

	@Test
	public void deleteShoulThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});

	}

	@Test
	public void deleteShoulDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
	}

}
