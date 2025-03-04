package com.smartcontact.entities;

import java.util.ArrayList;
import java.util.Collection;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails{

    @Id
    private String userId;
    @Column(nullable = false)
    private String name;
    @Column(unique = true,nullable = false)
    private String email;

    @Getter(value = AccessLevel.NONE)
    private String password;
    @Column(length = 5000)
    private String about;
    @Column(length = 5000)
    private String profilePic;
    private String phone;
    private String gender;
    private String cloudinaryImagePublicid;
    @Getter(value = AccessLevel.NONE)
    private boolean enabled = true;
    private boolean emailVerified = false;
    private boolean phoneverified = false;

    @Enumerated(value=EnumType.STRING)
    //self, google, facebook,linkedin,twitter
    private Providers providers = Providers.SELF;
    private String providerUserId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Contacts> contacts = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();

    private String emailToken;
    private String otp;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Emails> emailsend = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        //list of roles [USER,ADMIN]
        //collection of SimpleGrantedAuthority[roles{USER,ADMIN}]
        Collection<SimpleGrantedAuthority> roles =  roleList.stream().map(role-> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return roles;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
    
    
}
