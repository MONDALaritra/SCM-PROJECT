package com.smartcontact.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.smartcontact.entities.Contacts;
import com.smartcontact.entities.User;
// import com.smartcontact.forms.ContactForm;
// import com.smartcontact.forms.UserForm;
import com.smartcontact.helpers.ResourceNotFoundException;
import com.smartcontact.repositories.ContactRepository;
import com.smartcontact.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Contacts save(Contacts contact) {
        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);
        return contactRepository.save(contact);
    }

    @Override
    public Contacts update(Contacts contact) {
        var contactOld = contactRepository.findById(contact.getId()).orElseThrow(()-> new ResourceNotFoundException("Contact not found"));
        contactOld.setName(contact.getName());
        contactOld.setEmail(contact.getEmail());
        contactOld.setPhone(contact.getPhone());
        contactOld.setAddress(contact.getAddress());
        contactOld.setDescription(contact.getDescription());
        contactOld.setFavourite(contact.isFavourite());
        contactOld.setLinkedinlink(contact.getLinkedinlink());
        contactOld.setWebsiteLink(contact.getWebsiteLink());
        contactOld.setPicture(contact.getPicture());
        contactOld.setCloudinaryImagePublicid(contact.getCloudinaryImagePublicid());

        return contactRepository.save(contactOld);
    }

    @Override
    public List<Contacts> getAll() {
        return contactRepository.findAll();
    }

    @Override
    public Contacts getById(String id) {
        return contactRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact not found with given id "+id));
    }

    @Override
    public void delete(String id) {
        var contact = contactRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact not found with given id "+id));
        contactRepository.delete(contact);
    }


    @Override
    public List<Contacts> getByUserId(String userId) {
       return contactRepository.findByUserId(userId);
    }

    @Override
    public Page<Contacts> getByUser(User user, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page,size,sort);

        
        return contactRepository.findByUser(user,pageable);
    }

    @Override
    public Page<Contacts> searchByName(String name, int size, int page, String sortBy, String direction, User user) {
        Sort sort = direction.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable= PageRequest.of(page,size,sort);
        return contactRepository.findByUserAndNameContaining( user,name,pageable);
    }

    @Override
    public Page<Contacts> searchByEmail(String email, int size, int page, String sortBy, String direction, User user) {
        Sort sort = direction.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable= PageRequest.of(page,size,sort);
        return contactRepository.findByUserAndEmailContaining( user,email,pageable);
    }

    @Override
    public Page<Contacts> searchByPhone(String phone, int size, int page, String sortBy, String direction, User user) {
        Sort sort = direction.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable= PageRequest.of(page,size,sort);
        return contactRepository.findByUserAndPhoneContaining( user,phone, pageable);
    }

    @Override
    public long getNumberOfContactsForUser(String userId) {
        return contactRepository.countByUserId(userId);
    }

    @Override
    public long getNumberOfFemaleContactsForUser(String userId) {
        return contactRepository.countFemaleContacts(userId);
    }

    @Override
    public long getNumberOfMaleContactsForUser(String userId) {
        return contactRepository.countMaleContacts(userId);
    }

    @Override
    public long getNumberOfOtherContactsForUser(String userId) {
        return contactRepository.countOtherContacts(userId);
    }

    @Override
    public long getNumberOfFavContacts(String userId) {
        return contactRepository.countFavContacts(userId);
    }

    @Override
    public Contacts getByEmail(String email) {
        return contactRepository.findByEmail(email);
    }

    @Override
    public Contacts getByPhone(String phone) {
        return contactRepository.findByPhone(phone);
    }

    // @Override
    // public Page<Contacts> getFavContacts(User user, Pageable pageable,ContactForm contactForm) {
    //     if(contactForm.isFavourite()){
    //         return contactRepository.findByUser(user, pageable);
    //     }
    //     return null;
    // }

    

}
