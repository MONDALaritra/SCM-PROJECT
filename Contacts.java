package com.smartcontact.entities;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contacts {
    @Id
    private String id;
    private String email;
    private String name;
    private String phone;
    private String address;
    private String picture;
    private String gender;

    @Column(length=5000)
    private String description;
    private boolean favourite = false;

    private String websiteLink;
    private String linkedinlink;
    //private List<SocialLink> sociallink = new ArrayList<>();
    private String cloudinaryImagePublicid;
    @ManyToOne
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "contacts", cascade = CascadeType.ALL, fetch= FetchType.EAGER, orphanRemoval = true)
    private List<SocialLink> links = new ArrayList<>();
}
