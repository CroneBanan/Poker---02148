package dk.dtu.Server;

import org.jspace.RemoteSpace;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;

public class Poker implements Runnable {
    private String uri;
    private RemoteSpace space;
    private RemoteSpace player1;
    private RemoteSpace player2;
    private ArrayList<Card> deck = new ArrayList<>();

    public Poker(String uri) throws IOException {
        this.space = new RemoteSpace(uri + "/gameState?conn");
        this.uri = uri;
    }

    public void initializeDeck() {
        Suite[] suites = {Suite.HEARTS, Suite.DIAMONDS, Suite.CLUBS, Suite.SPADES};
        for (Suite suite : suites) {
            for (int i = 2; i <= 14; i++) {
                deck.add(new Card(i, suite));
            }
        }
        Collections.shuffle(deck);
    }

    public void setBlinds() {

    }

    public void startGame() throws Exception {
        Deck deck = new Deck();
        this.player1 = new RemoteSpace(uri + "/player1?conn");
        this.player2 = new RemoteSpace(uri + "/player2?conn");

        Card card1 = deck.drawCard();
        System.out.println(card1.toString());
        card1 = deck.drawCard();
        System.out.println(card1.toString());
        card1 = deck.drawCard();
        System.out.println(card1.toString());
        card1 = deck.drawCard();
        System.out.println(card1.toString());
    }
    @Override
    public void run() {
        try {
            startGame();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

