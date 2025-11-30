package com.gabriel.draw.model;

import com.gabriel.draw.renderer.EllipseRenderer;
import com.gabriel.draw.renderer.RectangleRenderer;
import com.gabriel.drawfx.model.Shape;

import java.awt.*;

public class Ellipse extends Shape {

    public Ellipse(Point start, Point end) {
        super(start, end);
        this.setRendererService(new EllipseRenderer());
    }

    public Ellipse(Point start) {
        super(start);
        this.setRendererService(new EllipseRenderer());
    }

    public Ellipse(Point start, int width, int height) {
        super(start, width, height);
        this.setRendererService(new EllipseRenderer());
    }
}