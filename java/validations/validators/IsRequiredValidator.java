
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;

@Log4j2
public class IsRequiredValidator implements ConstraintValidator<IsRequired, Object> {

    private String[] fields = {};

    @Override
    public void initialize(IsRequired constraintAnnotation) {
        this.fields = constraintAnnotation.fields();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {

        boolean isValid = false;
        String actualField = null;
        String message = "{campo.obrigatorio}";

        if (obj != null) {

            try {
                for (String f : this.fields) {
                    actualField = f;
                    Method method = obj.getClass().getMethod(getFieldMethodName(actualField), (Class<?>[]) null);
                    Object value = method.invoke(obj, (Object[]) null);

                    isValid = value != null;

                    if (value instanceof String) {
                        isValid = StringUtils.isNotBlank((String) value);
                    }

                    if (!isValid) {
                        addToContext(context, actualField, message);
                    }
                }
            } catch (Exception e) {
                log.error("Erro ao validar campos do objeto anotado com @IsRequired: {}", e.getMessage());
                addToContext(context, actualField, String.format("Erro ao validar campos do objeto anotado com @IsRequired: %s", actualField));
            }

        }

        return isValid;
    }

    private void addToContext(ConstraintValidatorContext context, String actualField, String message) {
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(actualField)
                .addConstraintViolation();
    }

    private String getFieldMethodName(String field) {
        String s = StringUtils.trim(field);
        return "get".concat(s.substring(0, 1).toUpperCase().concat(s.substring(1)));
    }
}
