import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    @Test
    void savesRecordWhenUserExits(@TempDir Path tempDir) throws IOException {
        // Arrange
        Path collectionFile = tempDir.resolve("collection.txt");
        RecordManager manager = new RecordManager(collectionFile);
        Scanner input = new Scanner("Album\nArtist\nexit\n");
        PrintStream output = new PrintStream(new ByteArrayOutputStream());

        // Act
        Main.runSession(manager, input, output);

        // Assert
        assertEquals("Album|Artist", Files.readString(collectionFile).trim());
    }
}
