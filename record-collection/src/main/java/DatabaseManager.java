import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DEFAULT_URL = "jdbc:sqlite:collection.db";

    public static String defaultUrl() {
        return DEFAULT_URL;
    }

    public static void initialize(String dbUrl) throws SQLException {
        String createTableSql = """
            CREATE TABLE IF NOT EXISTS records (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                artist TEXT NOT NULL,
                UNIQUE(title, artist)
            );
            """;

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement stmt = conn.prepareStatement(createTableSql)) {
            stmt.execute();
        }
    }
}