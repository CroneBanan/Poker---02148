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

    public void startGame() throws IOException, InterruptedException {
        initializeDeck();
        this.player1 = new RemoteSpace(uri + "/player1?conn");
        this.player2 = new RemoteSpace(uri + "/player2?conn");

        System.out.println(deck.get(0).toString());
        player1.put("Cards", deck.get(0));
        deck.remove(0);

        System.out.println(deck.get(0).toString());
        player1.put("Cards", deck.get(0));
        deck.remove(0);

        System.out.println(deck.get(0).toString());
        player2.put("Cards", deck.get(0));
        deck.remove(0);

        System.out.println(deck.get(0).toString());
        player2.put("Cards", deck.get(0));
        deck.remove(0);
    }
    @Override
    public void run() {
        try {
            startGame();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

