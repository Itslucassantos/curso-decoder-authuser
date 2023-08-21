package com.ead.authuser.dtos;

import com.ead.authuser.validation.UsernameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
// Oculta os campos nulls, mostra somente os campos que tem valores. Está a nível de classe para ignorar todos os
// atributos que não tiver valores na hora da Serialização
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    // Só irá poder modificar os campos, conforme o tipo de view. Somente vai olhar os campos de registro quando for
    // registrar, para atualizar a msm coisa.
    public interface UserView {
        public static interface RegistrationPost {}
        public static interface UserPut {}
        public static interface PasswordPut {}
        public static interface ImagePut {}
    }

    private UUID userId;

    // é @NotBlank pq ele não pode ser null nem vázio = "", por ser uma String é melhor usar o @NotBlank.
    // groups = UserView.RegistrationPost.class, como estamos usando a anotação do @JsonView, é preciso específicar em
    // qual view que é pra fazer a validação.
    @NotBlank(groups = UserView.RegistrationPost.class)
    // Tamanho que esse caractere pode ter.
    @Size(min = 4, max = 50, groups = UserView.RegistrationPost.class)
    // Criando a própria anotação.
    @UsernameConstraint(groups = UserView.RegistrationPost.class)
    // userName não pode ser alterado, ele é único, ent ele só vai ser usado na hora de cadastrar, somente na view do post.
    @JsonView(UserView.RegistrationPost.class)
    private String userName;

    @NotBlank(groups = UserView.RegistrationPost.class)
    // Verifica se o email está dentro do padrão tradicional, se é um email msm.
    @Email(groups = UserView.RegistrationPost.class)
    @JsonView(UserView.RegistrationPost.class)
    private String email;

    @NotBlank(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @Size(min = 6, max = 20, groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    // password tem duas visões pq ele vai ser inserido na hora de cadastrar, mas ele também pode ser atualizado.
    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
    private String password;

    @NotBlank(groups = UserView.PasswordPut.class)
    @Size(min = 6, max = 20, groups = UserView.PasswordPut.class)
    // A senha antiga só aparece quando for alterar a senha, ent é só a view do password
    @JsonView(UserView.PasswordPut.class)
    private String oldPassword;

    // tem duas views uma para cadastrar e a outra para atualizar dados básicos.
    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String fullName;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String phoneNumber;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String cpf;

    @NotBlank(groups = UserView.ImagePut.class)
    @JsonView(UserView.ImagePut.class)
    private String imageUrl;

}
