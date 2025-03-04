package com.smartcontact.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartcontact.entities.Feedback;
import com.smartcontact.repositories.FeedbackRepository;
import com.smartcontact.services.FeedbackService;

@Service
public class FeedbackServiceImpl implements FeedbackService{


    @Autowired
    private FeedbackRepository feedbackRepository;

    @Override
    public void saveFeedback(Feedback feedback) {
        feedbackRepository.save(feedback);
    }

}
