package com.gabriel.draw.renderer;

import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.renderer.ShapeRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class RectangleRenderer extends ShapeRenderer {

    @Override
    public void render(Graphics g, Shape shape, boolean xor) {
        if(!shape.isVisible()){
            return;
        }

        int x = shape.getLocation().x;
        int y = shape.getLocation().y;
        int width = shape.getWidth();
        int height = shape.getHeight();

        if(xor) {
            g.setXORMode(shape.getColor());
        } else {
            g.setColor(shape.getColor());
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(shape.getThickness()));

        if (xor) {
            g2.setXORMode(shape.getColor());
        } else {
            g2.setColor(shape.getColor());

            // CHECK IF SHAPE HAS AN IMAGE
            String imageFilename = shape.getImageFilename();
            boolean hasImage = false;

            if (imageFilename != null && !imageFilename.isEmpty()) {
                try {
                    File imageFile = new File(imageFilename);
                    if (imageFile.exists()) {
                        BufferedImage img = ImageIO.read(imageFile);
                        if (img != null) {
                            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                            java.awt.Shape originalClip = g2.getClip();

                            Rectangle clipRect = new Rectangle(x, y, width, height);
                            g2.setClip(clipRect);

                            //draw image
                            g2.drawImage(img, x, y, width, height, null);

                            g2.setClip(originalClip);

                            hasImage = true;
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("Error loading image in rectangle: " + ex.getMessage());
                }
            }

            // Draw fill only if no image
            if (shape.getFill() != null && !hasImage) {
                if(shape.isGradient()) {
                    Color startColor = shape.getStartColor();
                    Color endColor = shape.getEndColor();

                    if (startColor == null) startColor = Color.WHITE;
                    if (endColor == null) endColor = Color.BLACK;

                    GradientPaint gp = new GradientPaint(
                            shape.getLocation().x + shape.getStart().x,
                            shape.getLocation().y + shape.getStart().y,
                            startColor,
                            shape.getLocation().x + width + shape.getEnd().x,
                            shape.getLocation().y + shape.getEnd().y + shape.getHeight(),
                            endColor
                    );
                    g2.setPaint(gp);
                } else {
                    g2.setColor(shape.getFill());
                }
                g2.fillRect(x, y, width, height);
                g2.setColor(shape.getColor());
            }
        }

        g2.drawRect(x, y, width, height);

        // --- START: Draw Text ---
        // (Copied from TextRenderer.java)
        String textStr = shape.getText();
        if (textStr != null && !textStr.isEmpty() && !xor) {

            Font originalFont = shape.getFont();
            if (originalFont == null) {
                originalFont = new Font("Arial", Font.PLAIN, 12);
            }

            int fontSize = originalFont.getSize();
            if (fontSize < 8) fontSize = 8;
            if (fontSize > 500) fontSize = 500;

            Font displayFont = new Font(originalFont.getFamily(), originalFont.getStyle(), fontSize);
            g2.setFont(displayFont);

            FontMetrics metrics = g2.getFontMetrics(displayFont);

            int textWidth = metrics.stringWidth(textStr);
            int textX = x + (width - textWidth) / 2;
            int textY = y + (height - metrics.getHeight()) / 2 + metrics.getAscent();

            // Apply gradient or solid color
            if (shape.isGradient()) {
                Color startColor = shape.getStartColor();
                Color endColor = shape.getEndColor();
                if (startColor == null) startColor = Color.WHITE;
                if (endColor == null) endColor = Color.BLACK;

                GradientPaint gp = new GradientPaint(
                        x + shape.getStart().x, y + shape.getStart().y, startColor,
                        x + width + shape.getEnd().x, y + height + shape.getEnd().y, endColor
                );
                g2.setPaint(gp);
            } else {
                g2.setColor(shape.getColor()); // Uses the shape's main color for text
            }

            g2.drawString(textStr, textX, textY);

            // Reset color just in case
            g2.setColor(shape.getColor());
        }
        // --- END: Draw Text ---

        super.render(g, shape, xor);
    }
}