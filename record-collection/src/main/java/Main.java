import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        RecordManager manager = new RecordManager();
        try {
            manager.loadFromDatabase();
        } catch (SQLException e) {
            System.err.println("Failed to load collection from database: " + e.getMessage());
        }
        runSession(manager, new Scanner(System.in), System.out);
    }

    static void runSession(RecordManager manager, Scanner scanner, PrintStream output) {
        boolean running = true;
        while (running) {
            output.println("Enter an Album name: (type exit to save + quit)");
            String albumInput = scanner.nextLine().trim();
            if (albumInput.equalsIgnoreCase("exit")) {
                try {
                    manager.saveToDatabase();
                    output.println("Saving to file and exiting the program, goodbye!");
                    running = false;
                } catch (SQLException e) {
                    System.err.println("Failed to save collection to database: " + e.getMessage());
                }
            }
            else {
                output.println("Enter the Artist name:");
                String artistInput = scanner.nextLine().trim();

                Record newRecord = new Record(albumInput, artistInput);

                if (manager.addRecord(newRecord)) {
                    output.println("New record added to memory");
                } else {
                    output.println("Duplicate record detected, this will not be added.");
                }
            }
        }

        output.println("Your record collection:");
        // Target the manager's list getter method to view the collection
        for (Record record : manager.getCollection()) {
            output.println(record.title + " by " + record.artist);
        }
    }
}