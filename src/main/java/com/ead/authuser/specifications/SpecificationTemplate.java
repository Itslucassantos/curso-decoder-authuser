package com.ead.authuser.specifications;

import com.ead.authuser.models.UserModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationTemplate {

    // para somar todas as specifications
    // pode usar @Or para pegar uma specification ou outra
    @And({
        // path = "userType" = vai filtrar por userType, spec = Equal.class = traz a informação exata.
        // Por ser enum é um Equal.class.
        @Spec(path = "userType", spec = Equal.class),
        @Spec(path = "userStatus", spec = Equal.class),
        // spec = Like.class = traz todos os parâmetros com aquele nome, ex: se buscar por Lucas e tiver Lucas 123,
        // vai trazer todos.
        @Spec(path = "email", spec = Like.class)
    })
    public interface UserSpec extends Specification<UserModel> {}

}
