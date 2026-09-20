// DbUtil.java
package xo_game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    // tell Derby to create the DB on first connect
    private static final String URL =
      "jdbc:derby://localhost:1527/XOGameDB;create=true";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Derby client driver missing!", e);
        }
        // this line will now create XOGameDB if it doesn’t exist
        return DriverManager.getConnection(URL);
    }
}

