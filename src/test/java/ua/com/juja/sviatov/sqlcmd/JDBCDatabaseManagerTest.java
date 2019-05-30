package ua.com.juja.sviatov.sqlcmd;

import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by anton.sviatov on 23.05.2019.
 */

public class JDBCDatabaseManagerTest {
        // Don't need to import JDBCDatabaseManager class, coz we call him in test variable
    private DatabaseManager manager;

        // Launch once before all tests (make connection)
    @Before
    public void setup() {
            // create new object of JDBCDatabaseManager
        manager = new JDBCDatabaseManager();
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

    @Test
    public void testGetTableData() {
            // given
        manager.clear("user");

            // when
        DataSet input = new DataSet();
        input.put("id", 13);
        input.put("name", "Stiven");
        input.put("password", "pass");
        manager.create(input);

            //then
        DataSet[] users = manager.getTableData("user");
        assertEquals(1, users.length);

        DataSet user = users[0];
        assertEquals("[name, password, id]", Arrays.toString(user.getNames()));
        assertEquals("[Stiven, pass, 13]", Arrays.toString(user.getValues()));
    }

    @Test
    public void testUpdateTableData() {
        // given
        manager.clear("user");

        DataSet input = new DataSet();
        input.put("id", 13);
        input.put("name", "Stiven");
        input.put("password", "pass");
        manager.create(input);

        // when
        DataSet newValue = new DataSet();
        newValue.put("password", "pass2");
        manager.update("user", 13, newValue);

        //then
        DataSet[] users = manager.getTableData("user");
        assertEquals(1, users.length);

        DataSet user = users[0];
        assertEquals("[name, password, id]", Arrays.toString(user.getNames()));
        assertEquals("[Stiven, pass2, 13]", Arrays.toString(user.getValues()));
    }
}
