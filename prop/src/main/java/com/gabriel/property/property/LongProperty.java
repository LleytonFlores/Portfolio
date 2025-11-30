package com.gabriel.property.property;

import com.gabriel.property.validator.Validator;
import com.gabriel.property.validator.longNumber.LongValidator;

public class LongProperty extends AbstractProperty<Long> {

    public LongProperty(String name, Long value) {
        super(name, value, new LongValidator());
    }

    public LongProperty(String name, Long value, Validator validator) {
        super(name, value, validator);
    }
}
