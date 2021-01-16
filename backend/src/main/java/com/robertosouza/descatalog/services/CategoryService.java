package com.robertosouza.descatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.robertosouza.descatalog.dto.CategoryDTO;
import com.robertosouza.descatalog.entities.Category;
import com.robertosouza.descatalog.repositories.CategoryRepository;
import com.robertosouza.descatalog.services.exceptions.EntityNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List<Category> list = categoryRepository.findAll();
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> categoryOptional = categoryRepository.findById(id);
		Category categoryEntity = categoryOptional.orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
		return new CategoryDTO(categoryEntity);
		
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO categoryDTO) {
		Category categoryEntity = new Category();
		categoryEntity.setName(categoryDTO.getName());
		categoryEntity = categoryRepository.save(categoryEntity);
		return new CategoryDTO(categoryEntity);
		
	}
	
	
	
	
	
}
