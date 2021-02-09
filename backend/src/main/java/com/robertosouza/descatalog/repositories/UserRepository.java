package com.robertosouza.descatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.robertosouza.descatalog.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
