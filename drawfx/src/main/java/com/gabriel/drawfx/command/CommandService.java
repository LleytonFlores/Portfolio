package com.gabriel.drawfx.command;

import java.util.Stack;

public class CommandService {
    static Stack<Command> undoStack = new Stack<Command>();
    static Stack<Command> redoStack = new Stack<Command>();

    public static void ExecuteCommand(Command command) {
        System.out.println("Executing command: " + command.getClass().getSimpleName());
        command.execute();
        undoStack.push(command);
        redoStack.clear();
        System.out.println("Undo stack size: " + undoStack.size());
    }

    public static void undo() {
        System.out.println("Undo called. Stack size: " + undoStack.size());
        if (undoStack.empty()) {
            System.out.println("Undo stack is empty!");
            return;
        }
        Command command = undoStack.pop();
        System.out.println("Undoing: " + command.getClass().getSimpleName());
        command.undo();
        redoStack.push(command);
    }

    public static void redo() {
        System.out.println("Redo called. Stack size: " + redoStack.size());
        if (redoStack.empty()) {
            System.out.println("Redo stack is empty!");
            return;
        }
        Command command = redoStack.pop();
        System.out.println("Redoing: " + command.getClass().getSimpleName());
        command.execute();
        undoStack.push(command);
    }
}