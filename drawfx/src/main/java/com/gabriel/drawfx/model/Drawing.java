package com.gabriel.drawfx.model;

import com.gabriel.drawfx.DrawMode;
import com.gabriel.drawfx.ShapeMode;
import lombok.Data;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class Drawing {
    Point location;
    int id;
    private String filename = null;
    private String imageFilename = null;

    private Color color;
    private Color fill;
    private Color startColor;
    private Color endColor;
    private boolean gradient;
    private Font font;

    private DrawMode drawMode = DrawMode.Idle;
    private ShapeMode shapeMode = ShapeMode.Select;

    private Point start;
    private Point end;
    int width = 0;
    int height = 0;

    List<Shape> shapes;

    private final List<Shape> selectedShapes = new ArrayList<>();
    private Shape primarySelectedShape = null;

    private String text = "";

    private int searchRadius = 5;
    private int thickness = 1;
    private boolean visible = true;

    public Drawing() {
        location  = new Point(0,0);
        color = Color.RED;
        fill = Color.WHITE;
        font = new Font("Serif", Font.BOLD, 24);
        shapes = new ArrayList<>();
        this.start = new Point(0,0);
        this.end = new Point(100,0);
    }

    public Shape getSelectedShape() {
        return primarySelectedShape;
    }

    public void setSelectedShape(Shape s) {
        clearSelection();
        if (s != null) addToSelection(s, true);
    }

    public List<Shape> getSelectedShapes() {
        return Collections.unmodifiableList(selectedShapes);
    }

    public boolean hasSelection() {
        return !selectedShapes.isEmpty();
    }

    public void addToSelection(Shape s, boolean makePrimary) {
        if (s == null) return;
        if (!selectedShapes.contains(s)) {
            selectedShapes.add(s);
            s.setSelected(true);
        }
        if (makePrimary || primarySelectedShape == null) {
            primarySelectedShape = s;
        }
    }

    public void toggleSelection(Shape s) {
        if (s == null) return;
        if (selectedShapes.contains(s)) {
            removeFromSelection(s);
        } else {
            addToSelection(s, true);
        }
    }

    public void removeFromSelection(Shape s) {
        if (s == null) return;
        if (selectedShapes.remove(s)) {
            s.setSelected(false);
            if (s == primarySelectedShape) {
                primarySelectedShape = selectedShapes.isEmpty() ? null : selectedShapes.get(selectedShapes.size() - 1);
            }
        }
    }

    public void clearSelection() {
        for (Shape sh : selectedShapes) {
            sh.setSelected(false);
        }
        selectedShapes.clear();
        primarySelectedShape = null;
    }

    public int getR() {
        return this.searchRadius;
    }

    public void setR(int r) {
        this.searchRadius = r;
    }
}