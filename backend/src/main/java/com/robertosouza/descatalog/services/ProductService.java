package com.robertosouza.descatalog.services;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.robertosouza.descatalog.dto.CategoryDTO;
import com.robertosouza.descatalog.dto.ProductDTO;
import com.robertosouza.descatalog.entities.Category;
import com.robertosouza.descatalog.entities.Product;
import com.robertosouza.descatalog.repositories.CategoryRepository;
import com.robertosouza.descatalog.repositories.ProductRepository;
import com.robertosouza.descatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
//	@Transactional(readOnly = true)
//	public List<ProductDTO> findAll(){
//		List<Product> list = productRepository.findAll();
//		return list.stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());
//	}
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Long categoryId, String name, PageRequest pageRequest){
		
		List<Category> categories = (categoryId == 0) ? null : Arrays.asList(categoryRepository.getOne(categoryId));
		
		Page<Product> list = productRepository.find(categories, name.trim(), pageRequest);
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> productOptional = productRepository.findById(id);
		Product productEntity = productOptional.orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
		return new ProductDTO(productEntity, productEntity.getCategories());
		
	}

	@Transactional
	public ProductDTO insert(ProductDTO productDTO) {
		Product productEntity = new Product();
		copyDtoToEntity(productDTO, productEntity);
		//productEntity.setName(productDTO.getName());
		productEntity = productRepository.save(productEntity);
		return new ProductDTO(productEntity);
		
	}

	

	@Transactional
	public ProductDTO update(Long id, ProductDTO productDTO) {
		try {
			Product entity = productRepository.getOne(id);
			copyDtoToEntity(productDTO, entity);
			//entity.setName(productDTO.getName());
			entity = productRepository.save(entity);
			return new ProductDTO(entity);
		} catch(EntityNotFoundException e ) {
			throw new ResourceNotFoundException("Id não encontrado " + id);
		}
	}

	public void delete(Long id) {
		try {
			productRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id não encontrado " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("Violação de integridade");
		}
	}
	
	private void copyDtoToEntity(ProductDTO productDTO, Product productEntity) {
		productEntity.setName(productDTO.getName());
		productEntity.setDescription(productDTO.getDescription());
		productEntity.setDate(productDTO.getDate());
		productEntity.setImgUrl(productDTO.getImgUrl());
		productEntity.setPrice(productDTO.getPrice());
		
		productEntity.getCategories().clear();
		
		for(CategoryDTO catDto: productDTO.getCategories()) {
			Category category = categoryRepository.getOne(catDto.getId());
			productEntity.getCategories().add(category);
		}
		
	}
	
	
	
}
