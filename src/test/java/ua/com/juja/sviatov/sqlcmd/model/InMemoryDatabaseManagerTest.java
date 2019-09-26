package ua.com.juja.sviatov.sqlcmd.model;

/**
 * Created by anton.sviatov on 23.05.2019.
 */

public class InMemoryDatabaseManagerTest extends DatabaseManagerTest {

    @Override
    public DatabaseManager getDatabaseManager() {
        return new InMemoryDatabaseManager();
    }
}
