package com.gabriel.draw.model;

import java.awt.*;

import com.gabriel.draw.renderer.RectangleRenderer;
import com.gabriel.draw.renderer.TextRenderer;
import com.gabriel.drawfx.model.Shape;
import lombok.Data;

public class Text extends  Shape {

    public Text(Point location) {
        super(location);
        this.setRendererService(new TextRenderer());
    }

    public Text(Point start, int width, int height) {
        super(start, width, height);
        this.setRendererService(new TextRenderer());
    }
}
