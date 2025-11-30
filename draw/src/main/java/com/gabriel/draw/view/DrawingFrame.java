package com.gabriel.draw.view;

import com.gabriel.draw.command.*;
import com.gabriel.draw.component.PropertySheet;
import com.gabriel.draw.controller.ActionController;
import com.gabriel.draw.controller.DrawingController;
import com.gabriel.draw.controller.DrawingWindowController;
import com.gabriel.draw.service.DrawingAppService;
import com.gabriel.draw.service.DrawingCommandAppService;
import com.gabriel.drawfx.ShapeMode;
import com.gabriel.drawfx.command.CommandService;
import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;
import com.gabriel.property.PropertyOptions;
import com.gabriel.property.event.PropertyEventAdapter;
import com.gabriel.property.property.Property;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DrawingFrame extends JFrame {

    Drawing drawing;
    DrawingAppService drawingAppService;
    AppService appService;
    DrawingFrame drawingFrame;
    Container pane;
    private PropertySheet propertySheet;
    ActionController actionListener;
    DrawingMenuBar drawingMenuBar;
    DrawingToolBar drawingToolBar;
    DrawingView drawingView;
    DrawingController drawingController;
    JScrollPane jScrollPane;
    DrawingStatusPanel drawingStatusPanel;
    DrawingWindowController drawingWindowController;

    private boolean inPropertyRefresh = false;

    public DrawingFrame() {

        drawing = new Drawing();
        drawingAppService = new DrawingAppService();
        appService = DrawingCommandAppService.getInstance(drawingAppService);

        pane = getContentPane();
        setLayout(new BorderLayout());

        actionListener = new ActionController(appService);
        actionListener.setFrame(this);
        drawingMenuBar = new DrawingMenuBar(actionListener);

        setJMenuBar(drawingMenuBar);
        drawingMenuBar.setVisible(true);

        drawingToolBar = new DrawingToolBar(actionListener);
        drawingToolBar.setVisible(true);

        drawingView = new DrawingView(appService);
        actionListener.setComponent(drawingView);

        drawingController = new DrawingController(appService, drawingView);
        drawingController.setDrawingView(drawingView);

        drawingView.addMouseMotionListener(drawingController);
        drawingView.addMouseListener(drawingController);
        drawingView.setPreferredSize(new Dimension(4095, 8192));

        jScrollPane = new JScrollPane(drawingView);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        drawingStatusPanel = new DrawingStatusPanel();
        drawingController.setDrawingStatusPanel(drawingStatusPanel);

        pane.add(drawingToolBar, BorderLayout.PAGE_START);
        pane.add(jScrollPane, BorderLayout.CENTER);
        pane.add(drawingStatusPanel, BorderLayout.PAGE_END);

        drawingAppService.setDrawingView(drawingView);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);

        drawingWindowController = new DrawingWindowController(appService);
        this.addWindowListener(drawingWindowController);
        this.addWindowFocusListener(drawingWindowController);
        this.addWindowStateListener(drawingWindowController);
        buildGUI(pane);
        drawingController.setPropertySheet(propertySheet);
    }

    public void buildGUI(Container pane) {
        buildPropertyTable(pane);
        JScrollPane scrollPane = new JScrollPane(propertySheet);
        pane.add(scrollPane, BorderLayout.LINE_END);
        pack();
    }

    void buildPropertyTable(Container pane) {
        String[] headers = new String[]{"Property", "value"};
        Color backgroundColor = new Color(255, 255, 255);
        Color invalidColor = new Color(255, 179, 176);
        int rowHeight = 30;
        PropertyOptions options = new PropertyOptions(headers, backgroundColor, invalidColor, rowHeight);

        propertySheet = new PropertySheet(new PropertyOptions.Builder().build());
        propertySheet.addEventListener(new EventListener());

        inPropertyRefresh = true;
        try {
            propertySheet.populateTable(appService);
        } finally {
            inPropertyRefresh = false;
        }

        repaint();
    }

    class EventListener extends PropertyEventAdapter {
        @Override
        public void onPropertyUpdated(Property property) {
            if (inPropertyRefresh) return;

            List<Shape> selectedShapes = appService.getSelectedShapes();
            String propertyName = property.getName();

            //outline color
            if ("Fore color".equals(propertyName)) {
                Color newColor = (Color) property.getValue();
                if (!selectedShapes.isEmpty()) {
                    SetColorCommand colorCommand = new SetColorCommand(appService, selectedShapes, newColor);
                    CommandService.ExecuteCommand(colorCommand);
                } else {
                    appService.setColor(newColor);
                }
            }

            //fill color
            else if ("Fill color".equals(propertyName)) {
                Color newFill = (Color) property.getValue();
                if (!selectedShapes.isEmpty()) {
                    SetFillCommand fillCommand = new SetFillCommand(appService, selectedShapes, newFill);
                    CommandService.ExecuteCommand(fillCommand);
                } else {
                    appService.setFill(newFill);
                }
            }

            //line thickness
            else if ("Line Thickness".equals(propertyName)) {
                int thickness = (int) property.getValue();
                if (!selectedShapes.isEmpty()) {
                    SetThicknessCommand thicknessCommand = new SetThicknessCommand(appService, selectedShapes, thickness);
                    CommandService.ExecuteCommand(thicknessCommand);
                } else {
                    appService.setThickness(thickness);
                }
            }

            //x location
            else if ("X Location".equals(propertyName)) {
                if (!selectedShapes.isEmpty()) {
                    SetLocationCommand locationCommand = new SetLocationCommand(appService, selectedShapes);
                    locationCommand.captureOriginal();

                    int newX = (int) property.getValue();
                    for (Shape shape : selectedShapes) {
                        shape.getLocation().x = newX;
                    }

                    locationCommand.captureNew();
                    CommandService.ExecuteCommand(locationCommand);
                } else {
                    appService.setXLocation((int) property.getValue());
                }
            }

            //y location
            else if ("Y Location".equals(propertyName)) {
                if (!selectedShapes.isEmpty()) {
                    SetLocationCommand locationCommand = new SetLocationCommand(appService, selectedShapes);
                    locationCommand.captureOriginal();

                    int newY = (int) property.getValue();
                    for (Shape shape : selectedShapes) {
                        shape.getLocation().y = newY;
                    }

                    locationCommand.captureNew();
                    CommandService.ExecuteCommand(locationCommand);
                } else {
                    appService.setYLocation((int) property.getValue());
                }
            }

            //width
            else if ("Width".equals(propertyName)) {
                if (!selectedShapes.isEmpty()) {
                    SetSizeCommand sizeCommand = new SetSizeCommand(appService, selectedShapes);
                    sizeCommand.captureOriginal();

                    int newWidth = (int) property.getValue();
                    for (Shape shape : selectedShapes) {
                        shape.setWidth(newWidth);
                    }

                    sizeCommand.captureNew();
                    CommandService.ExecuteCommand(sizeCommand);
                } else {
                    appService.setWidth((int) property.getValue());
                }
            }

            //height
            else if ("Height".equals(propertyName)) {
                if (!selectedShapes.isEmpty()) {
                    SetSizeCommand sizeCommand = new SetSizeCommand(appService, selectedShapes);
                    sizeCommand.captureOriginal();

                    int newHeight = (int) property.getValue();
                    for (Shape shape : selectedShapes) {
                        shape.setHeight(newHeight);
                    }

                    sizeCommand.captureNew();
                    CommandService.ExecuteCommand(sizeCommand);
                } else {
                    appService.setHeight((int) property.getValue());
                }
            }

            drawingView.repaint();

            inPropertyRefresh = true;
            try {
                propertySheet.populateTable(appService);
            } finally {
                inPropertyRefresh = false;
            }
        }
    }
}
