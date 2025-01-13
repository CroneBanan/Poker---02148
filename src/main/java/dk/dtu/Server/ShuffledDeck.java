package dk.dtu.Server;

import dk.dtu.Common.Card;
import dk.dtu.Common.Suite;

import java.util.ArrayList;
import java.util.Collections;

public class ShuffledDeck {

    private ArrayList<Card> cards = new ArrayList<>();

    public ShuffledDeck() {
        Suite[] suites = {Suite.HEARTS, Suite.DIAMONDS, Suite.CLUBS, Suite.SPADES};
        for (Suite suite : suites) {
            for (int i = 2; i <= 14; i++) {
                this.cards.add(new Card(i, suite));
            }
        }
        Collections.shuffle(this.cards);
    }

    public Card drawCard() throws Exception {
        if (cards.isEmpty()) {
            throw new Exception("Card cannot be drawn from empty deck");
        }
        Card top = cards.get(0);
        cards.remove(0);
        System.out.println(top);
        return top;
    }

    public Card[] deal() throws Exception {
        return new Card[]{drawCard(), drawCard()};
    }


    public int getSize() {
        return cards.size();
    }
}
