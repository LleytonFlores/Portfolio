package com.gabriel.property.property;

import com.gabriel.property.validator.Validator;
import com.gabriel.property.validator.doubleNumber.DoubleValidator;

public class DoubleProperty extends AbstractProperty<Double> {

    public DoubleProperty(String name, Double value) {
        super(name, value, new DoubleValidator());
    }

    public DoubleProperty(String name, Double value, Validator validator) {
        super(name, value, validator);
    }
}
