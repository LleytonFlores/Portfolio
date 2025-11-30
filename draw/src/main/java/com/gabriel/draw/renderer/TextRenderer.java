package com.gabriel.draw.renderer;

import com.gabriel.draw.model.Text;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.renderer.ShapeRenderer;

import java.awt.*;

public class TextRenderer extends ShapeRenderer {

    @Override
    public void render(Graphics g, Shape shape, boolean xor) {
        if(!shape.isVisible()){
            return;
        }

        Text text = (Text) shape;

        int x = shape.getLocation().x;
        int y = shape.getLocation().y;
        int width = shape.getWidth();
        int height = shape.getHeight();

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(shape.getThickness()));

        if (xor) {
            g2.setXORMode(shape.getColor());
            g2.drawRect(x, y, width, height);
        } else {
            //get font from shape
            Font originalFont = shape.getFont();
            if (originalFont == null) {
                originalFont = new Font("Arial", Font.PLAIN, 12);
            }

            //size limits for safety
            int fontSize = originalFont.getSize();
            if (fontSize < 8) fontSize = 8;           //minimum readable size
            if (fontSize > 500) fontSize = 500;       //maximum reasonable size

            //create display font with chosen size
            Font displayFont = new Font(originalFont.getFamily(), originalFont.getStyle(), fontSize);
            g2.setFont(displayFont);

            //text metric for centering
            FontMetrics metrics = g2.getFontMetrics(displayFont);
            String textStr = shape.getText();
            if (textStr == null || textStr.isEmpty()) {
                textStr = "";
            }

            //calculate centered position
            int textWidth = metrics.stringWidth(textStr);
            int textX = x + (width - textWidth) / 2;
            int textY = y + (height - metrics.getHeight()) / 2 + metrics.getAscent();

            //apply gradient or solid color
            if (shape.isGradient()) {
                Color startColor = shape.getStartColor();
                Color endColor = shape.getEndColor();

                if (startColor == null) startColor = Color.WHITE;
                if (endColor == null) endColor = Color.BLACK;

                GradientPaint gp = new GradientPaint(
                        x + shape.getStart().x,
                        y + shape.getStart().y,
                        startColor,
                        x + width + shape.getEnd().x,
                        y + height + shape.getEnd().y,
                        endColor
                );
                g2.setPaint(gp);
            } else {
                g2.setColor(shape.getColor());
            }

            //draw the text
            g2.drawString(textStr, textX, textY);

            //draw bounding box
            g2.setColor(new Color(200, 200, 200, 100));
            g2.drawRect(x, y, width, height);

            super.render(g, shape, xor);
        }
    }
}