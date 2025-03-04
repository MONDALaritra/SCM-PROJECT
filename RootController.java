package com.smartcontact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.smartcontact.entities.User;
import com.smartcontact.helpers.Helper;
import com.smartcontact.services.ContactService;
import com.smartcontact.services.EmailService;
import com.smartcontact.services.UserService;

@ControllerAdvice
public class RootController {

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private EmailService emailService;
    
     @ModelAttribute
    public void addLoggedInUserInformation(Model m, Authentication authentication){
        if(authentication==null){
            return;
        }
        //System.out.println("Adding loggedin user information");
        String username = Helper.getEmailOfLoggedInUser(authentication);
        //logger.info("User logged in: {}",username);
        User user = userService.getUserByEmail(username);
        // System.out.println(user);
        // System.out.println(user.getEmail());
        // System.out.println(user.getName());
        long number_of_contacts = contactService.getNumberOfContactsForUser(user.getUserId());
        long number_of_female_contacts = contactService.getNumberOfFemaleContactsForUser(user.getUserId());
        long number_of_male_contacts = contactService.getNumberOfMaleContactsForUser(user.getUserId());
        long number_of_other_gender_contacts = contactService.getNumberOfOtherContactsForUser(user.getUserId());
        long number_of_fav_contacts = contactService.getNumberOfFavContacts(user.getUserId());
        long number_of_emails = emailService.countEmails(user.getUserId());
        //System.out.println(user.getGender());
        m.addAttribute("loggedinUser", user);
        m.addAttribute("loggedinuserid", user.getUserId());
        m.addAttribute("userGender", user.getGender());
        m.addAttribute("numberofContacts", number_of_contacts);
        m.addAttribute("numberofMales", number_of_male_contacts);
        m.addAttribute("numberofFemales", number_of_female_contacts);
        m.addAttribute("numberofOtherContacts", number_of_other_gender_contacts);
        m.addAttribute("numberoffavContacts", number_of_fav_contacts);
        m.addAttribute("numberofEmails", number_of_emails);
        //m.addAttribute("loggedinUser", user)

    }

}   
