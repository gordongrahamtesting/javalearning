import java.util.ArrayList;

public class RecordManager {
    private ArrayList<Record> myCollection = new ArrayList<>();

    public ArrayList<Record> getCollection() {
        return myCollection;
    }

    public boolean addRecord(Record newRecord) {
        // Check for duplicate entries before adding
        boolean isDuplicate = false;
        for (Record i : myCollection) {
            if (newRecord.title.equalsIgnoreCase(i.title) && newRecord.artist.equalsIgnoreCase(i.artist)){
                isDuplicate = true;
                System.out.println("Duplicate record detected, this will not be added");
                return false;
            }
        }
        myCollection.add(newRecord);
        return true;
    }
}
