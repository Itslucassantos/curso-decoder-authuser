package com.ead.authuser.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

// A anotação @Documented em Java é usada para indicar que uma anotação personalizada (criada por você ou de terceiros)
// deve ser incluída na documentação gerada automaticamente.
@Documented
// O parâmetro validatedBy na anotação @Constraint é usado para especificar a classe que implementa a lógica de
// validação real para a restrição personalizada. Nesse caso, UsernameConstraintImplement.class é a classe que
// contém a implementação da validação para a restrição UsernameConstraint, que você deve ter definido em algum lugar
// do seu código.
@Constraint(validatedBy = UsernameConstraintImplement.class)
// A enumeração ElementType é usada para definir os tipos de elementos aos quais uma anotação pode ser aplicada.
// ElementType.METHOD indica que a anotação pode ser aplicada a métodos, enquanto ElementType.FIELD indica que ela
// pode ser aplicada a campos.
@Target({ElementType.METHOD, ElementType.FIELD})
// Quando a validação vai ocorrer.
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameConstraint {
    // Mensagem colocada quando ocorre o erro
    String message() default "Invalid username";
    // O elemento groups() permite agrupar validações. Isso pode ser útil quando você deseja aplicar diferentes
    //conjuntos de validações a diferentes situações. Cada grupo representa um conjunto de validações a serem aplicadas.
    Class<?>[] groups() default {};
    // O elemento payload() permite associar informações adicionais à validação. Essas informações podem ser usadas
    // para indicar o nível de severidade da validação ou para fornecer detalhes adicionais sobre o erro de validação.
    Class<? extends Payload>[] payload() default {};
}
