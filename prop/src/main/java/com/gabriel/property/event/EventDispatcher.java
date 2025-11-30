package com.gabriel.property.event;

import com.gabriel.property.property.Property;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Same API, but iteration is safe while listeners add/remove during callbacks.
 */
public class EventDispatcher {

    private final CopyOnWriteArrayList<PropertyEventListener> listeners;

    public EventDispatcher() {
        this.listeners = new CopyOnWriteArrayList<>();
    }

    public void addEventListener(PropertyEventListener eventListener) {
        if (eventListener != null) listeners.addIfAbsent(eventListener);
    }

    public void removeEventListener(PropertyEventListener eventListener) {
        listeners.remove(eventListener);
    }

    public void dispatchUpdateEvent(Property property) {
        // listeners list is snapshot-safe
        listeners.forEach(l -> l.onPropertyUpdated(property));
    }

    public void dispatchPropertyAddedEvent(Property property) {
        listeners.forEach(l -> l.onPropertyAdded(property));
    }
}