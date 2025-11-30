package com.gabriel.property.property;

import com.gabriel.property.validator.StringValidator;
import com.gabriel.property.validator.Validator;

public class StringProperty extends AbstractProperty<String> {

    public StringProperty(String name, String value) {
        super(name, value, new StringValidator());
    }

    public StringProperty(String name, String value, Validator validator) {
        super(name, value, validator);
    }
}
