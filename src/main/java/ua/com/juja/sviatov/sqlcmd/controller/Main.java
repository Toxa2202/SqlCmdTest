package ua.com.juja.sviatov.sqlcmd.controller;

import ua.com.juja.sviatov.sqlcmd.model.DatabaseManager;
import ua.com.juja.sviatov.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.sviatov.sqlcmd.view.Console;
import ua.com.juja.sviatov.sqlcmd.view.View;

/**
 * Created by anton.sviatov on 26.09.2019.
 */
public class Main {

    public static void main(String[] args) {
        View view = new Console();
        DatabaseManager manager = new JDBCDatabaseManager();

        MainController controller = new MainController(view, manager);
        controller.run();
    }
}
