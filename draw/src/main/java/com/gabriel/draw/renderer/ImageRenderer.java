package com.gabriel.draw.renderer;

import com.gabriel.draw.model.Image;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.renderer.ShapeRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageRenderer extends ShapeRenderer {

    @Override
    public void render(Graphics g, Shape shape, boolean xor) {
        if(!shape.isVisible()){
            return;
        }

        Point start = shape.getLocation();
        int x = start.x;
        int y = start.y;
        int width = shape.getWidth();
        int height = shape.getHeight();

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(shape.getThickness()));

        String imageFilename = ((Image) shape).getImageFilename();

        if (imageFilename == null || imageFilename.isEmpty()) {
            if(xor) {
                g2.setXORMode(shape.getColor());
                g2.drawRect(x, y, width, height);
            } else {
                g2.setColor(new Color(240, 240, 240));
                g2.fillRect(x, y, width, height);
                g2.setColor(Color.DARK_GRAY);
                g2.drawRect(x, y, width, height);

                Font oldFont = g2.getFont();
                g2.setFont(new Font("Arial", Font.PLAIN, 12));
                String msg = "No Image";
                FontMetrics fm = g2.getFontMetrics();
                int msgWidth = fm.stringWidth(msg);
                g2.drawString(msg, x + (width - msgWidth) / 2, y + height / 2);
                g2.setFont(oldFont);
            }
            return;
        }

        File input = new File(imageFilename);

        try {
            if (!input.exists()) {
                throw new IOException("File not found: " + imageFilename);
            }

            java.awt.Image bImage = ImageIO.read(input);

            if (bImage == null) {
                throw new IOException("Failed to read image");
            }

            if(xor) {
                g2.setXORMode(shape.getColor());
                g2.drawRect(x, y, width, height);
            } else {
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);

                // FIXED: Use java.awt.Shape instead of Shape
                java.awt.Shape originalClip = g2.getClip();

                Rectangle clipRect = new Rectangle(x, y, width, height);
                g2.setClip(clipRect);

                BufferedImage img = resizeImage((BufferedImage) bImage, width, height);
                g2.drawImage(img, x, y, null);

                // FIXED: originalClip is now properly typed
                g2.setClip(originalClip);

                g2.setColor(shape.getColor());
                g2.drawRect(x, y, width, height);

                super.render(g, shape, xor);
            }
        } catch(IOException ex) {
            System.err.println("Error loading image: " + imageFilename);
            System.err.println("  " + ex.getMessage());

            // Draw error placeholder
            g2.setColor(new Color(255, 220, 220));
            g2.fillRect(x, y, width, height);
            g2.setColor(Color.RED);
            g2.drawRect(x, y, width, height);

            // Draw error message
            Font oldFont = g2.getFont();
            g2.setFont(new Font("Arial", Font.PLAIN, 10));
            String msg = "Image Error";
            FontMetrics fm = g2.getFontMetrics();
            int msgWidth = fm.stringWidth(msg);
            g2.drawString(msg, x + (width - msgWidth) / 2, y + height / 2 - 5);

            String filename = new File(imageFilename).getName();
            if (filename.length() > 20) {
                filename = filename.substring(0, 17) + "...";
            }
            msgWidth = fm.stringWidth(filename);
            g2.drawString(filename, x + (width - msgWidth) / 2, y + height / 2 + 10);
            g2.setFont(oldFont);

        } catch(Exception ex) {
            System.err.println("Unexpected error rendering image: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        if (targetWidth <= 0) targetWidth = 1;
        if (targetHeight <= 0) targetHeight = 1;

        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();

        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    public static BufferedImage rotate(BufferedImage image, double angle) {
        double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
        int w = image.getWidth(), h = image.getHeight();
        int neww = (int)Math.floor(w*cos+h*sin), newh = (int) Math.floor(h * cos + w * sin);
        GraphicsConfiguration gc = getDefaultConfiguration();
        BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
        Graphics2D g = result.createGraphics();
        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(angle, w / 2, h / 2);
        g.drawRenderedImage(image, null);
        g.dispose();
        return result;
    }

    private static GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }
}