package com.gabriel.drawfx.util;

import com.gabriel.drawfx.model.Shape;

public class Normalizer {
    public static void normalize(Shape shape){
        if (shape.getClass().getSimpleName().equals("Line")) {
            return;
        }

        int w = shape.getWidth();
        int h = shape.getHeight();

         if(shape.getWidth() < 0){
            shape.getLocation().x += w;
            shape.setWidth(-w);
        }
        if(shape.getHeight()<0){
            shape.getLocation().y += h;
            shape.setHeight(-h);
        }
    }
}