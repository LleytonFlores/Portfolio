package com.gabriel.property.cell;

import com.gabriel.property.property.ActionProperty;

import javax.swing.*;
import java.awt.*;

public class ActionCellComponent extends AbstractCellComponent {

    private JButton button;

    public ActionCellComponent(ActionProperty actionProperty) {
        this.button = new JButton(actionProperty.getActionName());

        button.addActionListener(e -> actionProperty.getValue().execute());
    }

    @Override
    public Component getTableCellEditorComponent(JTable jTable, Object o, boolean b, int i, int i1) {
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public Component getTableCellRendererComponent(JTable jTable, Object o, boolean b, boolean b1, int i, int i1) {
        return button;
    }
}
