package com.smartcontact.forms;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserFormCheck {
    @NotBlank(message="Name is required")
    @Size(min=3, message="Min 3 Characters are required")
    private String name;

    @NotBlank(message="Email is required")
    @Email(message="Invalid Email Address")
    private String email;

    

    @NotBlank(message = "About is required")
    private String about;

    @NotBlank(message="Phone number is required")
    @Size(min=8, max=12, message="Invalid phone number")
    private String phone;

    @NotBlank(message="put your gender here")
    private String gender;

    private MultipartFile profilePic;

    private String pictureUrl;
}
