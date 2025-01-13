package dk.dtu.Server;

import dk.dtu.Common.Card;
import dk.dtu.Common.ComparisonResult;
import dk.dtu.Common.HandComparator;
import dk.dtu.Common.IHandComparator;
import org.apache.commons.lang3.ArrayUtils;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Poker implements Runnable {
    private String uri;
    private RemoteSpace space;
    private RemoteSpace turn;
    private CircularList<Player> players = new CircularList();
    private CircularList<Player> blindOrder = new CircularList();
    //private ArrayList<Player> activePlayers = players;
    private int pot;
    private ShuffledDeck deck;
    private Card[] cardsInPlay;
    private int highestBet;
    private int turnsSinceHighestBetChange = 0;

    public Poker(String uri) throws IOException {
        this.space = new RemoteSpace(uri + "/gameState?conn");
        this.turn = new RemoteSpace(uri + "/turn?conn");
        this.uri = uri;
        this.pot = 0;
    }
    public void doPlayerTurn() throws InterruptedException {
        Player p = players.getNext();
        //getTurnToken
        turn.get(new ActualField(p.getId()));
        //getAction if playing
        if (!p.getStatus().equals("Fold")) {
            Object[] action = p.getSpace().get(
                    new ActualField("Action"),
                    new FormalField(String.class),
                    new FormalField((Integer.class))
            );
            String actionType = (String) action[1];
            int val = (int) action[2];
            //if(isValidAction)
            if (isActionValid(p, actionType, val)) {
                // then: do player action() and put 'Valid Action'
                playerAction(p, actionType, val);
                p.getSpace().put("Action Feedback", "Valid Action");
            } else {
                //else put 'Invalid action'
                p.getSpace().put("Action Feedback", "Invalid Action");
            }
        }
        //putTurnToken
        turn.put(players.get(0).getId());

        signalGameStateChange();
    }

    public void signalGameStateChange() {
        //TODO: SKAL IMPLEMENTERES
        return;
    }

    public void playerAction(Player p, String actionType, int val) throws InterruptedException {
        switch (actionType) {
            case "Fold":
                p.setStatus("Fold");
                break;
            case "Raise":
                p.makeBet(val);
                pot += val;
                p.setStatus("Raise");
                if(highestBet < val) {
                    setHighestBet(val);
                }
                break;
            case "All In":
                pot += p.getCash();
                p.makeBet(p.getCash());
                p.setStatus("All In");
                if(highestBet < val) {
                    setHighestBet(val);
                }
                break;
            case "Check":
                p.setStatus("Check");
                break;
        }
    }

    public boolean isActionValid(Player p, String actionType, int val) {
        switch (actionType) {
            case "Fold":
                return true;
            case "Raise":
                return (p.enoughCash(val) && (val + p.getBet()) >= highestBet);
            case "All In":
                return true;
            case "Check":
                return (p.getBet() == highestBet);
            case "Match":
                return (p.enoughCash(highestBet));
        }
        return true;
    }

    public void bettingRound() throws InterruptedException {
        Player player = blindOrder.get(1);
        while (players.get(0).getId() != (player.getId())) {
            players.getNext();
        }
        turn.put(player.getId());
        do {
            //take turn
            doPlayerTurn();
            turnsSinceHighestBetChange += 1;
        } while (turnsSinceHighestBetChange != players.toList().size());
        setHighestBet(0);


        //clear bets
        for (Player p : players.toList()) {
            p.setBet(0);
        }
    }

    public void setHighestBet(int highestBet) {
        this.highestBet = highestBet;
        turnsSinceHighestBetChange = 0;
    }

    public void setBlinds() {
        blindOrder.getNext().makeBet(10);
        pot += 10;
        blindOrder.get(0).makeBet(20);
        pot += 20;
        setHighestBet(20);
    }

    public List<Player> activePlayers() {
        return players.toList().stream().filter(p -> !p.getStatus().equals("Fold")).toList();
    }

    public void dealCards() throws Exception {
        for (Player player : players.toList()) {
            player.setHand(deck.deal());
        }
    }

    public ArrayList<Player> findWinners() {
        ArrayList<Player> winners = new ArrayList<>();
        Card[] player1Hand = ArrayUtils.addAll(players.get(0).getHand(), cardsInPlay);
        Card[] player2Hand = ArrayUtils.addAll(players.get(1).getHand(), cardsInPlay);

        IHandComparator comparator = new HandComparator();
        ComparisonResult result = comparator.compareFinalHands(player1Hand, player2Hand);
        if (result == ComparisonResult.Larger) {
            winners.add(players.get(0));
        } else if (result == ComparisonResult.Smaller) {
            winners.add(players.get(1));
        } else {
            winners.add(players.get(0));
            winners.add(players.get(1));
        }
        return winners;
    }

    public void distributePot(ArrayList<Player> winners) {
        for (Player p : winners) {
            p.addCash(pot / winners.size());
            System.out.println(p.getName() + " Gets:");
            System.out.println(pot / winners.size());
        }
        pot = 0;
    }

    public void startGame() throws Exception {
        deck = new ShuffledDeck();
        List<Player> ps = new ArrayList<>();
        ps.add(new Player("Christian", 10000, "/player1"));
        ps.add(new Player("Shannon", 1000000, "/player2"));
        ps.get(0).setSpace(uri + ps.get(0).getUriPart() + "?conn");
        ps.get(1).setSpace(uri + ps.get(1).getUriPart() + "?conn");
        ps.get(0).getSpace().put("Action","Raise",10);

        for (int i = 0; i < 10; i++) {
            ps.get(0).getSpace().put("Action","Check",0);
            ps.get(1).getSpace().put("Action","Check",0);
        }
        players.setObjects(ps);
        blindOrder.setObjects(ps);

        setBlinds();
        dealCards();
        bettingRound();
        flop();
        bettingRound();
        turn();
        bettingRound();
        river();
        bettingRound();

        ArrayList<Player> winners = findWinners();
        distributePot(winners);

        System.out.println("DONE");
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

