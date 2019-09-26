package ua.com.juja.sviatov.sqlcmd.controller;

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
            } else if (command.equals("help")){
                doHelp();
            } else {
                view.write("Command does not exist: " + command);
            }
        }
    }

    private void doHelp() {
        view.write("Existing commands:");
        view.write("\tlist");
        view.write("\t\tfor all databases list");
        view.write("\thelp");
        view.write("\t\tfor view this list on the screen");

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
