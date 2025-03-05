package com.smartcontact.services;

import java.util.List;

import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;

import com.smartcontact.entities.Contacts;
import com.smartcontact.entities.User;
// import com.smartcontact.forms.UserForm;

public interface ContactService {
    Contacts save(Contacts contact);
    Contacts update(Contacts contact);
    List<Contacts> getAll();
    //get contact by id
    Contacts getById(String id);
    //delete contact
    void delete(String id);

    //search contact
    Page<Contacts> searchByName(String name, int size, int page, String sortBy, String direction, User user);

    Page<Contacts> searchByEmail(String email, int size, int page, String sortBy, String direction, User user);

    Page<Contacts> searchByPhone(String phone, int size, int page, String sortBy, String direction, User user);

    //get contacts by user id
    List<Contacts> getByUserId(String userId);

    Page<Contacts> getByUser(User user, int page, int size, String sortBy, String direction);

    long getNumberOfContactsForUser(String userId);

    long getNumberOfFemaleContactsForUser(String userId);
    long getNumberOfMaleContactsForUser(String userId);
    long getNumberOfOtherContactsForUser(String userId);

    long getNumberOfFavContacts(String userId);

    // Page<Contacts> getFavContacts(User user, Pageable pageable);
    Contacts getByEmail(String email);
    Contacts getByPhone(String phone);
    Contacts getByUserAndEmail(User user, String email);
    Contacts getByUserAndPhone(User user, String phone);


}
