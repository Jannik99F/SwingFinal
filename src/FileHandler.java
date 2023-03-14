import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class FileHandler {

    //readFile liest die scores.txt ein und gibt ein Array mit den Spielern zurück
    public static ArrayList<Player> readFile(String filePath) {
        ArrayList<Player> players = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\) ");
                int score = Integer.parseInt(parts[0].substring(1));
                String name = parts[1];
                players.add(new Player(score, name));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not find the file '" + filePath + "'.");
        }
        Collections.sort(players);
        return players;
    }

     //writeFile erhält über readFile ein Array mit den Spielern, fügt den neuen Spieler hinzu, sortiert das array und speichert es in die scores.txt
    public static void write(ArrayList<Player> players, Player newPlayer, String filePath) throws IOException {
        players.add(newPlayer);
        Collections.sort(players);

        try (FileWriter writer = new FileWriter(filePath)) {
            for (Player player : players) {
                writer.write("(" + player.getScore() + ")" + " " + player.getName());
                writer.write(System.lineSeparator());
            }
        }
    }

}




