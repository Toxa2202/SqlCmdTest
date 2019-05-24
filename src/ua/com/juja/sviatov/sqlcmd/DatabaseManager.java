package ua.com.juja.sviatov.sqlcmd;

import java.sql.*;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by anton.sviatov on 23.05.2019.
 */
public class DatabaseManager {
        // Initialize connection once
    private Connection connection;

    public static void main(String[] args) throws SQLException {
            // Database information
        String database = "sqlcmd";
        String user = "postgres";
        String password = "postgres";

            // Create DatabaseManager object
        DatabaseManager manager = new DatabaseManager();
            // With connect method create connection (with parameters)
        manager.connect(database, user, password);
            // Create connection with getConnection TODO refactor that
        Connection connection = manager.getConnection();

        /** Delete */
        manager.clear("user");

        /** Insert */
        DataSet data = new DataSet();
        data.put("id", 13);
        data.put("name", "Stiven");
        data.put("password", "pass");
        manager.create(data);

        /** Select */
            // First get list of tables through getTableNames method
            // TODO two operations in one box (tables & select)
        String[] tables = manager.getTableNames(); // Array to store list of DB's
        System.out.println(Arrays.toString(tables)); // Show list on the screen

        String tableName = "user";
        DataSet[] result = manager.getTableData(tableName);
        System.out.println(Arrays.toString(result));


        /** Update */
        PreparedStatement ps = connection.prepareStatement(
                "UPDATE public.user SET password = ? WHERE id > 3");
        String pass = "password_" + new Random().nextInt();
        ps.setString(1, pass);
        ps.executeUpdate();
        ps.close();
        connection.close();
    }

    public DataSet[] getTableData(String tableName) {
        try {
            int size = getSize(tableName);

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM public." + tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            // Array to store all saved data from the tables
            DataSet[] result = new DataSet[size];
            int index = 0;

            while (rs.next()) {
                // Save every line in array with index iteration
                DataSet dataSet = new DataSet();
                result[index++] = dataSet;
                // Save info from all not-empty tables with PUT method
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    dataSet.put(rsmd.getColumnName(i), rs.getObject(i));
                }
            }
            rs.close();
            stmt.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new DataSet[0];
        }
    }

    private int getSize(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        // Get number of all written data elements
        ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM public." + tableName);
        rsCount.next();
        // Save than number to integer variable
        int size = rsCount.getInt(1);
        rsCount.close();
        return size;
    }

    // Get List of existing tables
    public String[] getTableNames() {
            // try to catch SQL errors
        try {
            Statement stmt = connection.createStatement();
                // Select all tables
            ResultSet rs = stmt.executeQuery("SELECT table_name FROM " +
                    "information_schema.tables WHERE table_schema = 'public' AND " +
                    "table_type = 'BASE TABLE'");
                // Create empty 100  String array
            String[] tables = new String[100];
            int index = 0;
                // Put info inside the array, call every time ResultSet
            while (rs.next()) {
                tables[index++] = rs.getString("table_name");
            }
                // Save only not empty fields in the array
            tables = Arrays.copyOf(tables, index, String[].class);
            stmt.close();
            rs.close();
                // Return list of tables upstairs
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
                // If Error, return empty array
            return new String[0];
        }
    }

        // Connection to Database
    public void connect (String database, String user, String password) {
            // Try to catch ClassNotFoundException errors
        try {
                // Connect to jdbc driver
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
                // If Error - get message
            System.out.println("Please add jdbc jar to project");
            e.printStackTrace();
        }

            // Try to catch SQLException errors
        try {
                // Make connection to current DB with login / password
            connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5434/"
                                + database, user, password);
        } catch (SQLException e) {
                // If errors - get message
            System.out.println(String.format("Can't get connection for database:%s user:%s", database, user));
            e.printStackTrace();
                // close connection
            connection = null;
        }
    }

        // Getter for connection
    private Connection getConnection() {
        return connection;
    }

    public void clear(String tableName) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM public." + tableName);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void create(DataSet input) {
        try {
            Statement stmt = connection.createStatement();

            String tableNames = "";
            for (String name : input.getNames()) {
                tableNames += name + ",";
            }
                // delete last symbol by substring
            tableNames = tableNames.substring(0, tableNames.length() - 1);

            String values = "";
            for (Object value: input.getValues()) {
                values += "'" + value.toString() + "'" + ",";
            }
            // delete last symbol by substring
            values = values.substring(0, values.length() - 1);

            stmt.executeUpdate("INSERT INTO public.user (" + tableNames + ")" +
                    "VALUES (" + values + ")");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}