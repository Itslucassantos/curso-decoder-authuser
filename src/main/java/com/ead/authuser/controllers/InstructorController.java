package com.ead.authuser.controllers;

import com.ead.authuser.dtos.InstructorDto;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Log4j2
@RestController
// * é permitido que a api seja acessada de qualquer origem e 3600 segundos
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/instructors")
public class InstructorController {

    private final UserService userService;

    @Autowired
    public InstructorController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscriptionInstructor(@RequestBody @Valid InstructorDto instructorDto) {
        Optional<UserModel> userModelOptional = this.userService.findById(instructorDto.getUserId());
        if(!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" User not found! ");
        } else {
            UserModel userModel = userModelOptional.get();
            userModel.setUserType(UserType.INSTRUCTOR);
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            this.userService.save(userModel);
            return ResponseEntity.status(HttpStatus.OK).body(userModel);
        }
    }

}
