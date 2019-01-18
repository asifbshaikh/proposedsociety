package validation;

import static play.libs.F.None;
import static play.libs.F.Some;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.hibernate.validator.internal.engine.resolver.DefaultTraversableResolver;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.libs.F.Option;

public class PSForm<T> extends play.data.Form<T> {
    private String rootName;
    private Class<T> backedType;
    private Map<String,List<ValidationError>> errors;
    private Class<?> groups;

    public PSForm(Class<T> clazz) {
        super(clazz);
        this.backedType = clazz;
        this.rootName = null;
        this.errors = new HashMap<String, List<ValidationError>>();
        this.backedType = clazz;
    }

    public PSForm(String name, Class<T> clazz, Class<?> groups) {
        super(name, clazz, groups);
        this.rootName = name;
        this.backedType = clazz;
        this.groups = groups;
        this.errors = new HashMap<String, List<ValidationError>>();
    }

    public PSForm(String rootName, Class<T> clazz, Map<String, String> data, Map<String, List<ValidationError>> errors, Option<T> value, Class<?> groups) {
        super(rootName, clazz, data, errors, value, groups);
        this.rootName = rootName;
        this.errors = errors;
        this.groups = groups;
        this.backedType = clazz;
    }

    public PSForm(String rootName, Class<T> clazz, Map<String, String> data, Map<String, List<ValidationError>> errors, Option<T> value) {
        super(rootName, clazz, data, errors, value);
        this.rootName = rootName;
        this.backedType = clazz;
        this.errors = errors;
        this.rootName = rootName;
        this.backedType = clazz;
    }

    public PSForm(String name, Class<T> clazz) {
        super(name, clazz);
    }

    @SuppressWarnings("unchecked")
    public PSForm<T> bind(Map<String,String> data, String... allowedFields) {

        DataBinder dataBinder = null;
        Map<String, String> objectData = data;
        if(rootName == null) {
            dataBinder = new DataBinder(blankInstance());
        } else {
            dataBinder = new DataBinder(blankInstance(), rootName);
            objectData = new HashMap<String,String>();
            for(String key: data.keySet()) {
                if(key.startsWith(rootName + ".")) {
                    objectData.put(key.substring(rootName.length() + 1), data.get(key));
                }
            }
        }
        if(allowedFields.length > 0) {
            dataBinder.setAllowedFields(allowedFields);
        }

        SpringValidatorAdapter validator = new SpringValidatorAdapter(
                Validation.byDefaultProvider()
                .configure()
                .traversableResolver(new ProposedSocietyTraversableResolver(new DefaultTraversableResolver(),groups))
                .buildValidatorFactory().getValidator());

        dataBinder.setValidator(validator);
        dataBinder.setConversionService(play.data.format.Formatters.conversion);
        dataBinder.setAutoGrowNestedPaths(true);
        dataBinder.bind(new MutablePropertyValues(objectData));
        Set<ConstraintViolation<Object>> validationErrors;
        if (groups != null) {
            validationErrors = validator.validate(dataBinder.getTarget(), groups);
        } else {
            validationErrors = validator.validate(dataBinder.getTarget());
        }

        BindingResult result = dataBinder.getBindingResult();

        for (ConstraintViolation<Object> violation : validationErrors) {
            String field = violation.getPropertyPath().toString();
            FieldError fieldError = result.getFieldError(field);
            if (fieldError == null || !fieldError.isBindingFailure()) {
                try {
                    result.rejectValue(field,
                            violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName(),
                            getArgumentsForConstraint(result.getObjectName(), field, violation.getConstraintDescriptor()),
                            violation.getMessage());
                }
                catch (NotReadablePropertyException ex) {
                    throw new IllegalStateException("JSR-303 validated property '" + field +
                            "' does not have a corresponding accessor for data binding - " +
                            "check your DataBinder's configuration (bean property versus direct field access)", ex);
                }
            }
        }

        if(result.hasErrors()) {
            Map<String,List<ValidationError>> errors = new HashMap<String,List<ValidationError>>();
            for(FieldError error: result.getFieldErrors()) {
                String key = error.getObjectName() + "." + error.getField();
                if(key.startsWith("target.") && rootName == null) {
                    key = key.substring(7);
                }
                List<Object> arguments = new ArrayList<Object>();
                for(Object arg: error.getArguments()) {
                    if(!(arg instanceof org.springframework.context.support.DefaultMessageSourceResolvable)) {
                        arguments.add(arg);
                    }                    
                }
                if(!errors.containsKey(key)) {
                   errors.put(key, new ArrayList<ValidationError>()); 
                }
                errors.get(key).add(new ValidationError(key, error.isBindingFailure() ? "error.invalid" : error.getDefaultMessage(), arguments));                    
            }
            return new PSForm(rootName, backedType, data, errors, None(), groups);
        } else {
            Object globalError = null;
            if(result.getTarget() != null) {
                try {
                    java.lang.reflect.Method v = result.getTarget().getClass().getMethod("validate");
                    globalError = v.invoke(result.getTarget());
                } catch(NoSuchMethodException e) {
                } catch(Throwable e) {
                    throw new RuntimeException(e);
                }
            }
            if(globalError != null) {
                Map<String,List<ValidationError>> errors = new HashMap<String,List<ValidationError>>();
                if(globalError instanceof String) {
                    errors.put("", new ArrayList<ValidationError>());
                    errors.get("").add(new ValidationError("", (String)globalError, new ArrayList()));
                } else if(globalError instanceof List) {
                    for (ValidationError error : (List<ValidationError>) globalError) {
                      List<ValidationError> errorsForKey = errors.get(error.key());
                      if (errorsForKey == null) {
                        errors.put(error.key(), errorsForKey = new ArrayList<ValidationError>());
                      }
                      errorsForKey.add(error);
                    }
                } else if(globalError instanceof Map) {
                    errors = (Map<String,List<ValidationError>>)globalError;
                }
                return new PSForm(rootName, backedType, data, errors, None(), groups);
            }
            
            return new PSForm(rootName, backedType, new HashMap<String,String>(data), new HashMap<String,List<ValidationError>>(errors), Some((T)result.getTarget()), groups);
        }
    }
    
    private T blankInstance() {
        try {
            return backedType.newInstance();
        } catch(Exception e) {
            throw new RuntimeException("Cannot instantiate " + backedType + ". It must have a default constructor", e);
        }
    }

    /**
     * Instantiates a dynamic form.
     */
    public static DynamicForm form() {
        return new DynamicForm();
    }
    
    /**
     * Instantiates a new form that wraps the specified class.
     */
    public static <T> PSForm<T> form(Class<T> clazz) {
        return new PSForm<T>(clazz);
    }
    
    /**
     * Instantiates a new form that wraps the specified class.
     */
    public static <T> PSForm<T> form(String name, Class<T> clazz) {
        return new PSForm<T>(name, clazz);
    }
    
    /**
     * Instantiates a new form that wraps the specified class.
     */
    public static <T> PSForm<T> form(String name, Class<T> clazz, Class<?> group) {
        return new PSForm<T>(name, clazz, group);
    }

    /**
     * Instantiates a new form that wraps the specified class.
     */
    public static <T> PSForm<T> form(Class<T> clazz, Class<?> group) {
        return new PSForm<T>(null, clazz, group);
    }
}
