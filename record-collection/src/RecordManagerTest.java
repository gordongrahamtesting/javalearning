import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RecordManagerTest {

    @Test
    void addRecord() {
        // Arrange
        RecordManager manager = new RecordManager();
        Record record = new Record("Timely", "Anri");
        int initialSize = manager.getCollection().size();

        // Act
        boolean result = manager.addRecord(record);

        // Assert
        assertTrue(result);
        assertEquals(initialSize + 1, manager.getCollection().size());
    }

    @Test
    void duplicateReturnsFalse() {
        // Arrange
        RecordManager manager = new RecordManager();
        Record record = new Record("Title", "Artist");
        manager.addRecord(record);
        int initialSize = manager.getCollection().size();

        // Act
        boolean result = manager.addRecord(record);

        // Assert
        assertFalse(result);
        assertEquals(initialSize, manager.getCollection().size());
    }

    @Test
    void duplicateWithDifferentCasingReturnsFalse() {
        // Arrange
        RecordManager manager = new RecordManager();
        Record record1 = new Record("Title", "Artist");
        Record record2 = new Record("title", "artist");
        manager.addRecord(record1);
        int initialSize = manager.getCollection().size();

        // Act
        boolean result = manager.addRecord(record2);

        // Assert
        assertFalse(result);
        assertEquals(initialSize, manager.getCollection().size());
    }

    @Test
    void getCollectionContainsDefaultRecords() {
        // Arrange
        RecordManager manager = new RecordManager();

        // Act
        ArrayList<Record> collection = manager.getCollection();

        // Assert
        assertFalse(collection.isEmpty());
    }
}