package com.smartcontact.helpers;




import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import org.springframework.security.oauth2.core.user.DefaultOAuth2User;




public class Helper {
    
    
    @SuppressWarnings("null")
    public static String getEmailOfLoggedInUser(Authentication authentication){

        //Principal principal =(Principal)authentication.getPrincipal();

        
        if(authentication instanceof OAuth2AuthenticationToken){
            var oauth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
            String clientId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

            DefaultOAuth2User oauth2USer = (DefaultOAuth2User)authentication.getPrincipal();
            String username="";
            if(clientId.equalsIgnoreCase("google")){
                //if user is logged in by google

                //System.out.println("Inside the google");
                username = oauth2USer.getAttribute("email").toString();
            }else if(clientId.equalsIgnoreCase("github")){
                //System.out.println("Inside github");
                //if user is logged in by github
                username = oauth2USer.getAttribute("email") != null ? oauth2USer.getAttribute("email").toString() : oauth2USer.getAttribute("login").toString()+"@gmail.com";


            }
            return username;
        }
        else{
            //if user is mannually loggedin
            //System.out.println("it is generating from database");
            return authentication.getName();
         }   
        
    }


    public static String getLinkForEMailVerification(String emailToken){
        String link = "http://localhost:8080/auth/verify-email?token="+ emailToken;
        return link;
    }
}
