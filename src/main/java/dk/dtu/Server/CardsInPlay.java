package dk.dtu.Server;

import dk.dtu.Common.Card;
import dk.dtu.Common.Suite;

public class CardsInPlay {
    private Card[] cards;

    public CardsInPlay() throws Exception {
        this.cards = new Card[5];
    }

    public void flop(ShuffledDeck deck) throws Exception {
       for (int i = 0; i < 3; i++) {
           this.cards[i] = deck.drawCard();
       }
    }

    public void turn(ShuffledDeck deck) throws Exception {
        this.cards[3] = deck.drawCard();
    }

    public void river(ShuffledDeck deck) throws Exception {
        this.cards[4] = deck.drawCard();
    }

    public Card[] getCards() {
        return cards;
    }

    public Card get(int i) {
        if (cards[i] == null) {
            return new Card(0, Suite.HEARTS); //Hearts is an arbitrary value.
        } else {
            return cards[i];
        }
    }
}
