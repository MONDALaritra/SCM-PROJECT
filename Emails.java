package com.smartcontact.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;

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
@AllArgsConstructor
@NoArgsConstructor
public class Emails {

    @Id
    private String emailid;

    private String sourceemail;

    private String destinationname;

    private String destinationemail;

    private String subject;

    private String message;

    @ManyToOne
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "email", cascade = CascadeType.ALL, fetch= FetchType.EAGER, orphanRemoval = true)
    private List<FileEntity> files = new ArrayList<>();

    


}
