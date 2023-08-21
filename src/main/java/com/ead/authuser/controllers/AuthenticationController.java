package com.ead.authuser.controllers;

import com.ead.authuser.Services.UserService;
import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
// * é permitido que a api seja acessada de qualquer origem e 3600 segundos
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserDto.UserView.RegistrationPost.class)
    // @JsonView(UserDto.UserView.RegistrationPost.class) vai validar somente os campos que tem a assinatura do RegistrationPost.
                                                   @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto) {
        if(userService.existsByUserName(userDto.getUserName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(" Error: Username is already taken! ");
        } if(userService.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(" Error: Email is already taken! ");
        }
        UserModel userModel = new UserModel();
        //Para converter o dto em model.
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        // ZoneId.of("UTC") para definir o formato da data.
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }



}
