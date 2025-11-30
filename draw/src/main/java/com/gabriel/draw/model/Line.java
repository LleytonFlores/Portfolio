package com.gabriel.draw.model;


import com.gabriel.draw.renderer.ImageRenderer;
import com.gabriel.draw.renderer.LineRenderer;
import lombok.Data;
import com.gabriel.drawfx.model.Shape;
import java.awt.*;


@Data
public class Line extends Shape {

    public Line(Point start, Point end) {
        super(start, end);
        this.setColor(Color.RED);
        this.setRendererService(new LineRenderer());
    }

    public Line(Point start) {
        super(start);
        this.setColor(Color.RED);
        this.setRendererService(new LineRenderer());
    }

    public Line(Point start, int width, int height) {
        super(start, width, height);
        this.setRendererService(new LineRenderer());
    }
}
