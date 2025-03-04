package com.smartcontact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcontact.entities.User;
import com.smartcontact.repositories.UserRepository;

@Controller
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;
    //verify email
    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token){
        User user = userRepository.findByEmailToken(token).orElse(null);

        if(user!=null){
            if(user.getEmailToken().equals(token)){
                user.setEmailVerified(true);
                user.setEnabled(true);
                userRepository.save(user);
                return "success_page";
            }
            return "error_page";
        }
        return "error_page";
    }
}
