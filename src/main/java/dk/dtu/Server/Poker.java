package dk.dtu.Server;

import dk.dtu.Common.Card;
import org.jspace.ActualField;
import org.jspace.RemoteSpace;
import org.jspace.SpaceRepository;

import java.io.IOException;
import java.util.ArrayList;

public class Poker implements Runnable {
    private String uri;
    private RemoteSpace space;
    private ArrayList<RemoteSpace> playerSpaces = new ArrayList<>();
    private ArrayList<RemoteSpace> activePlayerSpaces = playerSpaces;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Player> activePlayers = players;
    private int pool;
    private ShuffledDeck deck;
    private Card[] cardsInPlay;
    private int highestBet;

    public Poker(String uri) throws IOException {
        this.space = new RemoteSpace(uri + "/gameState?conn");
        this.uri = uri;
        this.pool = 0;
    }

    public void bet() {

    }

    public void setBlinds() {
        Player first = players.get(0);
        RemoteSpace firstSpace = playerSpaces.get(0);
        first.makeBet(10);
        pool += 10;
        players.get(1).makeBet(20);
        pool += 20;
        players.remove(0);
        players.add(players.size(), first);
        playerSpaces.remove(0);
        playerSpaces.add(players.size(), firstSpace);
        highestBet = 20;
    }

    public boolean checkRound() {
        activePlayers.removeIf(p -> p.getStatus().equals("Fold"));
        for (Player p : activePlayers) {
            if (highestBet < p.getBet()) {
                highestBet = p.getBet();
            }
        }

        for (Player p : activePlayers) {
            if (highestBet != p.getBet()) {
                if (!p.getStatus().equals("All in")) {
                    return false;
                }
            }
        }
        return true;
    }

    public void dealCards() throws Exception {
        deck = new ShuffledDeck();
        for (int i = 0; i < players.size(); i++) {
            Card[] cards = deck.deal();
            players.get(i).setHand(cards);
            playerSpaces.get(i).put("Hand", cards);
        }
    }

    public void startGame() throws Exception {
        players.add(new Player("Christian", 10000, "/player1"));
        players.add(new Player("Shannon", 1000000, "/player2"));
        playerSpaces.add(new RemoteSpace(uri + players.get(0).getUriPart() + "?conn"));
        playerSpaces.add(new RemoteSpace(uri + players.get(1).getUriPart() + "?conn"));

        setBlinds();
        dealCards();
        cardsInPlay = deck.flop();

        space.put("Flop", deck.flop());


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

