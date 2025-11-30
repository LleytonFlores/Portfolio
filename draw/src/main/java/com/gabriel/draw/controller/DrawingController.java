package com.gabriel.draw.controller;

import com.gabriel.draw.command.DeleteShapeCommand;
import com.gabriel.draw.command.MoveShapeCommand;
import com.gabriel.draw.command.ScaleShapeCommand;
import com.gabriel.draw.command.SetTextCommand;
import com.gabriel.draw.component.PropertySheet;
import com.gabriel.draw.model.Ellipse;
import com.gabriel.draw.model.Image;
import com.gabriel.draw.model.Line;
import com.gabriel.draw.model.Text;
import com.gabriel.draw.view.DrawingStatusPanel;
import com.gabriel.draw.view.DrawingView;
import com.gabriel.drawfx.DrawMode;
import com.gabriel.drawfx.SelectionMode;
import com.gabriel.drawfx.ShapeMode;
import com.gabriel.drawfx.command.CommandService;
import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;
import com.gabriel.drawfx.util.Normalizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class DrawingController implements MouseListener, MouseMotionListener, KeyListener {

    private final AppService appService;
    private final Drawing drawing;

    private DrawingView drawingView;
    private PropertySheet propertySheet;
    private DrawingStatusPanel drawingStatusPanel;

    private Point start;
    private Point end;
    private Shape currentShape;

    private boolean isMovingShape = false;
    private boolean isScalingGroup = false;
    private SelectionMode groupScaleMode = SelectionMode.None;

    private Rectangle originalGroupBounds;
    private List<ShapeTransform> originalTransforms;

    private MoveShapeCommand currentMoveCommand;
    private ScaleShapeCommand currentScaleCommand;

    private Point dragStartPoint;
    private List<ShapePosition> originalPositionsBeforeDrag;

    // --- NEW ---
    // Fields to manage the in-place text editor
    private JTextField inlineEditor;
    private Shape editingShape;

    public DrawingController(AppService appService, DrawingView drawingView) {
        this.appService = appService;
        this.drawing = appService.getDrawing();
        setDrawingView(drawingView);
    }

    public DrawingController(AppService appService,
                             DrawingView drawingView,
                             PropertySheet propertySheet,
                             DrawingStatusPanel drawingStatusPanel) {
        this(appService, drawingView);
        setPropertySheet(propertySheet);
        setDrawingStatusPanel(drawingStatusPanel);
    }

    public void setDrawingView(DrawingView view) {
        this.drawingView = view;
        if (this.drawingView != null) {
            this.drawingView.addMouseListener(this);
            this.drawingView.addMouseMotionListener(this);
            this.drawingView.addKeyListener(this);
            this.drawingView.setFocusable(true);
            this.drawingView.requestFocusInWindow();
        }
    }

    public void setPropertySheet(PropertySheet sheet) {
        this.propertySheet = sheet;
    }

    public void setDrawingStatusPanel(DrawingStatusPanel panel) {
        this.drawingStatusPanel = panel;
    }

    private Shape findTopmostShapeAt(Point p) {
        List<Shape> shapes = drawing.getShapes();
        for (int i = shapes.size() - 1; i >= 0; i--) {
            Shape s = shapes.get(i);
            if (!s.isVisible()) continue;
            java.awt.Rectangle r = new java.awt.Rectangle(
                    s.getLocation().x, s.getLocation().y, s.getWidth(), s.getHeight()
            );
            if (r.contains(p)) return s;
        }
        return null;
    }

    private boolean isToggleModifier(InputEvent e) {
        if (e instanceof MouseEvent me) {
            return me.isControlDown() || me.isShiftDown() || me.isMetaDown();
        }
        return false;
    }

    private Rectangle getSelectionBounds() {
        List<Shape> selected = appService.getSelectedShapes();
        if (selected.isEmpty()) return null;

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Shape s : selected) {
            Point loc = s.getLocation();
            minX = Math.min(minX, loc.x);
            minY = Math.min(minY, loc.y);
            maxX = Math.max(maxX, loc.x + s.getWidth());
            maxY = Math.max(maxY, loc.y + s.getHeight());
        }

        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    private SelectionMode getGroupHandleAt(Point p) {
        Rectangle bounds = getSelectionBounds();
        if (bounds == null) return SelectionMode.None;

        int r = 10;
        int x = bounds.x;
        int y = bounds.y;
        int w = bounds.width;
        int h = bounds.height;

        if (isNear(p, x, y, r)) return SelectionMode.UpperLeft;
        if (isNear(p, x + w/2, y, r)) return SelectionMode.MiddleTop;
        if (isNear(p, x + w, y, r)) return SelectionMode.UpperRight;
        if (isNear(p, x, y + h/2, r)) return SelectionMode.MiddleLeft;
        if (isNear(p, x + w, y + h/2, r)) return SelectionMode.MiddleRight;
        if (isNear(p, x, y + h, r)) return SelectionMode.LowerLeft;
        if (isNear(p, x + w/2, y + h, r)) return SelectionMode.MiddleBottom;
        if (isNear(p, x + w, y + h, r)) return SelectionMode.LowerRight;

        return SelectionMode.None;
    }

    private boolean isNear(Point p, int x, int y, int radius) {
        return Math.abs(p.x - x) <= radius && Math.abs(p.y - y) <= radius;
    }

    private void captureOriginalTransforms() {
        originalTransforms = new java.util.ArrayList<>();
        for (Shape s : appService.getSelectedShapes()) {
            originalTransforms.add(new ShapeTransform(s));
        }
    }

    private void scaleGroup(Rectangle newBounds) {
        if (originalGroupBounds == null || originalTransforms == null) return;

        double scaleX = (double) newBounds.width / originalGroupBounds.width;
        double scaleY = (double) newBounds.height / originalGroupBounds.height;

        List<Shape> selected = appService.getSelectedShapes();
        for (int i = 0; i < selected.size() && i < originalTransforms.size(); i++) {
            Shape s = selected.get(i);
            ShapeTransform orig = originalTransforms.get(i);

            int relX = orig.x - originalGroupBounds.x;
            int relY = orig.y - originalGroupBounds.y;

            s.getLocation().x = newBounds.x + (int)(relX * scaleX);
            s.getLocation().y = newBounds.y + (int)(relY * scaleY);
            s.setWidth((int)(orig.width * scaleX));
            s.setHeight((int)(orig.height * scaleY));
        }
    }

    // --- REMOVED ---
    // The old showTextInputDialog method is no longer needed.
    // We replaced it with startInlineEdit() and commitInlineEdit()
    /*
    private void showTextInputDialog(Shape shape) {
        ...
    }
    */

    @Override
    public void mouseClicked(MouseEvent e) {
        // Check for double-click
        if (e.getClickCount() == 2) {
            // Find which shape was clicked
            Shape clickedShape = findTopmostShapeAt(e.getPoint());

            if (clickedShape != null) {
                // --- CHANGED ---
                // Show in-place editor instead of pop-up
                startInlineEdit(clickedShape);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // --- NEW ---
        // If we are editing text, a click outside should
        // commit the edit and stop the click from doing anything else.
        if (inlineEditor != null) {
            commitInlineEdit();
            return;
        }

        //For debugging
        System.out.println("-----MOUSE PRESSED-----");
        System.out.println("Current mode: " + appService.getShapeMode());
        System.out.println("DrawMode: " + appService.getDrawMode());

        if (drawingView != null) {
            drawingView.requestFocusInWindow();
        }

        // ... rest of your mousePressed code is unchanged ...
        if (appService.getDrawMode() != DrawMode.Idle) return;

        start = e.getPoint();
        ShapeMode mode = appService.getShapeMode();

        if (mode != ShapeMode.Select) {
            Shape hit = findTopmostShapeAt(start);
            if (hit != null) {
                appService.setShapeMode(ShapeMode.Select);
                drawing.setSelectedShape(hit);
                if (propertySheet != null) propertySheet.populateTable(appService);
                if (drawingView != null) drawingView.repaint();
                return;
            }
        }

        if (mode == ShapeMode.Select) {
            List<Shape> selected = appService.getSelectedShapes();

            if (selected.size() > 1) {
                SelectionMode handle = getGroupHandleAt(start);
                if (handle != SelectionMode.None) {
                    isScalingGroup = true;
                    isMovingShape = false;
                    groupScaleMode = handle;
                    originalGroupBounds = getSelectionBounds();
                    captureOriginalTransforms();
                    currentScaleCommand = new ScaleShapeCommand(appService, selected);
                    currentScaleCommand.captureOriginalState();
                    appService.setDrawMode(DrawMode.MousePressed);
                    return;
                }

                Rectangle bounds = getSelectionBounds();
                if (bounds != null && bounds.contains(start)) {
                    isMovingShape = true;
                    isScalingGroup = false;
                    dragStartPoint = new Point(start.x, start.y);
                    captureOriginalPositionsForMove(selected);
                    appService.setDrawMode(DrawMode.MousePressed);
                    return;
                }
            }

            Shape hit = findTopmostShapeAt(start);

            if (hit != null && hit.isSelected()) {
                if (selected.size() == 1) {
                    SelectionMode handle = getIndividualHandleAt(hit, start);
                    if (handle != SelectionMode.None) {
                        hit.setSelectionMode(handle);
                        isScalingGroup = true;
                        isMovingShape = false;
                        groupScaleMode = handle;
                        originalGroupBounds = new Rectangle(hit.getLocation().x, hit.getLocation().y,
                                hit.getWidth(), hit.getHeight());
                        captureOriginalTransforms();
                        currentScaleCommand = new ScaleShapeCommand(appService, hit);
                        currentScaleCommand.captureOriginalState();
                    } else {
                        isMovingShape = true;
                        isScalingGroup = false;
                        dragStartPoint = new Point(start.x, start.y);
                        captureOriginalPositionsForMove(java.util.Arrays.asList(hit));
                    }
                } else {
                    isMovingShape = true;
                    isScalingGroup = false;
                    dragStartPoint = new Point(start.x, start.y);
                    captureOriginalPositionsForMove(selected);
                }
                appService.setDrawMode(DrawMode.MousePressed);

            } else if (hit != null) {
                if (isToggleModifier(e)) {
                    drawing.toggleSelection(hit);
                } else {
                    drawing.setSelectedShape(hit);
                }
                if (propertySheet != null) propertySheet.populateTable(appService);
                if (drawingView != null) drawingView.repaint();

            } else {
                if (!isToggleModifier(e)) {
                    drawing.clearSelection();
                    if (propertySheet != null) propertySheet.populateTable(appService);
                    if (drawingView != null) drawingView.repaint();
                }
            }
            return;
        }

        switch (mode) {
            case Line:
                currentShape = new Line(start);
                currentShape.setColor(appService.getColor());
                currentShape.setThickness(appService.getThickness());
                break;
            case Rectangle:
                currentShape = new com.gabriel.draw.model.Rectangle(start);
                currentShape.setColor(appService.getColor());
                currentShape.setFill(appService.getFill());
                break;
            case Ellipse:
                currentShape = new Ellipse(start);
                currentShape.setColor(appService.getColor());
                currentShape.setFill(appService.getFill());
                break;
            case Image:
                currentShape = new Image(start);
                currentShape.setImageFilename(drawing.getImageFilename());
                currentShape.setColor(appService.getColor());
                currentShape.setThickness(appService.getThickness());
                break;
            case Text:
                currentShape = new Text(start);
                currentShape.setColor(appService.getColor());
                currentShape.setThickness(appService.getThickness());
                currentShape.setWidth(100);
                currentShape.setHeight(50);
                // --- CHANGED ---
                // I set the default text to "" based on your previous request
                currentShape.setText("");
                currentShape.setFont(drawing.getFont());
                break;
            default:
                currentShape = null;
                break;
        }

        System.out.println("Created currentShape: " + currentShape);
        appService.setDrawMode(DrawMode.MousePressed);
    }

    private SelectionMode getIndividualHandleAt(Shape shape, Point p) {
        int r = 10;
        Point loc = shape.getLocation();
        int width = shape.getWidth();
        int height = shape.getHeight();

        if (isNear(p, loc.x, loc.y, r)) return SelectionMode.UpperLeft;
        if (isNear(p, loc.x + width/2, loc.y, r)) return SelectionMode.MiddleTop;
        if (isNear(p, loc.x + width, loc.y, r)) return SelectionMode.UpperRight;
        if (isNear(p, loc.x, loc.y + height/2, r)) return SelectionMode.MiddleLeft;
        if (isNear(p, loc.x + width, loc.y + height/2, r)) return SelectionMode.MiddleRight;
        if (isNear(p, loc.x, loc.y + height, r)) return SelectionMode.LowerLeft;
        if (isNear(p, loc.x + width/2, loc.y + height, r)) return SelectionMode.MiddleBottom;
        if (isNear(p, loc.x + width, loc.y + height, r)) return SelectionMode.LowerRight;

        return SelectionMode.None;
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        // --- NEW ---
        // Don't do anything if we are editing text
        if (inlineEditor != null) return;

        if (appService.getDrawMode() != DrawMode.MousePressed) return;

        end = e.getPoint();
        ShapeMode mode = appService.getShapeMode();

        if (mode == ShapeMode.Select) {
            if (isScalingGroup) {
                for (Shape s : appService.getSelectedShapes()) {
                    Normalizer.normalize(s);
                    s.setSelectionMode(SelectionMode.None);
                }
                if (currentScaleCommand != null) {
                    currentScaleCommand.captureNewState();
                    CommandService.ExecuteCommand(currentScaleCommand);
                    currentScaleCommand = null;
                }
                isScalingGroup = false;
                groupScaleMode = SelectionMode.None;
                originalGroupBounds = null;
                originalTransforms = null;

            } else if (isMovingShape) {
                restoreOriginalPositions();
                List<Shape> selectedShapes = appService.getSelectedShapes();
                if (!selectedShapes.isEmpty() && dragStartPoint != null && end != null) {
                    MoveShapeCommand moveCommand = new MoveShapeCommand(appService, selectedShapes, dragStartPoint, end);
                    CommandService.ExecuteCommand(moveCommand);
                }
                isMovingShape = false;
                dragStartPoint = null;
                originalPositionsBeforeDrag = null;
            }

            if (drawingView != null) drawingView.repaint();
            if (propertySheet != null) propertySheet.populateTable(appService);
            appService.setDrawMode(DrawMode.Idle);
            return;
        }

        if (currentShape != null) {
            appService.scale(currentShape, end);

            // --- CHANGED ---
            // Set default text properties (to "")
            if (currentShape.getText() == null) {
                currentShape.setText("");
            }
            currentShape.setFont(drawing.getFont());

            currentShape.setGradient(drawing.isGradient());
            currentShape.setFill(drawing.getFill());
            currentShape.setStartColor(drawing.getStartColor());
            currentShape.setEndColor(drawing.getEndColor());

            Normalizer.normalize(currentShape);
            appService.create(currentShape);
            currentShape.setSelected(true);
            drawing.setSelectedShape(currentShape);
            currentShape = null;

            if (drawingView != null) drawingView.repaint();
            if (propertySheet != null) propertySheet.populateTable(appService);
        }
        appService.setDrawMode(DrawMode.Idle);
    }

    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) {
        // --- NEW ---
        // Don't allow dragging shapes while editing text
        if (inlineEditor != null) return;

        if (appService.getDrawMode() != DrawMode.MousePressed) return;

        end = e.getPoint();
        ShapeMode mode = appService.getShapeMode();

        if (mode == ShapeMode.Select) {
            if (isScalingGroup) {
                Rectangle newBounds = calculateNewBounds(originalGroupBounds, groupScaleMode, start, end);
                scaleGroup(newBounds);
                if (drawingView != null) drawingView.repaint();

            } else if (isMovingShape) {
                int dx = end.x - start.x;
                int dy = end.y - start.y;
                List<Shape> selectedShapes = appService.getSelectedShapes();
                for (Shape shape : selectedShapes) {
                    shape.getLocation().x += dx;
                    shape.getLocation().y += dy;
                }
                if (drawingView != null) drawingView.repaint();
                start = end;
            }
            return;
        }

        if (currentShape != null) {
            if (drawingView != null && drawingView.getGraphics() != null) {
                Graphics g = drawingView.getGraphics();
                currentShape.getRendererService().render(g, currentShape, true); // XOR to erase
            }

            appService.scale(currentShape, end);

            if (drawingView != null && drawingView.getGraphics() != null) {
                Graphics g = drawingView.getGraphics();
                currentShape.getRendererService().render(g, currentShape, true); // XOR to draw
            }
        }
    }

    private Rectangle calculateNewBounds(Rectangle orig, SelectionMode mode, Point dragStart, Point dragEnd) {
        int dx = dragEnd.x - dragStart.x;
        int dy = dragEnd.y - dragStart.y;

        int x = orig.x;
        int y = orig.y;
        int w = orig.width;
        int h = orig.height;

        switch (mode) {
            case UpperLeft:
                x += dx; y += dy; w -= dx; h -= dy;
                break;
            case MiddleTop:
                y += dy; h -= dy;
                break;
            case UpperRight:
                y += dy; w += dx; h -= dy;
                break;
            case MiddleLeft:
                x += dx; w -= dx;
                break;
            case MiddleRight:
                w += dx;
                break;
            case LowerLeft:
                x += dx; w -= dx; h += dy;
                break;
            case MiddleBottom:
                h += dy;
                break;
            case LowerRight:
                w += dx; h += dy;
                break;
        }

        return new Rectangle(x, y, w, h);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (drawingStatusPanel != null) drawingStatusPanel.setPoint(e.getPoint());
    }

    @Override public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        // --- NEW ---
        // Don't allow key commands (like delete) while typing
        if (inlineEditor != null) return;

        //delete selected shape
        if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            List<Shape> selectedShapes = appService.getSelectedShapes();
            if (!selectedShapes.isEmpty()) {
                DeleteShapeCommand deleteCommand = new DeleteShapeCommand(appService, selectedShapes);
                CommandService.ExecuteCommand(deleteCommand);
                appService.clearSelections();
                if (drawingView != null) drawingView.repaint();
                if (propertySheet != null) propertySheet.populateTable(appService);
            }
            e.consume();
        }

        //Select All using Ctrl+A
        else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_A) {
            appService.clearSelections();
            for (Shape shape : appService.getDrawing().getShapes()) {
                shape.setSelected(true);
            }
            if (drawingView != null) drawingView.repaint();
            if (propertySheet != null) propertySheet.populateTable(appService);
            e.consume();
        }

        //move using arrow keys
        else if (e.getKeyCode() >= KeyEvent.VK_LEFT && e.getKeyCode() <= KeyEvent.VK_DOWN) {
            List<Shape> selectedShapes = appService.getSelectedShapes();
            if (!selectedShapes.isEmpty()) {
                int dx = 0, dy = 0;
                int nudgeAmount = e.isShiftDown() ? 10 : 1; //with shift 10px, normal 1px

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:  dx = -nudgeAmount; break;
                    case KeyEvent.VK_RIGHT: dx = nudgeAmount; break;
                    case KeyEvent.VK_UP:    dy = -nudgeAmount; break;
                    case KeyEvent.VK_DOWN:  dy = nudgeAmount; break;
                }

                Point start = new Point(0, 0);
                Point end = new Point(dx, dy);
                MoveShapeCommand moveCommand = new MoveShapeCommand(appService, selectedShapes, start, end);
                CommandService.ExecuteCommand(moveCommand);

                if (drawingView != null) drawingView.repaint();
                if (propertySheet != null) propertySheet.populateTable(appService);
                e.consume();
            }
        }
    }

    @Override public void keyReleased(KeyEvent e) { }

    // --- START: NEW METHODS FOR IN-PLACE EDITING ---

    /**
     * Creates and displays a JTextField over the given shape.
     */
    private void startInlineEdit(Shape shape) {
        // If we are already editing, commit the previous edit first
        if (inlineEditor != null) {
            commitInlineEdit();
        }

        // Store the shape we're editing
        this.editingShape = shape;

        // Create the text field
        inlineEditor = new JTextField();

        // Set its text, font, and bounds to match the shape
        inlineEditor.setText(shape.getText());
        inlineEditor.setFont(shape.getFont());

        // --- A small border/padding for the editor ---
        int padding = 2;
        inlineEditor.setBounds(shape.getLocation().x - padding,
                shape.getLocation().y - padding,
                shape.getWidth() + (padding * 2),
                shape.getHeight() + (padding * 2));

        // Center the text in the editor
        inlineEditor.setHorizontalAlignment(JTextField.CENTER);

        // Add a listener for the "Enter" key
        inlineEditor.addActionListener(ae -> commitInlineEdit());

        // Add a listener for "focus lost" (clicking outside the box)
        inlineEditor.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                commitInlineEdit();
            }
        });

        // Add the editor to the DrawingView
        drawingView.add(inlineEditor);

        // Update the view
        drawingView.revalidate();
        drawingView.repaint();

        // Request focus and select all text
        inlineEditor.requestFocusInWindow();
        inlineEditor.selectAll();
    }

    /**
     * Commits the text from the inline editor to the shape and removes the editor.
     */
    private void commitInlineEdit() {
        // Check if we are actually in an editing state
        if (inlineEditor == null) {
            return;
        }

        // Get the new text
        String newText = inlineEditor.getText();

        // --- IMPORTANT: Clean up *before* updating ---
        // Get local references before nulling them
        JTextField editorToRemove = inlineEditor;
        Shape shapeToUpdate = editingShape;

        // Clear the editing state
        this.inlineEditor = null;
        this.editingShape = null;

        // Remove the editor component from the view
        drawingView.remove(editorToRemove);
        drawingView.revalidate();
        drawingView.repaint();

        // --- Now, update the model ---
        // Only update if the text actually changed
        if (shapeToUpdate != null && !newText.equals(shapeToUpdate.getText())) {

            // Use your existing Command framework for undo/redo
            List<Shape> shapes = new ArrayList<>();
            shapes.add(shapeToUpdate);

            SetTextCommand textCommand = new SetTextCommand(appService, shapes, newText);
            CommandService.ExecuteCommand(textCommand);

            // Refresh the property sheet
            if (propertySheet != null) {
                propertySheet.populateTable(appService);
            }
        }
    }

    // --- END: NEW METHODS FOR IN-PLACE EDITING ---


    private static class ShapeTransform {
        int x, y, width, height;

        ShapeTransform(Shape s) {
            this.x = s.getLocation().x;
            this.y = s.getLocation().y;
            this.width = s.getWidth();
            this.height = s.getHeight();
        }
    }

    private static class ShapePosition {
        Shape shape;
        int x, y;

        ShapePosition(Shape s) {
            this.shape = s;
            this.x = s.getLocation().x;
            this.y = s.getLocation().y;
        }
    }

    private void captureOriginalPositionsForMove(List<Shape> shapes) {
        originalPositionsBeforeDrag = new ArrayList<>();
        for (Shape s : shapes) {
            originalPositionsBeforeDrag.add(new ShapePosition(s));
        }
    }

    private void restoreOriginalPositions() {
        if (originalPositionsBeforeDrag == null) return;
        for (ShapePosition pos : originalPositionsBeforeDrag) {
            pos.shape.getLocation().x = pos.x;
            pos.shape.getLocation().y = pos.y;
        }
    }
}