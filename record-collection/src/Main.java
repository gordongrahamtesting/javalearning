import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        RecordManager manager = new RecordManager();

        // file load on start up
        manager.loadFromFile();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("Enter an Album name: (or type exit to quit)");
            String albumInput = scanner.nextLine().trim();
            if (albumInput.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the program, goodbye!");
                System.out.println();
                running = false;
            }
            else {
                System.out.println("Enter the Artist name:");
                String artistInput = scanner.nextLine().trim();

                Record newRecord = new Record(albumInput, artistInput);

                // Give the record to the manager and let it tell us the result
                if (manager.addRecord(newRecord)) {
                    manager.saveToFile();
                    System.out.println("Record added successfully!");
                } else {
                    System.out.println("Duplicate record detected, this will not be added.");
                }
            }
        }

        System.out.println("Your record collection:");
        // Target the manager's list getter method to view the collection
        for (Record record : manager.getCollection()) {
            System.out.println(record.title + " by " + record.artist);
        }
    }
}