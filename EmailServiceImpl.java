package com.smartcontact.services.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import com.smartcontact.entities.Emails;
import com.smartcontact.entities.FileEntity;
import com.smartcontact.entities.User;
import com.smartcontact.repositories.EmailRepository;

import com.smartcontact.services.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailRepository emailRepository;

    int sentemails = 0;
    
    @Override
    public void sendEmail(String to, String subject, String message) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        simpleMailMessage.setFrom("aritramondal7099@gmail.com");
        mailSender.send(simpleMailMessage);
    }
    @Override
    public void sendEmail(String[] to, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        simpleMailMessage.setFrom("aritramondal7099@gmail.com");
        mailSender.send(simpleMailMessage);
    }
    @Override
    public void sendEmailWithHtml(String to, String subject, String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("aritramondal7099@gmail.com");
            helper.setText(htmlContent, true);
            mailSender.send(message);
            

        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    public void sendEmailWithFIle(String to, String subject, String message, File file) {
         MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("aritramondal7099@gmail.com");
            helper.setText(message);
            FileSystemResource fileSystemResource = new FileSystemResource(file);
            helper.addAttachment(fileSystemResource.getFilename(),file  );
            mailSender.send(mimeMessage);
            

        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    public void sendEmailWithFIle(String to, String subject, String message, InputStream is) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("aritramondal7099@gmail.com");
            helper.setText(message,true);
            File file = new File("src/main/resources/email/test.png");
            Files.copy(is,file.toPath(),StandardCopyOption.REPLACE_EXISTING);
            FileSystemResource fileSystemResource = new FileSystemResource(file);
            helper.addAttachment(fileSystemResource.getFilename(), file);
            mailSender.send(mimeMessage);
            

        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    public Emails saveEmail(Emails email) {
        
        return emailRepository.save(email);
    }

    @SuppressWarnings("null")
    @Override
    public void sendEmailWithAttachment(String from, String to, String subject, String text, List<FileEntity> file) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        

        
        if (!file.isEmpty()) {
            for (FileEntity fileEntity : file) {
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(text);
                InputStreamSource attachmentSource = new InputStreamSource() {
                    @Override
                    public InputStream getInputStream() throws IOException {
                        return new java.io.ByteArrayInputStream(fileEntity.getData());
                    }
                };
                helper.addAttachment(fileEntity.getFileName(), attachmentSource);
            }
            mailSender.send(message);
        }else{
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            mailSender.send(message);
        }

        
    }
    @Override
    public Page<Emails> getByUser(User user, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page,size,sort);
        return emailRepository.findByUser(user,pageable);
    }
    @Override
    public void deleteEmail(String id) {
        Emails email = emailRepository.findById(id).get();
        emailRepository.delete(email);
    }
    @Override
    public long countEmails(String id) {
        return emailRepository.countByUserId(id);
    }
    @Override
    public Emails getEmailById(String emailid) {
        return emailRepository.findByEmailid(emailid);
    }
   

}
