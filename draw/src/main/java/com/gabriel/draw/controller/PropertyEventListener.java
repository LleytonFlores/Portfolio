package com.gabriel.draw.controller;

import com.gabriel.draw.command.*;
import com.gabriel.drawfx.command.CommandService;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.property.event.PropertyEventAdapter;
import com.gabriel.property.property.Property;
import com.gabriel.drawfx.service.AppService;

import java.awt.*;
import java.util.List;

public class PropertyEventListener extends PropertyEventAdapter {
    private AppService appService;

    public PropertyEventListener(AppService appService) {
        this.appService = appService;
    }

    @Override
    public void onPropertyUpdated(Property property) {
        List<Shape> selectedShapes = appService.getSelectedShapes();
        String propertyName = property.getName();

        //fill color
        if ("Fill color".equals(propertyName)) {
            Color newFill = (Color) property.getValue();
            if (!selectedShapes.isEmpty()) {
                SetFillCommand fillCommand = new SetFillCommand(appService, selectedShapes, newFill);
                CommandService.ExecuteCommand(fillCommand);
            } else {
                appService.setFill(newFill);
            }
        }

        //outline color
        else if ("Fore color".equals(propertyName)) {
            Color newColor = (Color) property.getValue();
            if (!selectedShapes.isEmpty()) {
                SetColorCommand colorCommand = new SetColorCommand(appService, selectedShapes, newColor);
                CommandService.ExecuteCommand(colorCommand);
            } else {
                appService.setColor(newColor);
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

                //apply change
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

                //apply change
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

                // Apply the change
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

                //apply change
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

        //text
        else if ("Text".equals(propertyName)) {
            String newText = (String) property.getValue();
            if (!selectedShapes.isEmpty()) {
                SetTextCommand textCommand = new SetTextCommand(appService, selectedShapes, newText);
                CommandService.ExecuteCommand(textCommand);
            } else {
                appService.setText(newText);
            }
        }

        //startColor (gradient)
        else if ("Start color".equals(propertyName)) {
            Color startColor = (Color) property.getValue();
            if (!selectedShapes.isEmpty()) {
                SetGradientPropertiesCommand gradientCommand = new SetGradientPropertiesCommand(
                        appService, selectedShapes, startColor, null, null);
                CommandService.ExecuteCommand(gradientCommand);
            } else {
                appService.setStartColor(startColor);
            }
        }

        //endColor (gradient)
        else if ("End color".equals(propertyName)) {
            Color endColor = (Color) property.getValue();
            if (!selectedShapes.isEmpty()) {
                SetGradientPropertiesCommand gradientCommand = new SetGradientPropertiesCommand(
                        appService, selectedShapes, null, endColor, null);
                CommandService.ExecuteCommand(gradientCommand);
            } else {
                appService.setEndColor(endColor);
            }
        }

        //isGradient
        else if ("IsGradient".equals(propertyName)) {
            boolean isGradient = (Boolean) property.getValue();
            if (!selectedShapes.isEmpty()) {
                SetGradientPropertiesCommand gradientCommand = new SetGradientPropertiesCommand(
                        appService, selectedShapes, null, null, isGradient);
                CommandService.ExecuteCommand(gradientCommand);
            } else {
                appService.setIsGradient(isGradient);
            }
        }

        //isVisible
        else if ("IsVisible".equals(propertyName)) {
            boolean isVisible = (Boolean) property.getValue();
            if (!selectedShapes.isEmpty()) {
                ToggleVisibilityCommand visibilityCommand = new ToggleVisibilityCommand(
                        appService, selectedShapes, isVisible);
                CommandService.ExecuteCommand(visibilityCommand);
            } else {
                appService.setIsVisible(isVisible);
            }
        }

        //font family
        else if ("Font family".equals(propertyName)) {
            if (!selectedShapes.isEmpty()) {
                Font currentFont = selectedShapes.get(0).getFont();
                if (currentFont == null) currentFont = appService.getFont();
                Font newFont = new Font((String) property.getValue(), currentFont.getStyle(), currentFont.getSize());
                SetFontCommand fontCommand = new SetFontCommand(appService, selectedShapes, newFont);
                CommandService.ExecuteCommand(fontCommand);
            } else {
                Font font = appService.getFont();
                Font newFont = new Font((String) property.getValue(), font.getStyle(), font.getSize());
                appService.setFont(newFont);
            }
        }

        //font style
        else if ("Font style".equals(propertyName)) {
            if (!selectedShapes.isEmpty()) {
                Font currentFont = selectedShapes.get(0).getFont();
                if (currentFont == null) currentFont = appService.getFont();
                Font newFont = new Font(currentFont.getFamily(), (int) property.getValue(), currentFont.getSize());
                SetFontCommand fontCommand = new SetFontCommand(appService, selectedShapes, newFont);
                CommandService.ExecuteCommand(fontCommand);
            } else {
                Font font = appService.getFont();
                Font newFont = new Font(font.getFamily(), (int) property.getValue(), font.getSize());
                appService.setFont(newFont);
            }
        }

        //font size
        else if ("Font size".equals(propertyName)) {
            if (!selectedShapes.isEmpty()) {
                Font currentFont = selectedShapes.get(0).getFont();
                if (currentFont == null) currentFont = appService.getFont();
                Font newFont = new Font(currentFont.getFamily(), currentFont.getStyle(), (int) property.getValue());
                SetFontCommand fontCommand = new SetFontCommand(appService, selectedShapes, newFont);
                CommandService.ExecuteCommand(fontCommand);
            } else {
                Font font = appService.getFont();
                Font newFont = new Font(font.getFamily(), font.getStyle(), (int) property.getValue());
                appService.setFont(newFont);
            }
        }

        else if ("Font size".equals(propertyName)) {
            int newSize = (int) property.getValue();

            if (!selectedShapes.isEmpty()) {
                Font currentFont = selectedShapes.get(0).getFont();
                if (currentFont == null) currentFont = appService.getFont();

                Font newFont = new Font(currentFont.getFamily(), currentFont.getStyle(), newSize);
                SetFontCommand fontCommand = new SetFontCommand(appService, selectedShapes, newFont);
                CommandService.ExecuteCommand(fontCommand);
            } else {
                Font font = appService.getFont();
                Font newFont = new Font(font.getFamily(), font.getStyle(), newSize);
                appService.setFont(newFont);
            }
        }

        //gradient start to end
        else if ("Start x".equals(propertyName)) {
            appService.setStartX((int) property.getValue());
        }
        else if ("Start y".equals(propertyName)) {
            appService.setStarty((int) property.getValue());
        }
        else if ("End x".equals(propertyName)) {
            appService.setEndx((int) property.getValue());
        }
        else if ("End y".equals(propertyName)) {
            appService.setEndy((int) property.getValue());
        }
    }
}