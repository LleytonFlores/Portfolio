package com.gabriel.drawfx.service;

import com.gabriel.drawfx.SelectionMode;
import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.model.Shape;

import java.awt.*;
import java.util.List;

public final class SearchService {

    public Shape getSelectedShape(Drawing drawing){
        return drawing.getSelectedShape();
    }

    public void search(AppService appService, Point p) {
        search(appService, p, true);
    }

    public void search(AppService appService, Point p, boolean single) {
        Drawing drawing = appService.getDrawing();
        drawing.setSelectedShape(null);
        List<Shape> shapes = drawing.getShapes();
        int r = appService.getSearchRadius();

        for (Shape shape : shapes) {
            Point loc = shape.getLocation();
            int width = shape.getWidth();
            int height = shape.getHeight();

            int minX = width >= 0 ? loc.x : loc.x + width;
            int maxX = width >= 0 ? loc.x + width : loc.x;
            int minY = height >= 0 ? loc.y : loc.y + height;
            int maxY = height >= 0 ? loc.y + height : loc.y;

            if (p.x >= minX - r && p.x <= maxX + r &&
                    p.y >= minY - r && p.y <= maxY + r) {

                if (found(shape, p, loc.x, loc.y, r)) {
                    shape.setSelectionMode(SelectionMode.UpperLeft);
                } else if (found(shape, p, loc.x, loc.y + height / 2, r)) {
                    shape.setSelectionMode(SelectionMode.MiddleLeft);
                } else if (found(shape, p, loc.x, loc.y + height, r)) {
                    shape.setSelectionMode(SelectionMode.LowerLeft);
                } else if (found(shape, p, loc.x + width / 2, loc.y, r)) {
                    shape.setSelectionMode(SelectionMode.MiddleTop);
                } else if (found(shape, p, loc.x + width, loc.y, r)) {
                    shape.setSelectionMode(SelectionMode.UpperRight);
                } else if (found(shape, p, loc.x + width, loc.y + height / 2, r)) {
                    shape.setSelectionMode(SelectionMode.MiddleRight);
                } else if (found(shape, p, loc.x + width, loc.y + height, r)) {
                    shape.setSelectionMode(SelectionMode.LowerRight);
                } else if (found(shape, p, loc.x + width / 2, loc.y + height, r)) {
                    shape.setSelectionMode(SelectionMode.MiddleBottom);
                } else {
                    shape.setSelectionMode(SelectionMode.None);
                }
                shape.setSelected(true);
                drawing.setSelectedShape(shape);
            }
            else {
                if (single) {
                    if(shape.isSelected()) {
                        shape.setSelected(false);
                    }
                }
            }
        }
    }

    boolean found(Shape shape, Point p, int x, int y, int r){
        return (p.x > x - r && p.x < x + r && p.y > y - r && p.y < y + r);
    }
}