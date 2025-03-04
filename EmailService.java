package com.smartcontact.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.data.domain.Page;

import com.smartcontact.entities.Emails;
import com.smartcontact.entities.FileEntity;
import com.smartcontact.entities.User;

import jakarta.mail.MessagingException;

public interface EmailService {
    //send email to single person
    void sendEmail(String to, String subject, String message);

    //send email to multiple person
    void sendEmail(String []to, String subject, String message);

    //send email with html
    void sendEmailWithHtml(String to, String subject, String htmlContent);

    //email with file
    void sendEmailWithFIle(String to, String subject, String message, File file);
        
    void sendEmailWithFIle(String to, String subject, String message, InputStream is);

    
    void sendEmailWithAttachment(String from, String to, String subject, String text, List<FileEntity> file) throws MessagingException, IOException;

    Emails saveEmail(Emails email);

    Page<Emails> getByUser(User user, int page, int size, String sortBy, String direction);

    void deleteEmail(String id);

    long countEmails(String id);

    Emails getEmailById(String emailid);
}
