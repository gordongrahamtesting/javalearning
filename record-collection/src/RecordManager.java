import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.nio.file.Files;
import java.util.List;

public class RecordManager {
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
}
