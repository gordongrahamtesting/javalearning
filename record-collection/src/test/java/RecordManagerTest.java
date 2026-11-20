import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecordManagerTest {

    private static String sqliteUrl(Path tempDir) {
        return "jdbc:sqlite:" + tempDir.resolve("test-collection.db").toAbsolutePath();
    }

    @Test
    void addRecordAddsNewRecordToCollection() {
        RecordManager manager = new RecordManager();
        Record record = new Record("Timely", "Anri");
        int initialSize = manager.getCollection().size();

        boolean result = manager.addRecord(record);

        assertTrue(result);
        assertEquals(initialSize + 1, manager.getCollection().size());
    }

    @Test
    void addRecordRejectsExactDuplicate() {
        RecordManager manager = new RecordManager();
        Record record = new Record("Title", "Artist");
        manager.addRecord(record);
        int initialSize = manager.getCollection().size();

        boolean result = manager.addRecord(record);

        assertFalse(result);
        assertEquals(initialSize, manager.getCollection().size());
    }

    @Test
    void addRecordRejectsDuplicateWithDifferentCasing() {
        RecordManager manager = new RecordManager();
        manager.addRecord(new Record("Title", "Artist"));
        int initialSize = manager.getCollection().size();

        boolean result = manager.addRecord(new Record("title", "artist"));

        assertFalse(result);
        assertEquals(initialSize, manager.getCollection().size());
    }

    @Test
    void saveAndLoadDatabaseRoundTrip(@TempDir Path tempDir) throws SQLException {
        String url = sqliteUrl(tempDir);

        RecordManager writer = new RecordManager(url);
        writer.addRecord(new Record("Lightn Up", "Minako Yoshida"));
        writer.addRecord(new Record("Color the small one", "Sia"));
        writer.saveToDatabase();

        RecordManager reader = new RecordManager(url);
        reader.loadFromDatabase();

        assertEquals(2, reader.getCollection().size());
        assertEquals("Lightn Up", reader.getCollection().get(0).title);
        assertEquals("Minako Yoshida", reader.getCollection().get(0).artist);
        assertEquals("Color the small one", reader.getCollection().get(1).title);
        assertEquals("Sia", reader.getCollection().get(1).artist);
    }

    @Test
    void saveEmptyCollectionClearsExistingDatabaseRows(@TempDir Path tempDir) throws SQLException {
        String url = sqliteUrl(tempDir);

        RecordManager writer = new RecordManager(url);
        writer.addRecord(new Record("Lightn Up", "Minako Yoshida"));
        writer.saveToDatabase();

        RecordManager clearer = new RecordManager(url);
        clearer.saveToDatabase();

        RecordManager reader = new RecordManager(url);
        reader.loadFromDatabase();

        assertTrue(reader.getCollection().isEmpty());
    }

    @Test
    void loadFromDatabaseReplacesUnsavedInMemoryRecords(@TempDir Path tempDir) throws SQLException {
        String url = sqliteUrl(tempDir);

        RecordManager seed = new RecordManager(url);
        seed.addRecord(new Record("Sunshower", "Taeko Onuki"));
        seed.saveToDatabase();

        RecordManager manager = new RecordManager(url);
        manager.addRecord(new Record("Unsaved Album", "Unsaved Artist"));
        manager.loadFromDatabase();

        assertEquals(1, manager.getCollection().size());
        assertEquals("Sunshower", manager.getCollection().get(0).title);
        assertEquals("Taeko Onuki", manager.getCollection().get(0).artist);
    }

    @Test
    void saveAndLoadDatabasePreservesSpecialCharactersAndApostrophes(@TempDir Path tempDir) throws SQLException {
        String url = sqliteUrl(tempDir);

        RecordManager writer = new RecordManager(url);
        writer.addRecord(new Record("Light'n Up", "Minako Yoshida"));
        writer.addRecord(new Record("Yanqui UXO", "Godspeed You! Black Emperor"));
        writer.addRecord(new Record("1bgt!-,.'", "]`)ab34"));
        writer.saveToDatabase();

        RecordManager reader = new RecordManager(url);
        reader.loadFromDatabase();

        assertEquals(3, reader.getCollection().size());
        assertEquals("Light'n Up", reader.getCollection().get(0).title);
        assertEquals("Minako Yoshida", reader.getCollection().get(0).artist);
        assertEquals("Yanqui UXO", reader.getCollection().get(1).title);
        assertEquals("Godspeed You! Black Emperor", reader.getCollection().get(1).artist);
        assertEquals("1bgt!-,.'", reader.getCollection().get(2).title);
        assertEquals("]`)ab34", reader.getCollection().get(2).artist);
    }

    @Test
    void loadFromNewDatabaseReturnsEmptyCollection(@TempDir Path tempDir) throws SQLException {
        String url = sqliteUrl(tempDir);

        RecordManager reader = new RecordManager(url);
        reader.loadFromDatabase();

        assertTrue(reader.getCollection().isEmpty());
    }
}