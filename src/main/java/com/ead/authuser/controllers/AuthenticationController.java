package com.ead.authuser.controllers;

import com.ead.authuser.services.UserService;
import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Log4j2
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
        // Para ver o que está recebendo, a nível de desenvolvimento. {} é para usar o toString
        log.debug(" POST registerUser userDto received {} ", userDto.toString());
        if(userService.existsByUserName(userDto.getUserName())) {
            log.warn(" Username {} is already taken ", userDto.getUserName());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(" Error: Username is already taken! ");
        } if(userService.existsByEmail(userDto.getEmail())) {
            log.warn(" Email {} is already taken ", userDto.getEmail());
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
        log.debug(" POST registerUser userModel saved {} ", userModel.toString());
        log.info(" User saved successfully userId {} ", userModel.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }

    @GetMapping("/")
    public String index() {
        // Mensagens extremamente detalhadas, geralmente usadas para rastreamento de execução minucioso.
        log.trace("TRACE");
        // usado quando a api está em desenvolvimento, para saber os erros, sugestões de melhoras, etc.
        log.debug("DEBUG");
        // Mensagens informativas sobre o funcionamento do sistema. não é detalhado como o debug.
        log.info("INFO");
        // Mensagens que indicam situações que podem não ser desejáveis ou que merecem atenção.
        log.warn("WARN");
        // Mensagens que indicam falhas críticas no sistema.
        log.error("ERROR");
        return "Logging Spring Boot...";
    }



}
