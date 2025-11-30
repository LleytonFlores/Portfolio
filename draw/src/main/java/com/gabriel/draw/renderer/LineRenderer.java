package com.gabriel.draw.renderer;

import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.renderer.ShapeRenderer;

import java.awt.*;

public class LineRenderer extends ShapeRenderer {

    @Override
    public void render(Graphics g, Shape shape, boolean xor) {
        if(!shape.isVisible()){
            return;
        }

        int x1 = shape.getLocation().x;
        int y1 = shape.getLocation().y;

        //endpoint might be negative, might change later
        int x2 = x1 + shape.getWidth();
        int y2 = y1 + shape.getHeight();

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(shape.getThickness()));

        if(xor) {
            g2.setXORMode(shape.getColor());
        }
        else {
            g2.setColor(shape.getColor());
        }

        g2.drawLine(x1, y1, x2, y2);

        if (!xor) {
            super.render(g, shape, xor);
        }
    }
}