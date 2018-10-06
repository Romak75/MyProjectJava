package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class MainController {

    private final Command[] commands;
    private View view;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.commands = new Command[] {new Connect(manager, view),
                new Help(view),
                new Exit(view),
                new IsConnected(manager, view),
                new List(manager, view),
                new Clear(manager, view),
                new Create(manager, view),
                new Find(manager, view),
                new Unsupported(view)};
    }
public void run() {
    try {
        doWork();
        return;

    } catch (ExitException e) {
        // do nothing
    }
    }

    private void doWork() {
        view.write("Hi user!");
        view.write("Input database name, username, password, please according to format: connect|database|username|password");


        while (true) {

            String input = view.read();

            for (Command command : commands) {
                    try {
                if (command.canProcess(input)) {
                    command.process(input);
                    break;
                }
                } catch (Exception e) {
                        if (e instanceof ExitException) {
                            throw e;
                        }
                    printError(e);
                    break;
                }
            }
            view.write("Input a command or help for helping.");
        }
    }

    private void printError(Exception e) {
        String   message = e.getMessage();
        if (e.getCause() != null) {
            message += " " + e.getCause().getMessage();
        }
        view.write("The connection isn't done. Для кацапiв - невдача причина:" + message);
        view.write("Try once more, please");
    }

}
