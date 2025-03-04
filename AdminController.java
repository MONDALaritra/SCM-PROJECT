package com.smartcontact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcontact.entities.Feedback;
import com.smartcontact.services.FeedbackService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/send-message")
    public String sendFeedback(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("feedback") String message, HttpSession session
) {
        //TODO: process POST request
        
        Feedback feedback = new Feedback();
        feedback.setEmail(email);
        feedback.setName(name);
        feedback.setMessage(message);
        feedbackService.saveFeedback(feedback);

        session.setAttribute("message", "Thanks for your valuable message");
        return "redirect:/contact";
    }
    
}
