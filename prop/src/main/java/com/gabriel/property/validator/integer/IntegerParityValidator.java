package com.gabriel.property.validator.integer;

import com.gabriel.property.validator.Validator;

public class IntegerParityValidator implements Validator {

    protected boolean even;

    public IntegerParityValidator() {
        this(true);
    }

    public IntegerParityValidator(boolean even) {
        this.even = even;
    }

    public void setEven(boolean even) {
        this.even = even;
    }

    @Override
    public boolean validate(Object object) {
        int value = Integer.parseInt((String) object);

        if (even) {
            return value % 2 == 0;
        } else {
            return value % 2 != 0;
        }
    }
}
