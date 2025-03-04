package com.smartcontact.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartcontact.entities.User;
import com.smartcontact.helpers.AppConstants;
import com.smartcontact.helpers.Helper;
import com.smartcontact.helpers.ResourceNotFoundException;
import com.smartcontact.repositories.UserRepository;
import com.smartcontact.services.EmailService;
import com.smartcontact.services.UserService;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailService emailService;

    //private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public User saveUser(User user) {
        //dynamic generation of userId
        String userId = UUID.randomUUID().toString(); //it will set a random userid
        user.setUserId(userId); 
        //password encode
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //set the user role
        
        user.setRoleList(List.of(AppConstants.ROLE_USER));

        //logger.info(user.getProviders().toString());
        

        String emailToken = UUID.randomUUID().toString();
        user.setEmailToken(emailToken);
        User savedUser =  userRepo.save(user);
        String emailLink = Helper.getLinkForEMailVerification(emailToken);
        emailService.sendEmail(savedUser.getEmail(), "Verify Account : SMART CONTACT MANAGER", emailLink);

        return savedUser;
    }

    @Override
    public User getUserById(String id) {
        return userRepo.findByUserId(id);
    }

    @Override
    public Optional<User>  updateUser(User user) {
        User us = userRepo.findById(user.getUserId()).orElseThrow(()->new ResourceNotFoundException("User not found"));
        //update us from user
        us.setName(user.getName());
        us.setEmail(user.getEmail());
        us.setPassword(user.getPassword());
        us.setAbout(user.getAbout());
        us.setPhone(user.getPhone());
        us.setProfilePic(user.getProfilePic());
        us.setEnabled(user.isEnabled());
        us.setEmailVerified(user.isEmailVerified());
        us.setPhoneverified(user.isPhoneverified());
        us.setProviders(user.getProviders());
        us.setProviderUserId(user.getProviderUserId());

        //save the user in database
        User save = userRepo.save(us);
        return Optional.ofNullable(save);
    }

   

    @Override
    public boolean isUserExist(String id) {
        User us = userRepo.findById(id).orElse(null);
        return us!=null ? true : false;

    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User us = userRepo.findByEmail(email).orElse(null);
        return us!=null ? true : false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public void deleteUser(String id) {
        User us = userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));
        userRepo.delete(us);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }

    @Override
    public User update(User user) {
        var userOld = userRepo.findById(user.getUserId()).orElseThrow(()-> new ResourceNotFoundException("Contact not found"));
        userOld.setName(user.getName());
        userOld.setEmail(user.getEmail());
        userOld.setPhone(user.getPhone());
        userOld.setAbout(user.getAbout());
        
        userOld.setEnabled(user.isEnabled());
        userOld.setEmailVerified(user.isEmailVerified());
        
        userOld.setProfilePic(user.getProfilePic());
        //userOld.setCloudinaryImagePublicid(contact.getCloudinaryImagePublicid());

        return userRepo.save(userOld);
    }

    

    

}
