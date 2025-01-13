package dk.dtu.Server;

import dk.dtu.Common.Card;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.SpaceRepository;

import java.io.IOException;
import java.util.ArrayList;

public class Poker implements Runnable {
    private String uri;
    private RemoteSpace space;
    private RemoteSpace turn;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Player> activePlayers = players;
    private int pool;
    private ShuffledDeck deck;
    private Card[] cardsInPlay;
    private int highestBet;

    public Poker(String uri) throws IOException {
        this.space = new RemoteSpace(uri + "/gameState?conn");
        this.turn = new RemoteSpace(uri + "/turn?conn");
        this.uri = uri;
        this.pool = 0;
    }

    public void playerAction(Player p, String actionType, int val) throws InterruptedException {
        switch (actionType) {
            case "Fold":
                p.setStatus("Fold");
                this.activePlayers.remove(0);
                break;
            case "Raise":
                p.makeBet(val);
                p.setStatus("Raise");
                if(highestBet < val) {
                    highestBet = val;
                }
                break;
            case "All In":
                p.makeBet(p.getCash());
                p.setStatus("All In");
                if(highestBet < val) {
                    highestBet = val;
                }
                break;
            case "Check":
                p.setStatus("Check");
                break;
        }
    }

    public boolean isActionValid(Player player, String action) {
        return true;
        //TODO: inputs er bare et gæt, måske skal der også være en værdi tilknyttet fx et raise.
    }

    public void setBlinds() {
        Player first = players.get(0);
        first.makeBet(10);
        pool += 10;
        players.get(1).makeBet(20);
        pool += 20;
        players.remove(0);
        players.add(players.size(), first);
        highestBet = 20;
    }

    public boolean checkRound() {
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
        for (Player player : players) {
            player.setHand(deck.deal());
        }
    }

    public void startGame() throws Exception {
        deck = new ShuffledDeck();
        players.add(new Player("Christian", 10000, "/player1"));
        players.add(new Player("Shannon", 1000000, "/player2"));
        players.get(0).setSpace(uri + players.get(0).getUriPart() + "?conn");
        players.get(1).setSpace(uri + players.get(1).getUriPart() + "?conn");

        setBlinds();
        dealCards();

        flop();
        space.put("Flop", cardsInPlay);


    }

    private void flop() throws Exception {
        cardsInPlay = new Card[]{deck.drawCard(),deck.drawCard(),deck.drawCard(),null,null};
    }
    private void turn() throws Exception {
        cardsInPlay[3] = deck.drawCard();
    }
    private void river() throws Exception {
        cardsInPlay[4] = deck.drawCard();
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

