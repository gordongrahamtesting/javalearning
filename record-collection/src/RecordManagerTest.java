import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecordManagerTest {

    @Test
    void addRecord() {
        // Arrange
        RecordManager manager = new RecordManager();
        Record record = new Record("Timely", "Anri");

        // Act
        boolean result = manager.addRecord(record);

        // Assert
        assertTrue(result);
    }

    @Test
    void duplicateReturnsFalse() {
        // Arrange
        RecordManager manager = new RecordManager();
        Record record = new Record("Title", "Artist");
        manager.addRecord(record);

        // Act
        boolean result = manager.addRecord(record);

        // Assert
        assertFalse(result);
    }

    @Test
    void duplicateWithDifferentCasingReturnsFalse() {
        // Arrange
        RecordManager manager = new RecordManager();
        Record record1 = new Record("Title", "Artist");
        Record record2 = new Record("title", "artist");
        manager.addRecord(record1);

        // Act
        boolean result = manager.addRecord(record2);

        // Assert
        assertFalse(result);
    }
}