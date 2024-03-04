import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PokerHand  implements Comparable<PokerHand> {
    private ArrayList<String> cards; //Cartes apparetenant a la main
    private Integer rang; //chiffre qui sert a comparer les mains selon les combinaisons

    public PokerHand(String cards){
        String[] tempTab = cards.split("\\s");      //separation de la chaine de charactere pour avoir les cartes individuellement

        this.cards = new ArrayList<>();
        this.cards.addAll(Arrays.asList(tempTab));

        this.rang = calculRang();
    }

    private int calculRang(){   //methode de calcul du rang de combinaison pour les comaparaisons futur
        int result = 1;
        boolean couleur = false;

        ArrayList<Integer> tempNumber = new ArrayList<>();
        ArrayList<String> tempColor = new ArrayList<>();

        for (String card : cards) {                                 //separation des chiffres et des couleurs des cartes
            tempNumber.add(cardToNumber(card.substring(0, 1)));     //chiffres
            tempColor.add(card.substring(1));             //couleurs
        }

        Collections.sort(tempNumber);                              //Tri des chiffres dans l'ordre croissant

        if(Collections.frequency(tempColor,tempColor.get(0)) == 5){     //verification d'une combinaison de couleur
            couleur = true;
        }

        if(couleur){
            if(isSequence(tempNumber)){                             //Verification pour Quinte flush
                if(tempNumber.get(tempNumber.size()-1) == 14 && tempNumber.get(tempNumber.size()-2) == 13){      //Verification pour Quinte flush royale (si la derniere est un as et l'avant derniere un roi)
                    return 10;
                }
                return 9;
            }
            return 6;                                               //Resultat uniquement si couleur
        } else if (isSequence(tempNumber)) {                        //Verification pour Quinte
            return 5;
        } else{                                                     //Verification de paire/brelant/carre uniquement si aucune suite/couleur
            return findPairBrel(tempNumber);
        }

    }

    private boolean isSequence(ArrayList<Integer> list){    //methode qui verifie si la liste de carte donné en parametre est une suite de chiffre
        Integer prev = null;
        int seq = 0;
        for(Integer i : list) {
            if(prev != null && (prev+1 == i || prev+9 == i))                 //si le chiffre precedent+1 est similaire au chiffre actuel alors la boucle continue et la sequence de chiffre reste correct ("prev+9 == i" exception pour gerer l'As quand il y a une suite "As,2,3,4,5")
                seq = seq == 0 ? 2 : seq+1;
            prev = i;
        }
        return (seq == list.size());                        // retourne true uniquement si la sequence de chiffre se fait sur toute les cartes de la main
    }

    private int findPairBrel(ArrayList<Integer> list){      //methode qui verifie si la liste de carte donné en parametre contient une/des paires ou brelant
        int result = 1;
        boolean foundPaire = false;                         // conditions utilisé pour les double paire et les full
        boolean foundBrel = false;                          // conditions utilisé pour les full

        for(int i = 0; i<list.size(); i++) {
            if (Collections.frequency(list, list.get(i)) > 1) {         //verifie si le chiffre actuel est present plus qu'une fois dans la main
                switch (Collections.frequency(list, list.get(i))) {     //si cest le cas alors
                    case 2:
                        if (foundPaire) {                               //Double paire si une paire a deja ete trouve
                            result = 3;
                        } else if (foundBrel) {                         //Full si une brelant a deja ete trouve
                            result = 7;
                        } else {                                        //Simple paire
                            foundPaire = true;
                            result = 2;
                        }
                        break;
                    case 3:
                        if (foundPaire) {                               //Full si une paire a deja ete trouve
                            result = 7;
                        } else {                                        //Brelant
                            foundBrel = true;
                            result = 4;
                        }
                        break;
                    case 4:                                             //Carre
                        result = 8;
                        break;
                    default:
                        break;
                }
                list.removeAll(Collections.singleton(list.get(i)));     //des qu'une verification a été faite sur un chiffre, le/les chiffres correspondant sont retiré de la liste pour ne pas bouclé plusieurs fois sur les memes
            }
        }
        return result;
    }

    private int cardToNumber(String card){      //methode qui convertit les noms speciaux de cartes en leurs chiffre respectif
        try{
            return (Integer.parseInt(card));
        }catch (NumberFormatException e){
            switch (card){
                case "T": return 10;
                case "J": return 11;
                case "Q": return 12;
                case "K": return 13;
                case "A": return 14;
            }
        }
        return 0;
    }

    public Result compareWith(PokerHand hand){      //methode qui va comparer le rang des combinaisons des mains séléctioné et determine si elle est gagnante, perdante ou égalité
        if(this.rang > hand.getRang()){
            return Result.WIN;
        } else if (this.rang < hand.getRang()) {
            return Result.LOSS;
        }else{                                      // si les rangs de combinaison sont similaire
            if(this.rang <= 6 && hand.rang <= 6){   // uniquement possible jusqu'au combinaison de couleur sinon au dela egalité obligatoire voir egalité impossible(carre et full)
                for (int i= 0; i < cards.size(); i++) {
                    if(cardToNumber(cards.get(i).substring(0, 1)) > cardToNumber(hand.getCards().get(i).substring(0, 1))){      //comparaison du niveau des cartes entre les deux mains pour trouvé laquelle possede la prochaine carte la plus forte
                        return Result.WIN;
                    }else if(cardToNumber(cards.get(i).substring(0, 1)) < cardToNumber(hand.getCards().get(i).substring(0, 1))){
                        return Result.LOSS;
                    }
                }

            }
            return Result.TIE;
        }

    }

    public int getRang() {
        return rang;
    }

    public ArrayList<String> getCards() {
        return cards;
    }

    @Override
    public int compareTo(PokerHand hand) {
        return hand.rang.compareTo(this.rang);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        ArrayList<String> tempNumber = new ArrayList<>();
        ArrayList<String> tempColor = new ArrayList<>();

        for (String card : cards) {
            tempNumber.add(card.substring(0, 1));
            tempColor.add(card.substring(1));
        }

        for(String i: tempNumber) result.append("┌─────┐");
        result.append("\n");

        for(String i: tempNumber) result.append("│"+i+"    │");
        result.append("\n");

        for(int i = 0; i<tempColor.size(); i++) {
            String symbole = "";
            switch (tempColor.get(i)){
                case "S":
                    symbole="^";
                    break;
                case "H":
                    symbole="o";
                    break;
                case "D":
                    symbole="v";
                    break;
                case "C":
                    symbole="&";
                    break;
            }
            result.append("│  "+symbole+"  │");
        }
        result.append("\n");

        for(String i: tempNumber) result.append("│    "+i+"│");
        result.append("\n");


        for(String i: tempNumber) result.append("└─────┘");

        return result.toString();
    }

}
