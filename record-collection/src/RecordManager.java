import java.util.ArrayList;

public class RecordManager {
    private ArrayList<Record> myCollection = new ArrayList<>();

    public RecordManager() {
        // Default albums for testing
        myCollection.add(new Record("For You", "Tatsuro Yamashita"));
        myCollection.add(new Record("Midnight Cruisin'", "Kingo Hamada"));
        myCollection.add(new Record("After 5 Clash", "Toshiki Kadomatsu"));
    }

    public ArrayList<Record> getCollection() {
        return myCollection;
    }

    public boolean addRecord(Record newRecord) {
        // Check for duplicate entries before adding
        for (Record i : myCollection) {
            if (newRecord.title.equalsIgnoreCase(i.title) && newRecord.artist.equalsIgnoreCase(i.artist)){
                return false;
            }
        }
        myCollection.add(newRecord);
        return true;
    }
}
