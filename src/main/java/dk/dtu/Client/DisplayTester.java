package dk.dtu.Client;
import dk.dtu.Common.Card;
import dk.dtu.Server.ShuffledDeck;
import java.util.ArrayList;

public class DisplayTester {

    public static void main(String[] args) throws Exception {
        ShuffledDeck d = new ShuffledDeck();

        ArrayList<PlayerInfo> players = new ArrayList<>();
        PlayerInfo p1 = new PlayerInfo(1, "Søren", 100000, stackToInfo(d.deal()), 0, "Active");
        PlayerInfo p2 = new PlayerInfo(2, "Thomas", 100000, stackToInfo(d.deal()), 10, "Active");
        players.add(p1);
        players.add(p2);
        Card[] flop = {d.drawCard(), d.drawCard(), d.drawCard(), null, null};
        PokerInfo pi = new PokerInfo(players, players, 0, stackToInfo(flop), 10);
        Display display = new Display();
        display.display(pi);

        display.displayPlayerInfo();
        display.displayCards();
        display.displayPot();
        display.displayPlayerCard();
    }


    public static CardInfo cardToInfo(Card c) {
        if (c != null) {
            return new CardInfo(c.getValue(), c.getSuite());
        }
        else return null;
    }

    public static CardInfo[] stackToInfo(Card[] cards) {
        CardInfo[] cardInfos = new CardInfo[cards.length];
        for (int i = 0; i < cards.length; i++) {
            cardInfos[i] = cardToInfo(cards[i]);
        }
        return cardInfos;
    }
}
