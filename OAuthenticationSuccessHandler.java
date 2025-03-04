package com.smartcontact.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.smartcontact.entities.Providers;
import com.smartcontact.entities.User;
import com.smartcontact.helpers.AppConstants;
import com.smartcontact.repositories.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

    Logger logger = LoggerFactory.getLogger(OAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepository userRepo;

    @SuppressWarnings("null")
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
            logger.info("OAuthenticationSuccessHandler");

            DefaultOAuth2User user = (DefaultOAuth2User)authentication.getPrincipal();
            //System.out.println(user.getAttribute("email").toString());
            //identify the provider

            var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

            String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
            User us = new User();
            us.setUserId(UUID.randomUUID().toString());
            us.setRoleList(List.of(AppConstants.ROLE_USER));
            us.setEnabled(true);

            if(authorizedClientRegistrationId.equalsIgnoreCase("google")){
                //google
                //save data on database 

            
                String email = user.getAttribute("email").toString();

                String name = user.getAttribute("name").toString();

                String picture = user.getAttribute("picture").toString();

                //create user and save in database

                
                us.setEmail(email);
                us.setName(name);
                us.setProfilePic(picture);
                us.setPassword("password_google");
                
                us.setProviders(Providers.GOOGLE);
                
                us.setEmailVerified(true);
                us.setProviderUserId(user.getName());
                
                us.setAbout("This account is created using google...");

                

            }else if(authorizedClientRegistrationId.equalsIgnoreCase("github")){
                //github
                String email = user.getAttribute("email") != null ? user.getAttribute("email").toString() : user.getAttribute("login").toString()+"@gmail.com";
                String picture = user.getAttribute("avatar_url").toString();
                String name = user.getAttribute("login").toString();
                String providerUserId = user.getName();

                us.setEmail(email);
                us.setProfilePic(picture);
                us.setName(name);
                us.setProviderUserId(providerUserId);
                us.setProviders(Providers.GITHUB);
                us.setAbout("This account is created using github...");
                us.setPassword("password_github");
                us.setEmailVerified(true);
            }
            
            User user2 = userRepo.findByEmail(us.getEmail()).orElse(null);
            if(user2==null){
                userRepo.save(us);
                
            }
            new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");
    }

}
