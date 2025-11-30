package com.gabriel.draw.model;

import com.gabriel.draw.renderer.EllipseRenderer;
import com.gabriel.draw.renderer.ImageRenderer;
import com.gabriel.drawfx.model.Shape;
import lombok.Data;

import java.awt.*;

@Data
public class Image extends Shape {
    public Image(Point start){
        super(start);
        setRendererService(new ImageRenderer());
    }

    public Image(Point start, Point end, String imageFilename){
        super(start, end );
        setRendererService(new ImageRenderer());
    }
    public Image(Point start, int width, int height) {
        super(start, width, height);
        this.setRendererService(new ImageRenderer());
    }
}
