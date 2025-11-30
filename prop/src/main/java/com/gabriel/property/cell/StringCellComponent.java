package com.gabriel.property.cell;

import com.gabriel.property.property.StringProperty;
import com.gabriel.property.util.Keys;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class StringCellComponent extends AbstractCellComponent {

    private StringProperty property;
    private JTextField textField;

    public StringCellComponent(StringProperty stringProperty) {
        this.property = stringProperty;
        this.textField = new JTextField();

        textField.addKeyListener(new KeyAdapter());
    }

    @Override
    public Component getTableCellEditorComponent(JTable jTable, Object o, boolean b, int i, int i1) {
        textField.setText(property.getValue());

        return textField;
    }

    @Override
    public Object getCellEditorValue() {
        return property.getValue();
    }

    @Override
    public Component getTableCellRendererComponent(JTable jTable, Object o, boolean b, boolean b1, int i, int i1) {
        return new JLabel(property.getValue());
    }

    private class KeyAdapter extends java.awt.event.KeyAdapter {
        @Override
        public void keyReleased(KeyEvent event) {
            if (Keys.ignoreKey(event.getKeyCode())) {
                return;
            }

            if (property.getValidator().validate(textField.getText())) {
                property.setValue(textField.getText());
                textField.setBackground(options.getBackgroundColor());
                eventDispatcher.dispatchUpdateEvent(property);
            } else {
                textField.setBackground(options.getInvalidColor());
            }
        }
    }
}
