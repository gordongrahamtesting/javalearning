import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        RecordManager manager = new RecordManager();

        // file load on start up
        try {
            manager.loadFromFile();
        }
        catch (IOException exception) {
            System.out.println("File could not be read, quitting app");
            System.out.println(exception.getMessage());
            return;
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
                    manager.saveToFile();
                    output.println("Saving to file and exiting the program, goodbye!");
                    running = false;
                } catch (IOException e) {
                    output.println("Saving to file failed, please retry or type exit again at the next prompt to save + quit");
                }
            }
            else {
                output.println("Enter the Artist name:");
                String artistInput = scanner.nextLine().trim();

                Record newRecord = new Record(albumInput, artistInput);

                // Give the record to the manager and let it tell us the result
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