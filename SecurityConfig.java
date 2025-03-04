package com.smartcontact.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.smartcontact.helpers.Message;
import com.smartcontact.helpers.MessageType;
import com.smartcontact.services.impl.SecurityCustomUserDetailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Configuration
public class SecurityConfig {

    //user create and login using java code with in memory service
    /*@Bean
    public UserDetailsService userDetailsService(){

        
        UserDetails user1 = User
                            .withDefaultPasswordEncoder()
                            .username("admin123")
                            .password("admin123")
                            .roles("ADMIN","USER")
                            .build();

        //UserDetails user2 =  User.withUsername("user123").password("user123").build();

        var inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user1);
        return inMemoryUserDetailsManager;
    }*/

    @Autowired
    private SecurityCustomUserDetailService userDetailService;

    @Autowired
    private OAuthenticationSuccessHandler handler;

    //configuration of authentication provider spring security
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider =  new DaoAuthenticationProvider();

        //user details service object is required
        daoAuthenticationProvider.setUserDetailsService(userDetailService);

        //password encoder object 
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        //configuration of urls
        httpSecurity.authorizeHttpRequests(authorize->{
            //authorize.requestMatchers("/","/signup","/services").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
            
        });

        //form default login
        //if you want to make any changes in loginform then that must be done in this section
        httpSecurity.formLogin(formLogin->{
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.defaultSuccessUrl("/user/dashboard");
            //formLogin.failureForwardUrl("/login-error");
            //formLogin.defaultSuccessUrl("user/dashboard");
           
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");
            formLogin.failureHandler(new AuthenticationFailureHandler() {

                @Override
                public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException exception) throws IOException, ServletException {
                    if(exception instanceof DisabledException){
                        //user is disabled
                        HttpSession session = request.getSession();
                        session.setAttribute("message", Message.builder().content("User is disabled,Email with verification link is sent to your email id !!").type(MessageType.red).build());
                        response.sendRedirect("/login");
                    }
                }
                
            });


        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutForm->{
            logoutForm.logoutUrl("/logout");
            logoutForm.logoutSuccessUrl("/?logout=true");
            
                
            
        });
        
        //oauth configuration
        httpSecurity.oauth2Login(oauth->{
            oauth.loginPage("/login");
            oauth.successHandler(handler);  

        });

        return httpSecurity.build();
    }   
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
