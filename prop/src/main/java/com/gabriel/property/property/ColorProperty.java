package com.gabriel.property.property;

import com.gabriel.property.validator.NullValidator;

import java.awt.*;

public class ColorProperty extends AbstractProperty<Color> {

    public ColorProperty(String name, Color value) {
        super(name, value, new NullValidator());
    }
}
