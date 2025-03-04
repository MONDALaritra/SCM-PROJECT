package com.smartcontact.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.smartcontact.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,String>{
    //extra methods for db related operations
    //custom query methods
    //custom finfding methods

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailToken(String token);

    
    User findByUserId(String id);

    
}
