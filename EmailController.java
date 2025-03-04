package com.smartcontact.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartcontact.entities.Contacts;
import com.smartcontact.entities.Emails;
import com.smartcontact.entities.FileEntity;
import com.smartcontact.entities.User;
import com.smartcontact.helpers.AppConstants;
import com.smartcontact.helpers.Helper;
import com.smartcontact.helpers.Message;
import com.smartcontact.helpers.MessageType;
import com.smartcontact.services.ContactService;
import com.smartcontact.services.EmailService;
import com.smartcontact.services.FileService;
import com.smartcontact.services.UserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/user/emails")
public class EmailController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private FileService fileService;

    @PostMapping("/send/{id}")
    public String send(
        @PathVariable String id,
        @RequestParam String subject,
        @RequestParam String message,
        @RequestParam MultipartFile[] files,
        Authentication authentication,
        HttpSession session
    ) throws IOException, MessagingException{

        if(subject.isEmpty() || message.isEmpty()){
            session.setAttribute("message", Message.builder().content("You can't send empty email !!").type(MessageType.red).build());
            return "redirect:/user/contacts/emailform/"+id;
        }else{
            String useremail = Helper.getEmailOfLoggedInUser(authentication);
            User user = userService.getUserByEmail(useremail);
            Contacts contact = contactService.getById(id);
            
            String to = contact.getEmail();
            String emailid = UUID.randomUUID().toString();
            Emails email = new Emails();
            email.setSourceemail(useremail);
            email.setEmailid(emailid);
            email.setDestinationemail(contact.getEmail());
            email.setDestinationname(contact.getName());
            email.setSubject(subject);
            email.setMessage(message);
            email.setUser(user);
            List<FileEntity> filesSend = fileService.saveFiles(files);
            email.setFiles(filesSend);
            emailService.saveEmail(email);
            fileService.saveEmail(email);
            
            emailService.sendEmailWithAttachment(useremail,to, subject, message, filesSend);
            session.setAttribute("message", "Email sent successfully !!");
            return "redirect:/user/emails/sentEmails";
        }
        
    }


    @GetMapping("/sentEmails")
    public String SentEmails(
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="size", defaultValue = "10") int size,
        @RequestParam(value="sortBy",defaultValue = "destinationname") String sortBy,
        @RequestParam(value="direction", defaultValue = "asc") String direction,
        Model m, Authentication authentication
    ) {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        Page<Emails> pageEmails = emailService.getByUser(user,page,size,sortBy,direction);
        //System.out.println(pageEmails);
        
        m.addAttribute("pageEmails", pageEmails);
        m.addAttribute("number", pageEmails.getNumber());
        m.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        return "user/seeEmails";
    }

    @GetMapping("/deleteEmail/{id}")
    public String deleteEmail(@PathVariable String id,HttpSession session) {
        emailService.deleteEmail(id);
        session.setAttribute("message", Message.builder().content("Email is Deleted successfully !!").type(MessageType.green).build());
        return "redirect:/user/emails/sentEmails";
    }
    
    
}
