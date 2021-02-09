package com.robertosouza.descatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.robertosouza.descatalog.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

}
