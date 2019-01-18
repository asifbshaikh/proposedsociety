package json;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.deser.StdDeserializerProvider;
import org.codehaus.jackson.map.deser.std.IntegerDeserializer;
import org.codehaus.jackson.map.deser.std.LongDeserializer;
import org.codehaus.jackson.map.module.SimpleDeserializers;
import org.hibernate.validator.internal.engine.resolver.DefaultTraversableResolver;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import validation.ProposedSocietyTraversableResolver;

public final class JsonHelper {
    private JsonHelper() {}
    
    public static <T> BindingResult<T> parse(JsonNode node, Class<T> clazz) {
    	return parse(node, clazz, true);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> BindingResult<T> parse(JsonNode node, Class<T> clazz, boolean failOnUnknownProperty) {
        BindingResult<T> result = new BindingResult<T>();
        SimpleDeserializers deserializers = new SimpleDeserializers();
        
        deserializers.addDeserializer(Integer.class, new IntegerDeserializer(result, null));
        deserializers.addDeserializer(Long.class, new LongDeserializer(result, null));

        ObjectMapper mapper = new ObjectMapper().setDeserializerProvider(
                new StdDeserializerProvider().withAdditionalDeserializers(deserializers));
        
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperty);
        
        try {
            T value = mapper.treeToValue(node, clazz);
        
            result.setValue(value);

            SpringValidatorAdapter validator = new SpringValidatorAdapter(
                    Validation.byDefaultProvider()
                    .configure()
                    .traversableResolver(new ProposedSocietyTraversableResolver(new DefaultTraversableResolver()))
                    .buildValidatorFactory().getValidator());
            
            Set<ConstraintViolation<T>> violations = validator.validate(value);
            
            for (ConstraintViolation<T> v : violations) {
                if (!result.getErrors().containsKey(v.getPropertyPath().toString())) {
                    result.addValidationError(v.getPropertyPath().toString(), v.getMessage());
                }
            }

            try {
				java.lang.reflect.Method v = value.getClass().getMethod("validate");
				Object errors = v.invoke(value);
				
				if (errors instanceof Map) {
					for (Map.Entry<String, String> error : (Set<Entry<String, String>>) ((Map)errors).entrySet()) {
						result.addValidationError(error.getKey(), error.getValue());
					}
				}
			} catch (SecurityException e) {
			} catch (NoSuchMethodException e) {
			} catch (Throwable e) {
				e.printStackTrace();
			}
            
        // Ignore all exceptions for now, but retain print stack for now.
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
