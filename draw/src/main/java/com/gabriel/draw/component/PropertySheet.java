package com.gabriel.draw.component;

import com.gabriel.draw.command.SetDrawModeCommand;
import com.gabriel.draw.controller.PropertyEventListener;
import com.gabriel.drawfx.DrawMode;
import com.gabriel.drawfx.ShapeMode;
import com.gabriel.drawfx.command.CommandService;
import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;
import com.gabriel.property.PropertyOptions;
import com.gabriel.property.PropertyPanel;
import com.gabriel.property.cell.SelectionCellComponent;
import com.gabriel.property.event.PropertyEventAdapter;
import com.gabriel.property.property.*;
import com.gabriel.property.property.selection.Item;
import com.gabriel.property.property.selection.SelectionProperty;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertySheet extends PropertyPanel {

    private PropertyPanel propertyTable;
    private PropertyEventListener appListener;

    private SelectionProperty<ShapeMode> shapeProp;
    private Item<ShapeMode> RectangleItem;
    private Item<ShapeMode> EllipseItem;
    private Item<ShapeMode> LineItem;
    private Item<ShapeMode> TextItem;
    private Item<ShapeMode> SelectItem;

    private AppService appService;

    public PropertySheet(PropertyOptions options) {
        super(options);

        RectangleItem = new Item<>(ShapeMode.Rectangle, "Rectangle");
        EllipseItem   = new Item<>(ShapeMode.Ellipse,   "Ellipse");
        LineItem      = new Item<>(ShapeMode.Line,      "Line");
        TextItem      = new Item<>(ShapeMode.Text,      "Text");
        SelectItem    = new Item<>(ShapeMode.Select,    "Select");

        shapeProp = new SelectionProperty<>(
                "Current Shape",
                new ArrayList<>(Arrays.asList(RectangleItem, EllipseItem, LineItem, TextItem, SelectItem))
        );

        //add listener for shape mode changes
        addEventListener(new PropertyEventAdapter() {
            @Override
            public void onPropertyUpdated(Property property) {
                if ("Current Shape".equals(property.getName()) && appService != null) {
                    @SuppressWarnings("unchecked")
                    Item<ShapeMode> selectedItem = (Item<ShapeMode>) property.getValue();
                    if (selectedItem != null) {
                        ShapeMode newMode = selectedItem.getValue();
                        // FIXED: Use setShapeMode instead of SetDrawModeCommand
                        appService.setShapeMode(newMode);
                    }
                }
            }
        });
    }

    public void setShapeProp(ShapeMode shapeMode) {
        if (propertyTable == null) return;
        SelectionCellComponent scc = propertyTable.getSelectionCellComponent();
        if (scc == null) return;

        Object cur = scc.getCellEditorValue();
        switch (shapeMode) {
            case Rectangle:
                if (cur != RectangleItem) scc.setCellEditorValue(RectangleItem);
                break;
            case Ellipse:
                if (cur != EllipseItem) scc.setCellEditorValue(EllipseItem);
                break;
            case Line:
                if (cur != LineItem) scc.setCellEditorValue(LineItem);
                break;
            case Text:
                if (cur != TextItem) scc.setCellEditorValue(TextItem);
                break;
            case Select:
                if (cur != SelectItem) scc.setCellEditorValue(SelectItem);
                break;
        }
    }

    public void populateTable(AppService appService) {
        this.appService = appService;
        propertyTable = this;
        if (appListener == null) {
            appListener = new PropertyEventListener(appService);
            propertyTable.addEventListener(appListener);
        }

        propertyTable.clear();

        Drawing drawing = appService.getDrawing();
        List<Shape> selection = selectedShapes(drawing);

        String ctx = selection.isEmpty()
                ? "Application settings"
                : (selection.size() == 1 ? "1 object selected" : selection.size() + " objects selected");
        propertyTable.addProperty(new StringProperty("Context", ctx));
        propertyTable.addProperty(new StringProperty("Object Type", selection.isEmpty() ? "Drawing" : "Shape"));

        propertyTable.addProperty(shapeProp);
        SelectionCellComponent scc = propertyTable.getSelectionCellComponent();
        if (scc != null) {
            ShapeMode mode = appService.getShapeMode();
            switch (mode) {
                case Rectangle:
                    if (scc.getCellEditorValue() != RectangleItem) scc.setCellEditorValue(RectangleItem);
                    break;
                case Ellipse:
                    if (scc.getCellEditorValue() != EllipseItem) scc.setCellEditorValue(EllipseItem);
                    break;
                case Line:
                    if (scc.getCellEditorValue() != LineItem) scc.setCellEditorValue(LineItem);
                    break;
                case Text:
                    if (scc.getCellEditorValue() != TextItem) scc.setCellEditorValue(TextItem);
                    break;
                case Select:
                    if (scc.getCellEditorValue() != SelectItem) scc.setCellEditorValue(SelectItem);
                    break;
            }
        }

        if (selection.isEmpty()) {
            addAppearanceFromDefaults(appService);
            addGeometryAndMetaFromDefaults(appService);
            return;
        }

        if (selection.size() == 1) {
            Shape shape = selection.get(0);
            propertyTable.addProperty(new BooleanProperty("is Selected", shape.isSelected()));

            Color stroke = colorOrDefault(shape, appService.getColor(), "getColor", "getStrokeColor");
            Color fill   = colorOrDefault(shape, appService.getFill(),  "getFill", "getFillColor");
            Boolean grad = boolOrDefault(shape, appService.isGradient(), "isGradient", "getGradient");
            Integer th   = intOrDefault(shape, appService.getThickness(), "getThickness", "getLineThickness");

            propertyTable.addProperty(new ColorProperty("Fore color", stroke));
            propertyTable.addProperty(new ColorProperty("Fill color",  fill));
            propertyTable.addProperty(new ColorProperty("Start color", appService.getStartColor()));
            propertyTable.addProperty(new ColorProperty("End color",   appService.getEndColor()));
            propertyTable.addProperty(new BooleanProperty("IsGradient", grad));
            propertyTable.addProperty(new BooleanProperty("IsVisible",  appService.isVisible()));
            propertyTable.addProperty(new IntegerProperty("Line Thickness", th));

            addGeometryAndMetaFromDefaults(appService);
            return;
        }

        Color commonStroke = commonOf(selection, Color.class, "getColor", "getStrokeColor");
        Color commonFill   = commonOf(selection, Color.class, "getFill", "getFillColor");
        Boolean commonGrad = commonOf(selection, Boolean.class, "isGradient", "getGradient");
        Integer commonTh   = commonOf(selection, Integer.class, "getThickness", "getLineThickness");

        if (commonStroke != null) propertyTable.addProperty(new ColorProperty("Fore color", commonStroke));
        if (commonFill   != null) propertyTable.addProperty(new ColorProperty("Fill color",  commonFill));
        if (commonGrad   != null) propertyTable.addProperty(new BooleanProperty("IsGradient", commonGrad));
        if (commonTh     != null) propertyTable.addProperty(new IntegerProperty("Line Thickness", commonTh));

        propertyTable.addProperty(new StringProperty(
                "Note",
                "<html><span style='color:gray'>Multiple selection: edits apply to all. Mixed fields are hidden.</span></html>"
        ));

        propertyTable.addProperty(new IntegerProperty("X Location", appService.getXLocation()));
        propertyTable.addProperty(new IntegerProperty("Y Location", appService.getYLocation()));
        propertyTable.addProperty(new IntegerProperty("Width",      appService.getWidth()));
        propertyTable.addProperty(new IntegerProperty("Height",     appService.getHeight()));
    }

    private List<Shape> selectedShapes(Drawing drawing) {
        List<Shape> out = new ArrayList<>();
        if (drawing == null) return out;
        try {
            for (Shape s : drawing.getShapes()) {
                if (s != null && s.isSelected()) out.add(s);
            }
        } catch (Throwable ignored) {}
        return out;
    }

    private void addAppearanceFromDefaults(AppService appService) {
        propertyTable.addProperty(new ColorProperty("Fore color",  appService.getColor()));
        propertyTable.addProperty(new ColorProperty("Fill color",  appService.getFill()));
        propertyTable.addProperty(new ColorProperty("Start color", appService.getStartColor()));
        propertyTable.addProperty(new ColorProperty("End color",   appService.getEndColor()));
        propertyTable.addProperty(new BooleanProperty("IsGradient", appService.isGradient()));
        propertyTable.addProperty(new BooleanProperty("IsVisible",  appService.isVisible()));
        propertyTable.addProperty(new IntegerProperty("Line Thickness", appService.getThickness()));
    }

    private void addGeometryAndMetaFromDefaults(AppService appService) {
        propertyTable.addProperty(new IntegerProperty("X Location", appService.getXLocation()));
        propertyTable.addProperty(new IntegerProperty("Y Location", appService.getYLocation()));
        propertyTable.addProperty(new IntegerProperty("Width",      appService.getWidth()));
        propertyTable.addProperty(new IntegerProperty("Height",     appService.getHeight()));

        StringProperty txt = new StringProperty("Text",  appService.getText());
        propertyTable.addProperty(txt);
        txt = new StringProperty("Image", appService.getImageFilename());
        propertyTable.addProperty(txt);

        Font f = appService.getFont();
        propertyTable.addProperty(new StringProperty("Font family", f.getFamily()));
        propertyTable.addProperty(new IntegerProperty("Font style", f.getStyle()));
        propertyTable.addProperty(new IntegerProperty("Font size",  f.getSize()));
    }

    private Color colorOrDefault(Shape s, Color def, String... getters) {
        Object v = tryGetAny(s, getters);
        return (v instanceof Color) ? (Color) v : def;
    }

    private Boolean boolOrDefault(Shape s, Boolean def, String... getters) {
        Object v = tryGetAny(s, getters);
        return (v instanceof Boolean) ? (Boolean) v : def;
    }

    private Integer intOrDefault(Shape s, Integer def, String... getters) {
        Object v = tryGetAny(s, getters);
        return (v instanceof Integer) ? (Integer) v : def;
    }

    private <T> T commonOf(List<Shape> shapes, Class<T> type, String... getterNames) {
        if (shapes == null || shapes.isEmpty()) return null;

        boolean haveMethod = false;
        T first = null;
        boolean firstSet = false;

        for (Shape s : shapes) {
            if (s == null) continue;
            Object val = tryGetAny(s, getterNames);
            if (val == null || !type.isInstance(val)) return null;

            haveMethod = true;
            T cast = type.cast(val);
            if (!firstSet) {
                first = cast;
                firstSet = true;
            } else {
                if ((first == null && cast != null) || (first != null && !first.equals(cast))) {
                    return null;
                }
            }
        }
        return haveMethod ? first : null;
    }

    private Object tryGetAny(Object target, String... getterNames) {
        if (target == null) return null;
        for (String g : getterNames) {
            Object v = tryInvoke(target, g);
            if (v != null) return v;
        }
        return null;
    }

    private Object tryInvoke(Object target, String getter) {
        try {
            java.lang.reflect.Method m = target.getClass().getMethod(getter);
            m.setAccessible(true);
            return m.invoke(target);
        } catch (Throwable ignored) {
            return null;
        }
    }
}
