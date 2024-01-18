package com.ead.authuser.controllers;

import com.ead.authuser.configs.security.JwtProvider;
import com.ead.authuser.dtos.JwtDto;
import com.ead.authuser.dtos.LoginDto;
import com.ead.authuser.enums.RoleType;
import com.ead.authuser.models.RoleModel;
import com.ead.authuser.services.RoleService;
import com.ead.authuser.services.UserService;
import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Log4j2
@RestController
// * é permitido que a api seja acessada de qualquer origem e 3600 segundos
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RoleService roleService;

    // Para poder criptografar a senha no bd
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationController(JwtProvider jwtProvider, AuthenticationManager authenticationManager, UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserDto.UserView.RegistrationPost.class)
    // @JsonView(UserDto.UserView.RegistrationPost.class) vai validar somente os campos que tem a assinatura do RegistrationPost.
                                                   @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto) {
        // Para ver o que está recebendo, a nível de desenvolvimento. {} é para usar o toString
        log.debug(" POST registerUser userDto received {} ", userDto.toString());
        if(this.userService.existsByUserName(userDto.getUserName())) {
            log.warn(" Username {} is already taken ", userDto.getUserName());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(" Error: Username is already taken! ");
        } if(this.userService.existsByEmail(userDto.getEmail())) {
            log.warn(" Email {} is already taken ", userDto.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(" Error: Email is already taken! ");
        }
        RoleModel roleModel = this.roleService.findByRoleName(RoleType.ROLE_STUDENT)
                // se nao tiver nenhum retorno do bd, a role nao existir ele retorna a excecao
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        // set na senha, deixando-a criptografada para salvar no bd.
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        UserModel userModel = new UserModel();
        //Para converter o dto em model.
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        // ZoneId.of("UTC") para definir o formato da data.
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.getRoles().add(roleModel);
        this.userService.saveUser(userModel);
        log.debug(" POST registerUser userId saved {} ", userModel.getUserId());
        log.info(" User saved successfully userId {} ", userModel.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwt(authentication);
        return ResponseEntity.ok(new JwtDto(jwt));
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
