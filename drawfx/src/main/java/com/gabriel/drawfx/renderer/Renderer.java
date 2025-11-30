package com.gabriel.drawfx.renderer;

import com.gabriel.drawfx.model.Shape;

import java.awt.*;

public interface Renderer {
        void render(Graphics g, Shape shape, boolean xor);
}
