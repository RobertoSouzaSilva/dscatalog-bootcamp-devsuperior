package com.robertosouza.descatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.robertosouza.descatalog.dto.ProductDTO;
import com.robertosouza.descatalog.entities.Product;
import com.robertosouza.descatalog.repositories.ProductRepository;
import com.robertosouza.descatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
//	@Transactional(readOnly = true)
//	public List<ProductDTO> findAll(){
//		List<Product> list = productRepository.findAll();
//		return list.stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());
//	}
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
		Page<Product> list = productRepository.findAll(pageRequest);
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
		productEntity.setName(productDTO.getName());
		productEntity = productRepository.save(productEntity);
		return new ProductDTO(productEntity);
		
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO productDTO) {
		try {
			Product entity = productRepository.getOne(id);
			entity.setName(productDTO.getName());
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
	
	
	
	
	
}
