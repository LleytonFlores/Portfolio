package com.gabriel.property.event;

import com.gabriel.property.property.Property;

/**
 * Event listener interface
 * <p>
 * Objects implementing this interface can subscribe to events coming from the
 * property sheet.
 */
public interface PropertyEventListener {

    /**
     * Fired when a property is successfully updated
     *
     * @param property Property
     */
    public void onPropertyUpdated(Property property);

    /**
     * Fired when a property is successfully added to the sheet
     *
     * @param property Property
     */
    public void onPropertyAdded(Property property);
}
