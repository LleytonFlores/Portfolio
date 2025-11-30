package com.gabriel.property.exception;

import com.gabriel.property.property.Property;

public class PropertyNotSupportedException extends RuntimeException {

    public PropertyNotSupportedException(Property property) {
        super("Could not find default editor/renderer for property '" + property.getName() + "'");
    }
}
