package com.ead.authuser.models;

import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

// Traz todos os construtores, getters, setters, etc...
@Data
// Oculta os campos nulls, mostra somente os campos que tem valores. Está a nível de classe para ignorar todos os
// atributos que não tiver valores na hora da Serialização
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_USERS")
// é importante fazer o implements com o Serializable pq ela vai fazer a conversão de objetos java em uma sequência de
// bytes que pode ser salvo no banco de dados.
// RepresentationModel<UserModel> para criar os links
public class UserModel extends RepresentationModel<UserModel> implements Serializable {

    //para ver se os dados convertidos é dessa classe, a JVM víncula esse id com cada classe que faz a conversão, que é
    // Serializable, é importante declarar para evitar algumas exeções.
    private static final long serialVersionUID = 1L;

    //esse UUID é ideal para trabalhar com bancos dados distribuídos, evita replica.
    @Id
    // GenerationType.AUTO gera os id automático, por ser UUID não tem réplica.
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    //unique = true pq não pode ter usuário repetido, cada usuário é único.
    @Column(nullable = false, unique = true, length = 50)
    private String userName;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 255)
    // Não deve mostrar a senha pq a senha é restrita para cada usuário.
    @JsonIgnore
    private String password;

    @Column(nullable = false, length = 150)
    private String fullName;

    @Column(nullable = false)
    //para salvar no BD como uma String
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 20)
    private String cpf;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    // Formato da data que vai ser retornado na serialização
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime creationDate;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime lastUpdateDate;

}
