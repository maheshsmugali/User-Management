package com.pws.usermanagement.dto;

import com.pws.usermanagement.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Builder
@Data
public class SignUpDTO {
    private String firstName;

    private String lastName;

    private String email;

    private long phoneNumber;

    private String password;

    private Date dateOfBirth;

    private User.Keyword gender;

    private String role;

    private boolean isActive;
}
