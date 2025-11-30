package com.gabriel.property.property;

import com.gabriel.property.validator.Validator;
import com.gabriel.property.validator.floatNumber.FloatValidator;

public class FloatProperty extends AbstractProperty<Float> {

    public FloatProperty(String name, Float value) {
        super(name, value, new FloatValidator());
    }

    public FloatProperty(String name, Float value, Validator validator) {
        super(name, value, validator);
    }
}
