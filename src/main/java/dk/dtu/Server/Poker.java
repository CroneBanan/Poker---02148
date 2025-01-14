package dk.dtu.Server;

import dk.dtu.Common.Card;
import dk.dtu.Common.ComparisonResult;
import dk.dtu.Common.HandComparator;
import dk.dtu.Common.IHandComparator;
import org.apache.commons.lang3.ArrayUtils;
import org.jspace.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Poker implements Runnable {
    private Space changeSignal;
    private RemoteSpace turn;
    private CircularList<Player> players = new CircularList();
    private CircularList<Player> blindOrder = new CircularList();
    //private ArrayList<Player> activePlayers = players;
    private int pot;
    private ShuffledDeck deck;
    private Card[] cardsInPlay;
    private int highestBet;
    private int turnsSinceHighestBetChange = 0;

    public Poker(String uri, List<Player> players) throws Exception {
        this.changeSignal = new SequentialSpace();
        this.turn = new RemoteSpace(uri + "/turn?conn");
        this.pot = 0;

        for (int i = 0; i < 2; i++) {
            Player player = players.get(i);
            player.setSpace(uri + player.getUriPart() + "?conn");
            player.setCash(100);
        }
        players.get(0).getSpace().put("Action","Raise",10);
        for (int i = 0; i < 10; i++) {
            players.get(0).getSpace().put("Action","Check",0);
            players.get(1).getSpace().put("Action","Check",0);
        }
        this.players.setObjects(players);
        this.blindOrder.setObjects(players);
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
        signalGameStateChange();
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

    public List<Player> getPlayersAsList() {
        return players.toList();
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
        new Thread(new StateSender(this, changeSignal)).start();

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
        signalGameStateChange();
        System.out.println("DONE");
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
    public void signalGameStateChange() throws InterruptedException {
        changeSignal.put("Update");
    }

    private class StateSender implements Runnable {
        private Poker poker;
        private Space signalChange;

        public StateSender(Poker poker, Space signalChange) {
            this.poker = poker;
            this.signalChange = signalChange;
        }

        public void sendGameState() throws InterruptedException {
            for (Player p : poker.getPlayersAsList()) {
                System.out.println(p.getName());
                p.getSpace().put("State", "Private player info",
                        p.getHand()[0].getValue(),
                        p.getHand()[0].getSuite().name(),
                        p.getHand()[1].getValue(),
                        p.getHand()[1].getSuite().name());
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    System.out.println("signal change");
                    signalChange.get(new ActualField("Update"));
                    sendGameState();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

