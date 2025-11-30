package com.gabriel.draw.model;

import com.gabriel.draw.renderer.RectangleRenderer;
import com.gabriel.drawfx.model.Shape;

import java.awt.*;

public class Rectangle extends Shape {

    public Rectangle(Point start) {
        super(start);
        this.setRendererService(new RectangleRenderer());
    }

    public Rectangle(Point start, Point end){
        super(start, end);
        this.setRendererService(new RectangleRenderer());
    }
    public Rectangle(Point start, int width, int height){
        super(start, width, height);
        this.setRendererService(new RectangleRenderer());
    }
}
