package com.gabriel.property.property;

import com.gabriel.property.action.Action;
import com.gabriel.property.validator.NullValidator;


public class ActionProperty extends AbstractProperty<Action> {

    private String actionName;

    public ActionProperty(String label, String actionName, Action action) {
        super(label, action, new NullValidator());

        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }
}
