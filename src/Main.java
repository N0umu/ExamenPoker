
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {


        ArrayList<PokerHand> hands = Utils.input("src/txt/Hand.txt");
        Utils.output("src/txt/Hand2.txt", hands);

        System.out.print(hands);

//        PokerHand hand = new PokerHand("2C 3C AC 4C 5C");
//        PokerHand hand2 = new PokerHand("KS 2H 5C JD TD");
//
//        System.out.println(hand);
//        System.out.println(hand.getRang());
//
//        System.out.println(hand.compareWith(hand2));
//
//        ArrayList<PokerHand> hands = new ArrayList<PokerHand>();
//        hands.add(new PokerHand("2C 3C AC 4C 5C"));
//        hands.add(new PokerHand("KS 2H 5C JD TD"));
//
//        Collections.sort(hands);
//        System.out.println(hands);

    }
}