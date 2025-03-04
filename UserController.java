package com.smartcontact.controller;



import java.util.Random;
import java.util.UUID;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcontact.entities.User;
import com.smartcontact.forms.UserFormCheck;
import com.smartcontact.helpers.Helper;
import com.smartcontact.helpers.Message;
import com.smartcontact.helpers.MessageType;
import com.smartcontact.repositories.UserRepository;
import com.smartcontact.services.EmailService;
import com.smartcontact.services.ImageService;
import com.smartcontact.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;








@Controller
@RequestMapping("/user")
public class UserController {

    // private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

   @Autowired
    private ImageService imageService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    
    //user dashboard page
    @GetMapping("/dashboard")
    public String userDashboard(){
        return "user/dashboard";
    }


    @GetMapping("/profile")
    public String profile(Model m,Authentication authentication) {
        return "user/profile";
    }
    
    
    //user add contact

    //user view contact 

    //user edit contact
    @GetMapping("/edituser")
    public String updateUserView(Model m, Authentication authentication){

        if(authentication==null){
            return null;
        }
        //System.out.println("Adding loggedin user information");
        String username = Helper.getEmailOfLoggedInUser(authentication);
        //logger.info("User logged in: {}",username);
        User user = userService.getUserByEmail(username);
        m.addAttribute("userForm", user);
        m.addAttribute("userid", user.getUserId());
        return "user/edituserprofile";
    }


    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable String id,@Valid @ModelAttribute UserFormCheck userForm,BindingResult bresult, HttpSession session) {
        //form validation
        
        User user = userService.getUserById(id);
        user.setUserId(id);
        
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setAbout(userForm.getAbout());
        user.setGender(userForm.getGender());
        user.setPhone(userForm.getPhone());
        if(userForm.getProfilePic()!=null && !userForm.getProfilePic().isEmpty()){
            String filename = UUID.randomUUID().toString();
            String imageurl = imageService.uploadImage(userForm.getProfilePic(), filename);
            user.setCloudinaryImagePublicid(filename);
            user.setProfilePic(imageurl);
            userForm.setPictureUrl(imageurl);
        }

        userService.update(user);
        //session.setAttribute("message",Message.builder().content("Profile updated").type(MessageType.green).build());
        
        
        return "redirect:/user/profile";
    }
    
    //user delete profile
    @GetMapping("/delete/{id}")
    public String deleteProfile(@PathVariable String id,HttpSession session) {
        userService.deleteUser(id);
        session.setAttribute("deletemessage", "Profile deleted successfully");
        return "redirect:/logout";
    }


    

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,Authentication authentication, HttpSession session) {
        //TODO: process POST request
        // logger.info(oldPassword);
        // logger.info(newPassword);
        if(authentication==null){
            return null;
        }
        if(oldPassword.isEmpty() || newPassword.isEmpty()){
            Message message = Message.builder().content("password fields can't be empty").type(MessageType.red).build();
            session.setAttribute("message",message);
            return "user/profile";
        }else{
            //System.out.println("Adding loggedin user information");
            String username = Helper.getEmailOfLoggedInUser(authentication);
            //logger.info("User logged in: {}",username);
            User user = userService.getUserByEmail(username);
            String user_password = user.getPassword();
            if(this.bCryptPasswordEncoder.matches(oldPassword, user_password)){
                //change the password
                user.setPassword(bCryptPasswordEncoder.encode(newPassword));
                userRepository.save(user);
                Message message = Message.builder().content("Password changed successfully").type(MessageType.green).build();
                session.setAttribute("message",message);
            }else{
                Message message = Message.builder().content("Old password is incorrect").type(MessageType.red).build();
                session.setAttribute("message",message);
            }
            return "user/profile";
        }
        
    }

    @PostMapping("/sendotp")
    public String getMethodName(Authentication authentication) {
        String user_email = Helper.getEmailOfLoggedInUser(authentication);
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        String otpstr = String.valueOf(otp);
        String subject= "OTP for changing password";
        String message = "To change your password use this OTP:"+otpstr;
        emailService.sendEmail(user_email, subject, message);
        User user = userService.getUserByEmail(user_email);
        user.setOtp(otpstr);
        userRepository.save(user);
        return "user/otpform";
    }

    @PostMapping("/verifyotp")
    public String verifyOtp(@RequestParam("first") String first, @RequestParam("second") String second, @RequestParam("third") String third, @RequestParam("fourth") String fourth,HttpSession session,Authentication authentication) {
        String user_email = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(user_email);
        String otp = user.getOtp();

        String full_otp = first+second+third+fourth;
        if(full_otp.equals(otp)){
            Message message = Message.builder().content("OTP verified").type(MessageType.green).build();
            session.setAttribute("message",message);
            return "user/changepassword";
        
        }
        Message message = Message.builder().content("OTP is incorrect").type(MessageType.red).build();
        session.setAttribute("message", message);
        return "user/profile";
    }

    @PostMapping("/verifypassword")
    public String verifyPassword(@RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword, HttpSession session,Authentication authentication) {
        if(!newPassword.isEmpty() && !confirmPassword.isEmpty()){
            if(newPassword.length()>=6 && confirmPassword.length()>=6){
                String user_email = Helper.getEmailOfLoggedInUser(authentication);
                User user = userService.getUserByEmail(user_email);
                String user_password = user.getPassword();
                if(!this.bCryptPasswordEncoder.matches(newPassword, user_password)){
                    if(newPassword.equals(confirmPassword)){
                    
                        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
                        userRepository.save(user);
                        Message message = Message.builder().content("Password changed successfully").type(MessageType.green).build();
                        session.setAttribute("message", message );
                        return "redirect:/user/profile";
                    }else{
                        Message message = Message.builder().content("Password do not match").type(MessageType.red).build();
                        session.setAttribute("message", message);
                        return "user/changepassword";
                    }
                }else{
                    Message message = Message.builder().content("You can't put your old password as new password").type(MessageType.red).build();
                    session.setAttribute("message", message);
                    return "redirect:/user/profile";
                }
            }else{
                Message message = Message.builder().content("Password must be at least 6 characters long").type(MessageType.red).build();
                session.setAttribute("message", message);
                return "user/changepassword";
            }
            
        }
        else{
            Message message = Message.builder().content("Password fields cannot be empty").type(MessageType.red).build();
            session.setAttribute("message", message);
            return "user/changepassword";
        }
        
        
       
    }
    
    
    
    
    
    
    
}

