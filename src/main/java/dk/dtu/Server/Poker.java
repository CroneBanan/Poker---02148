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
    private int potInCents;
    private ShuffledDeck deck;
    private CardsInPlay cardsInPlay = new CardsInPlay();;
    private int highestBet;
    private int turnsSinceHighestBetChange = 0;

    public Poker(String uri, List<Player> players) throws Exception {
        this.changeSignal = new SequentialSpace();
        this.turn = new RemoteSpace(uri + "/turn?conn");
        this.potInCents = 0;

        for (int i = 0; i < 2; i++) {
            Player player = players.get(i);
            player.setSpace(uri + player.getUriPart() + "?conn");
            player.setCashInCents(10000);
        }
        players.get(0).getSpace().put("Action","Raise",1000);
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
                potInCents += val;
                p.setStatus("Raise");
                if(highestBet < val) {
                    setHighestBet(val);
                }
                break;
            case "All In":
                potInCents += p.getCashInCents();
                p.makeBet(p.getCashInCents());
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
                return (p.enoughCash(val) && (val + p.getBetInCents()) >= highestBet);
            case "All In":
                return true;
            case "Check":
                return (p.getBetInCents() == highestBet);
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
            p.setBetInCents(0);
        }
    }

    public void setHighestBet(int highestBet) {
        this.highestBet = highestBet;
        turnsSinceHighestBetChange = 0;
    }

    public void setBlinds() {
        blindOrder.getNext().makeBet(1000);
        potInCents += 1000;
        blindOrder.get(0).makeBet(2000);
        potInCents += 2000;
        setHighestBet(2000);
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
        Card[] player1Hand = ArrayUtils.addAll(players.get(0).getHand(), cardsInPlay.getCards());
        Card[] player2Hand = ArrayUtils.addAll(players.get(1).getHand(), cardsInPlay.getCards());

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
            p.addCashInCents(potInCents / winners.size());
            System.out.println(p.getName() + " Gets:");
            System.out.println(potInCents / winners.size());
        }
        potInCents = 0;
    }

    public void startGame() throws Exception {
        deck = new ShuffledDeck();
        new Thread(new StateSender(this, changeSignal)).start();

        setBlinds();
        dealCards();
        bettingRound();
        cardsInPlay.flop(deck);
        bettingRound();
        cardsInPlay.turn(deck);
        bettingRound();
        cardsInPlay.river(deck);
        bettingRound();

        ArrayList<Player> winners = findWinners();
        distributePot(winners);
        signalGameStateChange();
        System.out.println("DONE");
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
            for (Player p : players.toList()) {
                p.getSpace().put("State", "Player",
                        p.getHand()[0].getValue(),
                        p.getHand()[0].getSuite().toString(),
                        p.getHand()[1].getValue(),
                        p.getHand()[1].getSuite().toString(),
                        p.getId(),
                        p.getName(),
                        p.getCashInCents(),
                        p.getBetInCents(),
                        p.getStatus(),
                        getTurnNumber(p)
                );
                for (Player ps : players.toList()) {
                    if (!ps.equals(p)) {
                        p.getSpace().put("State", "Opponents",
                                ps.getId(),
                                ps.getName(),
                                ps.getCashInCents(),
                                ps.getBetInCents(),
                                ps.getStatus(),
                                getTurnNumber(ps)
                        );
                    }
                }
                p.getSpace().put("State", "Game state",
                        cardsInPlay.get(0).getValue(), cardsInPlay.get(0).getSuite().toString(),
                        cardsInPlay.get(1).getValue(), cardsInPlay.get(1).getSuite().toString(),
                        cardsInPlay.get(2).getValue(), cardsInPlay.get(2).getSuite().toString(),
                        cardsInPlay.get(3).getValue(), cardsInPlay.get(3).getSuite().toString(),
                        cardsInPlay.get(4).getValue(), cardsInPlay.get(4).getSuite().toString(),
                        potInCents, highestBet);
                p.getSpace().put("Update");
            }
        }

        public int getTurnNumber(Player p) {
            for (int i = 0; i < blindOrder.size(); i++) {
                if (p.equals(blindOrder.get(i))) {
                    return i + 1;
                }
            }
            return blindOrder.size();
        }

        @Override
        public void run() {
            try {
                while (true) {
                    signalChange.get(new ActualField("Update"));
                    sendGameState();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

