package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;

public class Help implements Command {

    private View view;

    public Help(View view) {

        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {

            view.write("The commands:");
            view.write("\tconnect|databaseName|userName|password");
            view.write("\t\tfor connection to database");

            view.write("\tlist");
            view.write("\t\tfor receiving all tables of connecting database");

            view.write("\tclear|tableName");
            view.write("\t\tfor clearing all table"); // TODO случайный ввод команды - проверка

            view.write("\tcreate|tableName|column1|value1|column2|value2|...|columnN|valueN");
            view.write("\t\tfor creating a row in a table");

            view.write("\tfind|TableName");
            view.write("\t\tfor receiving content of a pointed table 'tableName'");

            view.write("\thelp");
            view.write("\t\tto show this list on a screen");

            view.write("\texit");
            view.write("\t\tto exit from this program");

    }
}
