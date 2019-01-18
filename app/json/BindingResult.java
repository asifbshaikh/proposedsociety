package json;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BindingResult<T> {
    private final Map<String, String> errors = new HashMap<String, String>();
    private T value;

    public T get() {
        return value;
    }

    public void addBindingFailure(String property, String errorCode) {
        errors.put(property, errorCode);
    }

    public void addValidationError(String property, String errorCode) {
        errors.put(property, errorCode);
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public Map<String, String> getErrors() {
        return Collections.unmodifiableMap(errors);
    }
}
