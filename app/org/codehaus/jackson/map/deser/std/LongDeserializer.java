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

public class LongDeserializer extends PrimitiveOrWrapperDeserializer<Long> {
    private final BindingResult<?> result;

    public LongDeserializer(BindingResult<?> result, Long nvl) {
        super(Long.class, nvl);
        this.result = result;
    }

    protected final Long parseLongCustom(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException
        {
            JsonToken t = jp.getCurrentToken();
        
            // it should be ok to coerce (although may fail, too)
            if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
                return jp.getLongValue();
            }
            // let's allow Strings to be converted too
            if (t == JsonToken.VALUE_STRING) {
                // !!! 05-Jan-2009, tatu: Should we try to limit value space, JDK is too lenient?
                String text = jp.getText().trim();
                if (text.length() == 0) {
                    return (Long) getEmptyValue();
                }
                try {
                    return Long.valueOf(NumberInput.parseLong(text));
                } catch (IllegalArgumentException iae) { 
                    result.addBindingFailure(jp.getCurrentName(), "error.invalid");
                    return getNullValue();
                }
               
     
            }

            if (t == JsonToken.VALUE_NULL) {
                return (Long) getNullValue();
            }

            result.addBindingFailure(jp.getCurrentName(), "error.invalid");
            return getNullValue();
        }

    
    @Override
    public Long deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return parseLongCustom(jp, ctxt);
    }

    @Override
    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException,
            JsonProcessingException {
        return parseLongCustom(jp, ctxt);
    }

    
}
