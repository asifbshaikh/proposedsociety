package common; 

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

public final class RestUtils {
    private static final String KEY_STATUS = "status";
    private static final String VAL_STATUS_SUCCESS = "success";
    private static final String VAL_STATUS_FAILURE = "failure";
    
    private RestUtils() {
        
    }
    
    public static ObjectNode createSuccessResponse() {
        ObjectNode response = JsonNodeFactory.instance.objectNode();
        response.put(KEY_STATUS, VAL_STATUS_SUCCESS);
        return response;
    }

    public static ObjectNode createFailureResponse() {
        ObjectNode response = JsonNodeFactory.instance.objectNode();
        response.put(KEY_STATUS, VAL_STATUS_FAILURE);
        return response;
    }
    
    public static ObjectNode createFailureResponse(Map<String,String> result) {
        ObjectNode response = createFailureResponse();
        ArrayNode errors = JsonNodeFactory.instance.arrayNode();
        response.put("errors", errors);
        for (Map.Entry<String, String> entry : result.entrySet()) {
        	ObjectNode error = JsonNodeFactory.instance.objectNode();
        	error.put("fieldname", entry.getKey());
        	error.put("error", entry.getValue());
        	errors.add(error);
        }
        return response;
    }

    public static <T> ObjectNode createFailureResponse(Set<ConstraintViolation<T>> violations) {
        ObjectNode response = createFailureResponse();
        //ObjectNode response = JsonNodeFactory.instance.objectNode();
        ArrayNode errors = JsonNodeFactory.instance.arrayNode();
        response.put("errors", errors);

        for (ConstraintViolation<?> v : violations) {
            ObjectNode error = JsonNodeFactory.instance.objectNode();
            //FIXME: Verify the toString() format of various frameworks, 
            // it seems currently hibernate is being used and it appears 
            // to covert the name nicely for us
        	error.put("fieldname", v.getPropertyPath().toString());
            
            error.put("error", v.getMessage());
            errors.add(error);
        }

        return response;
    }

    public static <T> ObjectNode createFailureResponse(Set<ConstraintViolation<T>> violations, Map<String, String> fields) {
        ObjectNode response = createFailureResponse(violations);
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            response.put(entry.getKey(), entry.getValue());
        }
        return response;
    }

    public static <T> ObjectNode createFailureResponse(Set<ConstraintViolation<T>> violations, String key, String value) {
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(key, value);
        return createFailureResponse(violations, fields);
    }

    public static ObjectNode createFailureResponse(String key, String value) {
        ObjectNode response = createFailureResponse();
        response.put(key, value);
        return response;
    }
}
