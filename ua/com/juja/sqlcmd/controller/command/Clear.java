package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Clear implements Command {
    private DatabaseManager manager;
    private View view;

    public Clear(DatabaseManager manager, View view) {

        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("clear|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
       if (data.length != 2) {
           throw new IllegalArgumentException("The command format 'clear|tableName', but received:" + command);
       }
        manager.clear(data[1]);
       view.write(String.format("The table %s was cleared successfully", data[1]));

    }
}
