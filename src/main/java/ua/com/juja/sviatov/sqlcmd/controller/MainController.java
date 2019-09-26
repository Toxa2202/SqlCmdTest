package ua.com.juja.sviatov.sqlcmd.controller;

import ua.com.juja.sviatov.sqlcmd.model.DatabaseManager;
import ua.com.juja.sviatov.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.sviatov.sqlcmd.view.Console;
import ua.com.juja.sviatov.sqlcmd.view.View;

/**
 * Created by anton.sviatov on 26.09.2019.
 */
public class MainController {
    public static void main(String[] args) {
        View view = new Console();
        DatabaseManager manager = new JDBCDatabaseManager();

        view.write("Hello, user!");
        view.write("Enter database name, user name and password in format:" +
                "database|userName|password:");

        while (true) {
            String str = view.read();
            String[] data = str.split("\\|");
            String databaseName = data[0];
            String userName = data[1];
            String password = data[2];

            try {
                manager.connect(databaseName, userName, password);
                break;
            } catch (Exception e) {
                String message = e.getMessage();
                if (e.getCause() != null) {
                    message += " " + e.getCause().getMessage();
                }
                view.write("Error, because of " + message);
                view.write("Try again!");
            }
        }

        view.write("Success!");
    }
}
