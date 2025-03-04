package com.smartcontact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcontact.entities.Contacts;
import com.smartcontact.entities.Emails;
import com.smartcontact.services.ContactService;
import com.smartcontact.services.EmailService;

import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private EmailService emailService;

    //get contact
    @GetMapping("/contacts/{contactId}")
    public Contacts getContact(@PathVariable String contactId){
        return contactService.getById(contactId);
    }   

    //get email
    @GetMapping("/email/{emailid}")
    public Emails getEmail(@PathVariable String emailid) {
        return emailService.getEmailById(emailid);
    }
    


}
