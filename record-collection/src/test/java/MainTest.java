import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    @Test
    void savesRecordToDatabaseWhenUserExits(@TempDir Path tempDir) throws SQLException {
        String url = "jdbc:sqlite:" + tempDir.resolve("test-collection.db").toAbsolutePath();
        RecordManager writer = new RecordManager(url);
        Scanner input = new Scanner("Album\nArtist\nexit\n");
        PrintStream output = new PrintStream(new ByteArrayOutputStream());

        Main.runSession(writer, input, output);

        RecordManager reader = new RecordManager(url);
        reader.loadFromDatabase();
        assertEquals(1, reader.getCollection().size());
        assertEquals("Album", reader.getCollection().get(0).title);
        assertEquals("Artist", reader.getCollection().get(0).artist);
    }
}