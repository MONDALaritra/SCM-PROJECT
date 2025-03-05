package com.smartcontact.controller;



import java.util.UUID;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcontact.entities.Contacts;
import com.smartcontact.entities.User;
import com.smartcontact.forms.ContactForm;
import com.smartcontact.forms.ContactSearchForm;
import com.smartcontact.helpers.AppConstants;
import com.smartcontact.helpers.Helper;
import com.smartcontact.helpers.Message;
import com.smartcontact.helpers.MessageType;

import com.smartcontact.services.ImageService;
import com.smartcontact.services.ContactService;
import com.smartcontact.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    //private Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    //add contact page
    @GetMapping("/add")
    public String addContactView(Model m){
        ContactForm contactForm = new ContactForm();
        m.addAttribute("contactForm", contactForm);
        return "user/addcontact";
    }

    @PostMapping("/add")
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm,BindingResult result, Authentication authentication, HttpSession session){

        //form validation
        if(result.hasErrors()){
            session.setAttribute("message", Message.builder()
            .content("Please correct the following errors") 
            .type(MessageType.red)  
            .build());
            return "user/addcontact";
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(username);

        //picture process
        
        //code for upload image
        Contacts contact1 = contactService.getByUserAndEmail(user,contactForm.getEmail());
        Contacts contact2 = contactService.getByUserAndPhone(user,contactForm.getPhone());
        if(contact1==null && contact2==null){
            Contacts contact = new Contacts();
            contact.setName(contactForm.getName());
            contact.setFavourite(contactForm.isFavourite());
            contact.setEmail(contactForm.getEmail());
            contact.setPhone(contactForm.getPhone());
            contact.setAddress(contactForm.getAddress());
            contact.setDescription(contactForm.getDescription());
            contact.setWebsiteLink(contactForm.getWebsiteLink());
            contact.setLinkedinlink(contactForm.getLinkedinlink());
            contact.setGender(contactForm.getGender());
            if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
                String filename = UUID.randomUUID().toString();
                String fileURL= imageService.uploadImage(contactForm.getContactImage(),filename);
                contact.setPicture(fileURL);
                contact.setCloudinaryImagePublicid(filename);
            }
            
            contact.setUser(user);
            //process the data

            contactService.save(contact);
            //System.out.println(contactForm);

            //set the contact picture url

            //set the message for display only
            session.setAttribute("message", Message.builder()
            .content("You have successfully added a new contact") 
            .type(MessageType.green)  
            .build());
        }else{
            session.setAttribute("message", Message.builder()
            .content("Contact with this email or phone number is already there") 
            .type(MessageType.red)  
            .build());
        }

        
        return "redirect:/user/contacts/add";
    }


    //view contacts

    @RequestMapping
    public String viewContacts(
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="size", defaultValue = "6") int size,
        @RequestParam(value="sortBy",defaultValue = "name") String sortBy,
        @RequestParam(value="direction", defaultValue = "asc") String direction,
        Model m, Authentication authentication){

        //load all the user contacts
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contacts> pagecontacts = contactService.getByUser(user,page,size,sortBy,direction);

        
        m.addAttribute("pagecontacts", pagecontacts);
        m.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        m.addAttribute("contactSearchForm", new ContactSearchForm());
        return "user/contacts";
    }


    //search handler
    @GetMapping("/search")
    public String searchHandler(@ModelAttribute ContactSearchForm contactSearchForm,
        @RequestParam(value="size",defaultValue = AppConstants.PAGE_SIZE+" ") int size,
        @RequestParam(value="page",defaultValue = "0") int page,
        @RequestParam(value="sortBy", defaultValue="name") String sortBy,
        @RequestParam(value="direction", defaultValue="asc") String direction, Model m,
        Authentication authentication
    ){

        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));
        Page<Contacts> pageContact = null;
        if(contactSearchForm.getField().equalsIgnoreCase("name")){
            pageContact =  contactService.searchByName(contactSearchForm.getKeyword(), size, page, sortBy, direction,user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("email")){
           pageContact =  contactService.searchByEmail(contactSearchForm.getKeyword(), size, page, sortBy, direction,user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("phone")){
            pageContact =  contactService.searchByPhone(contactSearchForm.getKeyword(), size, page, sortBy, direction,user);
        }

        m.addAttribute("pagecontacts", pageContact);
        m.addAttribute("contactSearchForm", contactSearchForm);
        m.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        return "user/search";
    }



    //delete contacts handler
    @GetMapping("/delete/{id}")
    public String deleteContact(@PathVariable String id, HttpSession session) {
        contactService.delete(id);
        session.setAttribute("message", Message.builder().content("Contact is Deleted successfully !!").type(MessageType.green).build());
        return "redirect:/user/contacts";
    }

    //update contact form view
    @GetMapping("/view/{id}")
    public String updateContactFormView(@PathVariable String id, Model m){
        var contact = contactService.getById(id);

        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhone(contact.getPhone());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavourite(contact.isFavourite());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedinlink(contact.getLinkedinlink());
        contactForm.setPicture(contact.getPicture());
        contactForm.setGender(contact.getGender());
        String contactId = contact.getId();
        m.addAttribute("contactForm", contactForm);
        m.addAttribute("contactId", contactId);
        return "user/update_contact_view";
    }


    @PostMapping("/update/{id}")
    public String updateContact(@PathVariable String id,@Valid @ModelAttribute ContactForm contactForm,BindingResult result, HttpSession session) {
        

        //form validation
        if(result.hasErrors()){
            session.setAttribute("message", Message.builder()
            .content("Please correct the following errors") 
            .type(MessageType.red)  
            .build());
            return "user/update_contact_view";
        }
        

        //update operation
       var contact = contactService.getById(id);
       contact.setId(id);
       contact.setName(contactForm.getName());
       contact.setEmail(contactForm.getEmail());
       contact.setPhone(contactForm.getPhone());
       contact.setAddress(contactForm.getAddress());
       contact.setGender(contactForm.getGender());
       contact.setDescription(contactForm.getDescription());
       contact.setLinkedinlink(contactForm.getLinkedinlink());
       contact.setWebsiteLink(contactForm.getWebsiteLink());
        //image process 
       if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
            String filename = UUID.randomUUID().toString();
            String imageurl = imageService.uploadImage(contactForm.getContactImage(), filename);
            contact.setCloudinaryImagePublicid(filename);
            contact.setPicture(imageurl);
            contactForm.setPicture(imageurl);
       }
       
        

       contact.setFavourite(contactForm.isFavourite());

       contactService.update(contact);
       session.setAttribute("message",Message.builder().content("Contact updated").type(MessageType.green).build());
        return "redirect:/user/contacts/view/"+id;
    }
    
    @GetMapping("/favourite-contacts")
    public String favContacts(
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="size", defaultValue = "10") int size,
        @RequestParam(value="sortBy",defaultValue = "name") String sortBy,
        @RequestParam(value="direction", defaultValue = "asc") String direction,
        Model m, Authentication authentication
    ) {

        //load all the user contacts
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contacts> pagecontacts = contactService.getByUser(user,page,size,sortBy,direction);

        
        m.addAttribute("pagecontacts", pagecontacts);
        m.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        m.addAttribute("contactSearchForm", new ContactSearchForm());
       
        return "user/favcontacts";
    }
    
    @GetMapping("/male_contacts")
    public String maleContacts(
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="size", defaultValue = "10") int size,
        @RequestParam(value="sortBy",defaultValue = "name") String sortBy,
        @RequestParam(value="direction", defaultValue = "asc") String direction,
        Model m, Authentication authentication
    ) {
        //load all the user contacts
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contacts> pagecontacts = contactService.getByUser(user,page,size,sortBy,direction);

        
        m.addAttribute("pagecontacts", pagecontacts);
        m.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        m.addAttribute("contactSearchForm", new ContactSearchForm());
       
        return "user/malecontacts";
    }

    @GetMapping("/female_contacts")
    public String femaleContacts(
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="size", defaultValue = "10") int size,
        @RequestParam(value="sortBy",defaultValue = "name") String sortBy,
        @RequestParam(value="direction", defaultValue = "asc") String direction,
        Model m, Authentication authentication
    ) {
        //load all the user contacts
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contacts> pagecontacts = contactService.getByUser(user,page,size,sortBy,direction);

        
        m.addAttribute("pagecontacts", pagecontacts);
        m.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        m.addAttribute("contactSearchForm", new ContactSearchForm());
       
        return "user/femalecontacts";
    }


    @GetMapping("/emailcontacts")
    public String viewContactsEmail(
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="size", defaultValue = "6") int size,
        @RequestParam(value="sortBy",defaultValue = "name") String sortBy,
        @RequestParam(value="direction", defaultValue = "asc") String direction,
        Model m, Authentication authentication){

        //load all the user contacts
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contacts> pagecontacts = contactService.getByUser(user,page,size,sortBy,direction);

        
        m.addAttribute("pagecontacts", pagecontacts);
        m.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        m.addAttribute("contactSearchForm", new ContactSearchForm());
        return "user/emailcontact";
    }

    @GetMapping("/emailform/{id}")
    public String mailSenderForm(@PathVariable String id,Model m) {
        var contact = contactService.getById(id);
        m.addAttribute("contactdetails",contact);
        return "user/sendemail";
    }

    @GetMapping("/emailcontacts/search")
    public String searchHandlerEmail(@ModelAttribute ContactSearchForm contactSearchForm,
        @RequestParam(value="size",defaultValue = AppConstants.PAGE_SIZE+" ") int size,
        @RequestParam(value="page",defaultValue = "0") int page,
        @RequestParam(value="sortBy", defaultValue="name") String sortBy,
        @RequestParam(value="direction", defaultValue="asc") String direction, Model m,
        Authentication authentication
    ){

        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));
        Page<Contacts> pageContact = null;
        if(contactSearchForm.getField().equalsIgnoreCase("name")){
            pageContact =  contactService.searchByName(contactSearchForm.getKeyword(), size, page, sortBy, direction,user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("email")){
           pageContact =  contactService.searchByEmail(contactSearchForm.getKeyword(), size, page, sortBy, direction,user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("phone")){
            pageContact =  contactService.searchByPhone(contactSearchForm.getKeyword(), size, page, sortBy, direction,user);
        }

        m.addAttribute("pagecontacts", pageContact);
        m.addAttribute("contactSearchForm", contactSearchForm);
        m.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        return "user/searchcontactforEmail";
    }


    @GetMapping("/other_contacts")
    public String otherContacts(
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="size", defaultValue = "10") int size,
        @RequestParam(value="sortBy",defaultValue = "name") String sortBy,
        @RequestParam(value="direction", defaultValue = "asc") String direction,
        Model m, Authentication authentication
    ) {
        //load all the user contacts
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contacts> pagecontacts = contactService.getByUser(user,page,size,sortBy,direction);

        
        m.addAttribute("pagecontacts", pagecontacts);
        m.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        m.addAttribute("contactSearchForm", new ContactSearchForm());
       
        return "user/othercontacts";
    }
    
}
