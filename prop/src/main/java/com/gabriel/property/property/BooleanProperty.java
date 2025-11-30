package com.gabriel.property.property;

import com.gabriel.property.validator.NullValidator;

public class BooleanProperty extends AbstractProperty<Boolean> {

    public BooleanProperty(String name, Boolean value) {
        super(name, value, new NullValidator());
    }
}
