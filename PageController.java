package com.smartcontact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.smartcontact.entities.User;
import com.smartcontact.forms.ContactForm;
import com.smartcontact.forms.UserForm;
import com.smartcontact.helpers.Helper;
import com.smartcontact.helpers.Message;
import com.smartcontact.helpers.MessageType;
import com.smartcontact.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;






@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model m){
        // m.addAttribute("name", "Aritra Mondal");
        // m.addAttribute("college", "Future Institute of Technology");
        // m.addAttribute("designation", "Programmer analyst trainee Cognizant");
        return "home";
    }

    

    @GetMapping("/services")
    public String service() {
        return "services";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    
     
    @GetMapping("/contact")
    public String contactUs() {
        return "contactus";
    }
    

    @GetMapping("/signup")
    public String signup(Model m) {
        UserForm userForm = new UserForm();
       /*  userForm.setName("Aritra");
        userForm.setAbout("I am a good boy");
        userForm.setEmail("aritra@gmail.com");
        userForm.setPassword("1234");
        userForm.setPhone("8101017099");*/
        m.addAttribute("userForm", userForm);
        return "signup";
    }


    //processing register 
    @PostMapping("/do_register")
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult ,HttpSession session) {
        
        //fetch from data
        //System.out.println(userForm);

        //validate data
        if(bindingResult.hasErrors()){
            return "signup";
        }
        //save to database
        /*User user = User.builder().name(userForm.getName())
                    .email(userForm.getEmail())
                    .password(userForm.getPassword())
                    .about(userForm.getAbout())
                    .phone(userForm.getPhone())
                    .profilePic("https://thumbs.dreamstime.com/b/brunette-businessman-avatar-man-face-profile-icon-concept-online-support-service-male-cartoon-character-portrait-brunette-126956879.jpg").build();*/
        User userin = userService.getUserByEmail(userForm.getEmail());
        if(userin==null){
            User user = new User();
            user.setName(userForm.getName());
            
            user.setEmail(userForm.getEmail());
            user.setPassword(userForm.getPassword());
            user.setEnabled(false);
            user.setAbout(userForm.getAbout());
            user.setPhone(userForm.getPhone());
            user.setGender(userForm.getGender());
            
            //user.setProfilePic("https://png.pngtree.com/png-vector/20231019/ourmid/pngtree-user-profile-avatar-png-image_10211467.png");
    
            userService.saveUser(user);
    
            //message registration successfull
    
            //creating the dynamic message
            Message message = Message.builder().content("Registration Successful").type(MessageType.green).build();
            session.setAttribute("message",message);
        }else{
            Message message = Message.builder().content("User already exists").type(MessageType.red).build();
            session.setAttribute("message",message);
        }     
       
        return "redirect:/signup";
    }

    
    
    
    
    
}
