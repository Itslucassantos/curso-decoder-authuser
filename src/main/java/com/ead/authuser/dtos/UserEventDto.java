package com.ead.authuser.dtos;

import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import lombok.Data;

import java.util.UUID;

@Data
public class UserEventDto {

    private UUID userId;

    private String userName;

    private String email;

    private String fullName;

    private String userStatus;

    private String userType;

    private String phoneNumber;

    private String cpf;

    private String imageUrl;

    private String actionType;

}
