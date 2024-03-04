import java.io.*;
import java.util.ArrayList;

public class Utils {
    public static ArrayList<PokerHand> input(String filePath){
        ArrayList<PokerHand> hands = new ArrayList<PokerHand>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                hands.add(new PokerHand(line));
            }
        }

        catch(Exception e) {
            e.getStackTrace();
        }
        return hands;
    }

    public static void output(String filePath, ArrayList<PokerHand> hands){
        try {
            File file = new File(filePath);
            FileWriter fileReader = new FileWriter(file); // A stream that connects to the text file
            BufferedWriter bufferedWriter = new BufferedWriter(fileReader); // Connect the FileWriter to the BufferedWriter

            for (PokerHand hand: hands) {
                bufferedWriter.write(hand + "\n");
            }

            bufferedWriter.close (); // Close the stream
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
