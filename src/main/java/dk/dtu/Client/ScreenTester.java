package dk.dtu.Client;

import dk.dtu.Common.Suite;
import dk.dtu.Common.Card;

public class ScreenTester {
    public static void main(String[] args) {
        Card card1 = new Card(10, Suite.HEARTS);
        Card card2 = new Card(11, Suite.HEARTS);
        Card card3 = new Card(12, Suite.HEARTS);
        Card card4 = new Card(13, Suite.HEARTS);
        Card card5 = new Card(14, Suite.HEARTS);
        Card card6 = new Card(2, Suite.HEARTS);
        Card card7 = new Card(3, Suite.SPADES);


        Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

        Screen s = new Screen();

        s.welcomeScreen();
        s.lobbyScreen();



    }
}
