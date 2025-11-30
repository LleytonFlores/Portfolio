package com.gabriel.draw.renderer;

import com.gabriel.drawfx.model.Shape;

import java.awt.*;
import java.util.List;

public class SelectionRenderer {

    public static void renderSelection(Graphics g, List<Shape> selectedShapes) {
        if (selectedShapes == null || selectedShapes.isEmpty()) return;

        if (selectedShapes.size() == 1) {
            renderIndividualHandles(g, selectedShapes.get(0));
        } else {
            renderGroupBoundingBox(g, selectedShapes);
        }
    }

    private static void renderIndividualHandles(Graphics g, Shape shape) {
        Point loc = shape.getLocation();
        int width = shape.getWidth();
        int height = shape.getHeight();
        int r = 7;  //

        g.setColor(shape.getColor());

        // 8 handles
        drawHandle(g, loc.x, loc.y, r);                           //upper left
        drawHandle(g, loc.x + width/2, loc.y, r);                 //middle top
        drawHandle(g, loc.x + width, loc.y, r);                   //upper right
        drawHandle(g, loc.x, loc.y + height/2, r);                //middle left
        drawHandle(g, loc.x + width, loc.y + height/2, r);        //middle right
        drawHandle(g, loc.x, loc.y + height, r);                  //lower left
        drawHandle(g, loc.x + width/2, loc.y + height, r);        //middle bottom
        drawHandle(g, loc.x + width, loc.y + height, r);          //lower right
    }

    private static void renderGroupBoundingBox(Graphics g, List<Shape> shapes) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Shape s : shapes) {
            Point loc = s.getLocation();
            minX = Math.min(minX, loc.x);
            minY = Math.min(minY, loc.y);
            maxX = Math.max(maxX, loc.x + s.getWidth());
            maxY = Math.max(maxY, loc.y + s.getHeight());
        }

        int width = maxX - minX;
        int height = maxY - minY;
        int r = 7;

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 120, 255));
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                10, new float[]{5, 5}, 0));
        g2.drawRect(minX, minY, width, height);

        g2.setStroke(new BasicStroke(1));
        drawHandle(g, minX, minY, r);                    //upper left
        drawHandle(g, minX + width/2, minY, r);          //middle top
        drawHandle(g, minX + width, minY, r);            //upper right
        drawHandle(g, minX, minY + height/2, r);         //middle left
        drawHandle(g, minX + width, minY + height/2, r); //middle right
        drawHandle(g, minX, minY + height, r);           //lower left
        drawHandle(g, minX + width/2, minY + height, r); //middle bottom
        drawHandle(g, minX + width, minY + height, r);   //lower right
    }

    private static void drawHandle(Graphics g, int x, int y, int r) {
        //fill blue square with white square
        g.setColor(Color.WHITE);
        g.fillRect(x - r, y - r, 2*r, 2*r);
        g.setColor(new Color(0, 120, 255));
        g.drawRect(x - r, y - r, 2*r, 2*r);
    }
}
