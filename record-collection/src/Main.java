import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<Record> myCollection = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        myCollection.add(new Record("For You", "Tatsuro Yamashita"));
        myCollection.add(new Record("Midnight Cruisin'", "Kingo Hamada"));
        myCollection.add(new Record("After 5 Clash", "Toshiki Kadomatsu"));

        while (running) {
            System.out.println("Enter an Album name: (or type exit to quit)");
            String albumInput = scanner.nextLine();
            if (albumInput.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the program, goodbye!");
                System.out.println();
                running = false;
            }
            else {
                System.out.println("Enter the Artist name:");
                String artistInput = scanner.nextLine();
                myCollection.add(new Record(albumInput, artistInput));
            }
        }
        System.out.println("Your record collection:");
        for (Record record : myCollection) {
            System.out.println(record.title + " by " + record.artist);
        }
    }
}
