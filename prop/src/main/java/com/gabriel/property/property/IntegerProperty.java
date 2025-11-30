package com.gabriel.property.property;

import com.gabriel.property.validator.Validator;
import com.gabriel.property.validator.integer.IntegerValidator;

public class IntegerProperty extends AbstractProperty<Integer> {

    public IntegerProperty(String name, Integer value) {
        super(name, value, new IntegerValidator());
    }

    public IntegerProperty(String name, Integer value, Validator validator) {
        super(name, value, validator);
    }
}
