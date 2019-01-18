package validation;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;

import play.data.validation.Constraints;
import play.data.validation.Constraints.RequiredValidator;
import play.data.validation.Constraints.Validator;
import play.libs.F.Tuple;

public class RequiredAndNotEqualsValidator extends Validator<Object> implements ConstraintValidator<RequiredAndNotEquals, Object>{
    public static final String message = Constraints.RequiredValidator.message;
    private static final RequiredValidator delegate = new RequiredValidator();
    
    private List<String> disallowedValues;

    @Override
    public void initialize(RequiredAndNotEquals constraintAnnotation) {
        disallowedValues = Arrays.asList(constraintAnnotation.notEquals());
    }

    @Override
    public Tuple<String, Object[]> getErrorMessageKey() {
        return delegate.getErrorMessageKey();
    }

    @Override
    public boolean isValid(Object object) {
        // FIXME: The Required constraint of Play handles collection as well as other numeric values and dates, we do not!
        return delegate.isValid(object) && !disallowedValues.contains(object);
    }
}
