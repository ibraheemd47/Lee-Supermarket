package DataAccessLayer.Supplier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.nio.file.Paths;
import java.util.Properties;

public class ConnectionManager {

    public static Connection getConnection() {
        String path = Paths.get("").toAbsolutePath().toString();
        String dbUrl = "jdbc:sqlite:" + path + "/supDB.db";
        try {
            Class.forName("org.sqlite.JDBC");
            Properties props = new Properties();
           // props.setProperty("busy_timeout", "30000");
            Connection conn = DriverManager.getConnection(dbUrl);//, props);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
            return conn;
        } catch (Exception e) {
            System.err.println("Failed to connect to DB: " + e.getMessage());
            return null;
        }
    }
}
