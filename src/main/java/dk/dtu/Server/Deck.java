package dk.dtu.Server;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    private ArrayList<Card> cards = new ArrayList<>();

    public Deck() {
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
        return top;
    }

    public int getSize() {
        return cards.size();
    }
}
