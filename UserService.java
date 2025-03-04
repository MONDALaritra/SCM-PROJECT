package com.smartcontact.services;

import java.util.List;
import java.util.Optional;

import com.smartcontact.entities.User;

public interface UserService {
    User saveUser(User user);
    User getUserById(String id);
    Optional<User> updateUser(User user);
    User getUserByEmail(String email);
    boolean isUserExist(String id);
    boolean isUserExistByEmail(String email);
    List<User> getAllUsers();
    void deleteUser(String id);
    User update(User user); 
    //add methods related to service
}
