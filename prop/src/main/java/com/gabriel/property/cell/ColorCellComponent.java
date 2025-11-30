package com.gabriel.property.cell;

import com.gabriel.property.property.ColorProperty;

import javax.swing.*;
import java.awt.*;

/**
 * Same API. Adds null check to avoid setting null color when dialog is canceled.
 */
public class ColorCellComponent extends AbstractCellComponent {

    private final ColorProperty property;
    private final JButton delegate;

    public ColorCellComponent(ColorProperty property) {
        this.property = property;
        this.delegate = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(ColorCellComponent.this.property.getValue());
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
            }
        };

        delegate.addActionListener(e -> {
            Color chosen = JColorChooser.showDialog(delegate, "Choose colour", property.getValue());
            if (chosen == null) return;               // user canceled
            if (!chosen.equals(property.getValue())) {
                property.setValue(chosen);
                delegate.repaint();
                eventDispatcher.dispatchUpdateEvent(property);
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable jTable, Object o, boolean b, int row, int col) {
        return delegate;
    }

    @Override
    public Object getCellEditorValue() {
        return property.getValue();
    }

    @Override
    public Component getTableCellRendererComponent(JTable jTable, Object o, boolean isSelected, boolean hasFocus, int row, int col) {
        return delegate;
    }
}
