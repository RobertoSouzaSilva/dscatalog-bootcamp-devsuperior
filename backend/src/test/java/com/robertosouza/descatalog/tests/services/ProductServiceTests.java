package com.robertosouza.descatalog.tests.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.robertosouza.descatalog.dto.ProductDTO;
import com.robertosouza.descatalog.entities.Product;
import com.robertosouza.descatalog.repositories.ProductRepository;
import com.robertosouza.descatalog.services.ProductService;
import com.robertosouza.descatalog.services.exceptions.DatabaseException;
import com.robertosouza.descatalog.services.exceptions.ResourceNotFoundException;
import com.robertosouza.descatalog.tests.factory.ProductFactory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	private long existingId;
	private long nonExistingId;
	private long dependetId;
	private Product product;
	private ProductDTO productDto;
	private PageImpl<Product> page;
	private PageRequest pageReq;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		dependetId = 4L;
		product = ProductFactory.createProduct();
		productDto = ProductFactory.createProductDTO();
		page = new PageImpl<>(List.of(product));
		pageReq = PageRequest.of(0, 10);

		Mockito.when(repository.find(ArgumentMatchers.any(), ArgumentMatchers.anyString(), ArgumentMatchers.any()))
				.thenReturn(page);

		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.getOne(existingId)).thenReturn(product);
		Mockito.doThrow(EntityNotFoundException.class).when(repository).getOne(nonExistingId);

		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependetId);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionuctDtoWhenIdExists() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, productDto);
		});
	}
	
	@Test
	public void updateShouldReturnProductDtoWhenIdExists() {
		ProductDTO result = service.update(existingId, productDto);
		Assertions.assertFalse(result == null);
		Assertions.assertEquals(result.getName(), product.getName());
		Mockito.verify(repository, Mockito.times(1)).save(product);
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionuctDtoWhenIdExists() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
		Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
	}

	@Test
	public void findByIdShouldReturnProductDtoWhenIdExists() {
		ProductDTO result = service.findById(existingId);
		Assertions.assertFalse(result == null);
		Assertions.assertEquals(result.getName(), product.getName());
		Mockito.verify(repository, Mockito.times(1)).findById(existingId);
	}

	@Test
	public void findAllPagedShoulReturnPage() {
		Page<ProductDTO> result = service.findAllPaged(0L, "", pageReq);
		Assertions.assertNotNull(result);
		Assertions.assertFalse(result.isEmpty());
		Mockito.verify(repository, Mockito.times(1)).find(null, "", pageReq);

	}

	@Test
	public void deleteShoulThrowDatabaseExceptionWhenDependentId() {

		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependetId);
		});

		Mockito.verify(repository, Mockito.times(1)).deleteById(dependetId);
	}

	@Test
	public void deleteShoulThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});

		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
	}

	@Test
	public void deleteShoulDoNothingWhenIdExists() {

		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});

		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}

}
