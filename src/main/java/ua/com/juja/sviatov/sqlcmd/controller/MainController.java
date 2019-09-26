package ua.com.juja.sviatov.sqlcmd.controller;

import ua.com.juja.sviatov.sqlcmd.model.DataSet;
import ua.com.juja.sviatov.sqlcmd.model.DatabaseManager;
import ua.com.juja.sviatov.sqlcmd.view.View;

import java.util.Arrays;

/**
 * Created by anton.sviatov on 26.09.2019.
 */
public class MainController {
    private View view;
    private DatabaseManager manager;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    public void run() {
        connectToDB();

        while (true) {
            view.write("Enter Command (or Help for help):");
            String command = view.read();

            if (command.equals("list")) {
                doList();
            } else if (command.equals("help")) {
                doHelp();
            } else if (command.equals("exit")) {
                view.write("Have a nice day!");
                System.exit(0);
            } else if (command.startsWith("find|")) {
                doFind(command);
            } else {
                view.write("Command does not exist: " + command);
            }
        }
    }

    private void doFind(String command) {
        String[] data = command.split("\\|");
        String tableName = data[1];

        String[] tableColumns = manager.getTableColumns(tableName);
        printHeader(tableColumns);
        DataSet[] tableData = manager.getTableData(tableName);
        printTable(tableData);
    }

    private void printTable(DataSet[] tableData) {
        for (DataSet row : tableData) {
            printRow(row);
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

    private void printHeader(String[] tableColumns) {
        String result = "|";
        for (String name : tableColumns) {
            result += name + "|";
        }
        view.write("----------------------");
        view.write(result);
        view.write("----------------------");
    }

    private void doHelp() {
        view.write("Existing commands:");
        view.write("\tlist");
        view.write("\t\tto see all databases");

        view.write("\tfind|tableName");
        view.write("\t\tto data from the table 'tableName'");

        view.write("\thelp");
        view.write("\t\tto see this list on the screen");

        view.write("\texit");
        view.write("\t\tto Exit");

    }

    private void doList() {
        String[] tableNames = manager.getTableNames();
        String message = Arrays.toString(tableNames);
        view.write(message);
    }

    private void connectToDB() {
        view.write("Hello, user!");
        view.write("Enter database name, user name and password in format:" +
                "database|userName|password:");

        while (true) {
            try {
                String str = view.read();
                String[] data = str.split("\\|");
                if (data.length != 3) {
                    throw new IllegalArgumentException("You must enter 3 parameters, like: db|user|password");
                }
                String databaseName = data[0];
                String userName = data[1];
                String password = data[2];

                manager.connect(databaseName, userName, password);
                break;
            } catch (Exception e) {
                printError(e);
            }
        }

        view.write("Success!");
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        if (e.getCause() != null) {
            message += " " + e.getCause().getMessage();
        }
        view.write("Error, because of: " + message);
        view.write("Try again!");
    }
}
