package com.gabriel.property.event;

import com.gabriel.property.property.Property;

/**
 * Adapter to make writing event listeners a bit easier. This adapter implements the
 * property sheet event listener but does not execute any code. These methods can
 * be overwritten to provide the desired functionality.
 */
public class PropertyEventAdapter implements PropertyEventListener {

    @Override
    public void onPropertyUpdated(Property property) {
    }

    @Override
    public void onPropertyAdded(Property property) {
    }
}
