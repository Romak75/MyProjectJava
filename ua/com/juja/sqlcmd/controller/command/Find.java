package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.Command;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Find implements Command {
    private DatabaseManager manager;
    private View view;

    public Find(DatabaseManager manager, View view) {

        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("find|");
    }

    @Override
    public void process(String command) {

        String[] data = command.split("\\|");
        String tableName = data[1];//TODO to add validation
        DataSet[] tableData = manager.getTableData(tableName);
        String[] tableColoumns = manager.getTableColoumns(tableName);
        printHeader(tableColoumns);
        printTable(tableData);
    }

        private void printTable (DataSet[]tableData){

            for (DataSet row : tableData) {
                printRow(row);
                view.write("--------------------");
            }
        }

        private void printRow(DataSet row) {
            Object[] values = row.getValues();
            String result = "|";
            for (Object value : values) {
                result += value + "|";
            }
            view.write(result);
        }

        private void printHeader(String[] tableColoumns) {

            String result = "|";
            for (String name : tableColoumns) {
                result += name + "|";
            }
            view.write("--------------------");
            view.write(result);
            view.write("--------------------");
        }


    }

