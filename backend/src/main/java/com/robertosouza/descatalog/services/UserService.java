package com.robertosouza.descatalog.services;


import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.robertosouza.descatalog.dto.RoleDTO;
import com.robertosouza.descatalog.dto.UserDTO;
import com.robertosouza.descatalog.dto.UserInsertDTO;
import com.robertosouza.descatalog.entities.Role;
import com.robertosouza.descatalog.entities.User;
import com.robertosouza.descatalog.repositories.RoleRepository;
import com.robertosouza.descatalog.repositories.UserRepository;
import com.robertosouza.descatalog.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	
//	@Transactional(readOnly = true)
//	public List<UserDTO> findAll(){
//		List<User> list = userRepository.findAll();
//		return list.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
//	}
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(PageRequest pageRequest){
		Page<User> list = userRepository.findAll(pageRequest);
		return list.map(x -> new UserDTO(x));
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> userOptional = userRepository.findById(id);
		User userEntity = userOptional.orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
		return new UserDTO(userEntity);
		
	}

	@Transactional
	public UserDTO insert(UserInsertDTO userDTO) {
		User userEntity = new User();
		copyDtoToEntity(userDTO, userEntity);
		userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		//userEntity.setName(userDTO.getName());
		userEntity = userRepository.save(userEntity);
		return new UserDTO(userEntity);
		
	}

	

	@Transactional
	public UserDTO update(Long id, UserDTO userDTO) {
		try {
			User entity = userRepository.getOne(id);
			copyDtoToEntity(userDTO, entity);
			//entity.setName(userDTO.getName());
			entity = userRepository.save(entity);
			return new UserDTO(entity);
		} catch(EntityNotFoundException e ) {
			throw new ResourceNotFoundException("Id não encontrado " + id);
		}
	}

	public void delete(Long id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id não encontrado " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("Violação de integridade");
		}
	}
	
	private void copyDtoToEntity(UserDTO userDTO, User userEntity) {
		userEntity.setFirstName(userDTO.getFirstName());
		userEntity.setLastName(userDTO.getLastName());
		userEntity.setEmail(userDTO.getEmail());
		
		userEntity.getRoles().clear();
		
		for(RoleDTO roleDto: userDTO.getRoles()) {
			Role role = roleRepository.getOne(roleDto.getId());
			userEntity.getRoles().add(role);
		}
		
	}
	
	
	
}
