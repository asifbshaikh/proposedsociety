package org.codehaus.jackson.map.deser.std;

import java.io.IOException;

import json.BindingResult;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.NumberInput;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.deser.std.StdDeserializer.PrimitiveOrWrapperDeserializer;

public class IntegerDeserializer extends PrimitiveOrWrapperDeserializer<Integer> {
    private final BindingResult<?> result;

    public IntegerDeserializer(BindingResult<?> result, Integer nvl) {
        super(Integer.class, nvl);
        this.result = result;
    }

    protected final Integer parseIntegerCustom(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) { // coercing
                                                                                    // should
                                                                                    // work
                                                                                    // too
            return Integer.valueOf(jp.getIntValue());
        }
        if (t == JsonToken.VALUE_STRING) { // let's do implicit re-parse
            String text = jp.getText().trim();
            try {
                int len = text.length();
                if (len > 9) {
                    long l = Long.parseLong(text);
                    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
                        result.addBindingFailure(jp.getCurrentName(), "error.invalid");
                        return getNullValue();
                    }
                    return Integer.valueOf((int) l);
                }
                if (len == 0) {
                    return (Integer) getEmptyValue();
                }
                return Integer.valueOf(NumberInput.parseInt(text));
            } catch (IllegalArgumentException iae) {
                result.addBindingFailure(jp.getCurrentName(), "error.invalid");
                return getNullValue();
            }
        }
        if (t == JsonToken.VALUE_NULL) {
            return (Integer) getNullValue();
        }
        result.addBindingFailure(jp.getCurrentName(), "error.invalid");
        return getNullValue();
    }

    @Override
    public Integer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return parseIntegerCustom(jp, ctxt);
    }

    // 1.6: since we can never have type info ("natural type"; String, Boolean,
    // Integer, Double):
    // (is it an error to even call this version?)
    @Override
    public Integer deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException,
            JsonProcessingException {
        return parseIntegerCustom(jp, ctxt);
    }
}
