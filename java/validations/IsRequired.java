
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * O elemento anotado e os seus campos informados, não podem ter valor {@code null} e devem conter pelo menos um caractere que não seja
 * espaço vazio, caso seja uma {@code String}.
 *
 * @author Marks Souza
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = IsRequiredValidator.class)
public @interface IsRequired {
    String message() default "{campo.obrigatorio}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
    String[] fields() default {};
}
