import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.nio.file.Files;
import java.util.List;

public class RecordManager {
    private String dbUrl = DatabaseManager.defaultUrl();
    private ArrayList<Record> myCollection = new ArrayList<>();
    // Default file path for normal application runtime
    private Path filePath = Path.of("collection.txt");

    public RecordManager() {
        // Keeps default constructor functional for Main.java
    }

    public RecordManager(Path customPath) {
        // Fixed typo: changed minus (-) to equals (=)
        this.filePath = customPath;
    }

    public RecordManager(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public ArrayList<Record> getCollection() {
        return myCollection;
    }

    public boolean addRecord(Record newRecord) {
        for (Record i : myCollection) {
            if (newRecord.title.equalsIgnoreCase(i.title) && newRecord.artist.equalsIgnoreCase(i.artist)){
                return false;
            }
        }
        myCollection.add(newRecord);
        return true;
    }

    public void saveToFile() throws IOException {
        List<String> lines = new ArrayList<>();
        for (Record i : myCollection) {
            String line = i.title + "|" + i.artist;
            lines.add(line);
        }

        // Updated: Using global this.filePath field instead of hardcoded text
        Files.write(this.filePath, lines);
    }

    public void loadFromFile() throws IOException {
        // Removed the hardcoded internal variable re-declaration

        // Updated: Using global this.filePath field instead of hardcoded text
        if (!Files.exists(this.filePath)) {
            return;
        }

        List<String> lines = Files.readAllLines(this.filePath);
        myCollection.clear();

        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length == 2) {
                String title = parts[0];
                String artist = parts[1];
                myCollection.add(new Record(title, artist));
            }
        }
    }

    public void saveToDatabase() throws SQLException {
        DatabaseManager.initialize(this.dbUrl);

        String deleteSql = "DELETE FROM records;";
        String insertSql = """
            INSERT INTO records (title, artist)
            VALUES (?, ?);
            """;

        try (Connection conn = DriverManager.getConnection(this.dbUrl)) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                    deleteStmt.executeUpdate();
                }

                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    for (Record record : myCollection) {
                        insertStmt.setString(1, record.title);
                        insertStmt.setString(2, record.artist);
                        insertStmt.addBatch();
                    }
                    insertStmt.executeBatch();
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void loadFromDatabase() throws SQLException {
        DatabaseManager.initialize(this.dbUrl);

        String selectSql = "SELECT title, artist FROM records ORDER BY id ASC;";
        List<Record> loaded = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(this.dbUrl);
             PreparedStatement stmt = conn.prepareStatement(selectSql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                loaded.add(new Record(title, artist));
            }
        }

        myCollection.clear();
        myCollection.addAll(loaded);
    }
}
