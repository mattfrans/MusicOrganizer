package controller;

import model.Commands.Command;

import java.util.Stack;

public class CommandController {
    // this class handle commands to undo or redo
    private final Stack<Command> commandStack;  // contains 0-10 items commands
    private final Stack<Command> undoStack;  // whenever a command is undone, it gets added here
    private static CommandController instance;  // this class is a singleton,
    // since multiple commandControllers might mess up the command stack

    private CommandController(){
        // constructor
        this.commandStack = new Stack<>();
        this.undoStack = new Stack<>();
    }

    public static CommandController get(){
        // the singleton get method
        if (instance == null){
            instance = new CommandController();
        }
        return instance;
    }

    public void addNewCommand(Command command){
        // execute command
        assert command != null;
        command.execute();
        // this method is called whenever a new command is done by the user
        // method call generally from the MusicOrganizerController class
        if (this.commandStack.size() < 10){
            // add command to stack
            this.commandStack.add(command);
        } else {
            // add command to stack and remove the first command to keep 10 commands
            this.commandStack.add(command);
            this.commandStack.remove(0);
        }
        this.undoStack.clear();  // clear undo stack since that now is obsolete,
        // we cannot undo a command that is 2 commands "old"
    }

    private void addOldCommand(Command command){
        // this method gets called whenever a "redo" command is executed
        // add command to command stack, DO NOT clear the undo stack
        // method call from within this class, hence private
        this.commandStack.add(command);
    }

    public void undoLastCommand(){
        if (!this.commandStack.isEmpty()) {
            // has commands to undo
            Command c = this.commandStack.pop();  // get last command
            assert c != null; // something is wrong if there was a null command in stack
            c.undo();  // undo the command
            this.undoStack.add(c);  // removes last command from commandStack and adds to undoStack
        }
    }

    public void redoLastUndo(){
        assert this.undoStack.size() != 0;
        Command c = this.undoStack.pop();  // gets the last undo command
        assert c != null;
        c.redo();  // redo the command
        addOldCommand(c);  // add command back to command stack
    }

    public boolean hasCommands(){
        // helper method to check if stack has commands
        boolean hasCommands = !this.commandStack.isEmpty();
        System.out.println("Command controller has commands: " + hasCommands);
        return hasCommands;
    }

    public boolean hasRedoableCommands(){
        // helper stack to check if undo stack has redo-able commands
        boolean hasRedoAbleCommands = !this.undoStack.isEmpty();
        System.out.println("Command controller has redo-able commands: " + hasRedoAbleCommands);
        return hasRedoAbleCommands;
    }
}
