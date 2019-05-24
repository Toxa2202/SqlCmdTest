package ua.com.juja.sviatov.sqlcmd;

import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by anton.sviatov on 23.05.2019.
 */

public class DatabaseManagerTest {
    // Don't need to import DatabaseManager class, coz we call him in test variable
    private DatabaseManager manager;

    // Launch once before all tests (make connection)
    @Before
    public void setup() {
        // create new object of DatabaseManager
        manager = new DatabaseManager();
        // call method 'connect' to launch connection with 'sqlcmd'
        manager.connect("sqlcmd", "postgres", "postgres");
    }

    // First test
    @Test
    public void testGetAllTableNames() {
        // Put in array list of databases
        String[] tableNames = manager.getTableNames();
        // Verify given list with actual information
        assertEquals("[user, test]", Arrays.toString(tableNames));
    }
}
