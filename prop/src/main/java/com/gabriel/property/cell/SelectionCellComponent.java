package com.gabriel.property.cell;

import com.gabriel.property.property.selection.Item;
import com.gabriel.property.property.selection.SelectionProperty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Objects;

/**
 * Same behavior, but programmatic selection is silent.
 * Only user selection changes dispatch an update.
 */
public class SelectionCellComponent extends AbstractCellComponent {

    private final SelectionProperty property;
    private final JComboBox<Item> comboBox;
    private boolean suppressEvents = false;

    public SelectionCellComponent(SelectionProperty property) {
        this.property = property;
        this.comboBox = new JComboBox<>();

        property.getItems().forEach(e -> comboBox.addItem((Item) e));

        comboBox.addItemListener(e -> {
            if (e.getStateChange() != ItemEvent.SELECTED) return;
            if (suppressEvents) return;

            Object sel = ((Item) comboBox.getSelectedItem());
            if (sel == null) return;

            Object newVal = ((Item) sel).getValue();
            if (!Objects.equals(property.getValue(), newVal)) {
                property.setValue(newVal);
                eventDispatcher.dispatchUpdateEvent(property);
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable jTable, Object o, boolean b, int row, int col) {
        return comboBox;
    }

    @Override
    public Object getCellEditorValue() {
        return comboBox.getSelectedItem();
    }

    /** Programmatic set without firing ItemListener or dispatching update */
    public void setCellEditorValue(Object object) {
        suppressEvents = true;
        try {
            comboBox.setSelectedItem(object);
        } finally {
            suppressEvents = false;
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable jTable, Object o, boolean isSelected, boolean hasFocus, int row, int col) {
        JLabel label = new JLabel();
        if (comboBox.getSelectedItem() != null) {
            label.setText(comboBox.getSelectedItem().toString());
        }
        return label;
    }
}
