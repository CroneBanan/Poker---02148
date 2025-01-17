package dk.dtu.Server;

import dk.dtu.Common.Card;
import dk.dtu.Common.HandComparator;
import dk.dtu.Common.IHandComparator;
import org.apache.commons.lang3.ArrayUtils;
import org.jspace.*;

import java.util.ArrayList;
import java.util.List;

public class Poker implements Runnable {
    private Space changeSignal;
    private Space turn;
    private CircularList<Player> players = new CircularList();
    private CircularList<Player> blindOrder = new CircularList();
    private int potInCents;
    private ShuffledDeck deck;
    private CardsInPlay cardsInPlay;
    private int highestBet;
    private int turnsSinceHighestBetChange = 0;

    public Poker(String uri, List<Player> players) throws Exception {
        this.changeSignal = new SequentialSpace();
        this.turn = new SequentialSpace();
        this.potInCents = 0;

        for (int i = 0; i < 2; i++) {
            Player player = players.get(i);
            player.setSpace(uri + player.getUriPart() + "?conn");
            player.setCashInCents(10000);
        }
        this.players.setObjects(players);
        this.blindOrder.setObjects(players);
    }

    public void doPlayerTurn() throws InterruptedException {
        Player p = players.getNext();
        //getTurnToken
        turn.get(new ActualField(p.getId()));
        //getAction if playing
        boolean valid = false;
        do{
            if (mayDoAction(p)) {
                Object[] action = p.getSpace().get(
                        new ActualField("Action"),
                        new FormalField(String.class),
                        new FormalField((Integer.class))
                );
                String actionType = (String) action[1];
                int val = (int) action[2];
                //if(isValidAction)
                valid = isActionValid(p, actionType, val);
                if (valid) {
                    // then: do player action() and put 'Valid Action'
                    playerAction(p, actionType, val);
                    p.getSpace().put("Action Feedback", "Valid Action");
                } else {
                    //else put 'Invalid action'
                    p.getSpace().put("Action Feedback", "Invalid Action");
                }
            }
        } while (!valid);
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
                p.addToBet(val);
                potInCents += val;
                p.setStatus("Raise");
                if(highestBet < p.getBetInCents()) {
                    setHighestBet(p.getBetInCents());
                }
                break;
            case "All In":
                potInCents += p.getCashInCents();
                p.addToBet(p.getCashInCents());
                p.setStatus("All In");
                if(highestBet < p.getBetInCents()) {
                    setHighestBet(p.getBetInCents());
                }
                break;
            case "Check":
                p.setStatus("Check");
                break;
            case "Disconnect":
                p.setStatus("Disconnected");
                break;
        }
    }

    public boolean isActionValid(Player p, String actionType, int val) {
        switch (actionType) {
            case "Fold", "Disconnect", "All In":
                return true;
            case "Raise":
                return (p.enoughCash(val) && (val + p.getBetInCents()) >= highestBet);
            case "Check":
                return (p.getBetInCents() == highestBet);
            case "Match":
                return (p.enoughCash(highestBet));
        }
        return false;
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
        blindOrder.getNext().addToBet(1000);
        potInCents += 1000;
        blindOrder.get(0).addToBet(2000);
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
        IHandComparator comparator = new HandComparator();
        ArrayList<Player> winners = new ArrayList<>();
        Player firstPlayer = players.get(0);
        winners.add(firstPlayer);
        Card[] winnerHand = ArrayUtils.addAll(firstPlayer.getHand(), cardsInPlay.getCards());
        for (int i = 1; i < players.size(); i++) {
            Player currentPlayer = players.get(i);
            Card[] currentHand = ArrayUtils.addAll(currentPlayer.getHand(), cardsInPlay.getCards());
            switch (comparator.compareFinalHands(winnerHand, currentHand)) {
                case Larger -> {
                    //do nothing
                }
                case Smaller -> {
                    winners.clear();
                    winners.add(currentPlayer);
                    winnerHand = ArrayUtils.addAll(currentPlayer.getHand(), cardsInPlay.getCards());
                }
                case Equal -> {
                    winners.add(currentPlayer);
                }
            }
        }
        return winners;
    }

    public boolean mayDoAction(Player player) {
        String status = player.getStatus();
        return !(
                status.equals("Fold") ||
                status.equals("Disconnected") ||
                status.equals("Eliminated")
        );
    }

    public int eliminatePlayers() {
        if (potInCents != 0) {
            throw new IllegalStateException("Pot must be distributed first, but is " + potInCents);
        }
        boolean areActiveBets = !players.toList().stream().
                filter(p -> p.getBetInCents() != 0).toList().
                isEmpty();
        if (areActiveBets) {
            throw new IllegalStateException("all bets must have been finished, but not all players bets are 0");
        }

        int eliminations = 0;
        for (Player player : players.toList()) {
            if (player.getCashInCents() <= 0) {
                player.setStatus("Eliminated");
                eliminations++;
            }
        }
        return eliminations;
    }

    public boolean isPlayerActive(Player player) {
        String status = player.getStatus();
        return  !(status.equals("Elimination") || status.equals("Disconnected"));
    }

    public void resetPlayers() {
        for (Player player : players.toList()) {
            if (isPlayerActive(player)) {
                player.setStatus("ready");
                player.setHand(null);
            }
        }
    }


    public void distributePot(ArrayList<Player> winners) {
        for (Player p : winners) {
            p.addCashInCents(potInCents / winners.size());
            System.out.println(p.getName() + " Gets:");
            System.out.println(potInCents / winners.size());
        }
        potInCents = 0;
    }

    public Player currentPlayer() {
        return players.get(0);
    }

    public void startGame() throws Exception {
        //TODO: add while loop - så længe der er spillere med

        new Thread(new StateSender(this, changeSignal)).start();

        while (players.toList().stream().filter(this::isPlayerActive).toList().size() > 1) {
            deck = new ShuffledDeck();
            cardsInPlay = new CardsInPlay();
            resetPlayers();

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
            //TODO: show winning hand???
            eliminatePlayers();
            signalGameStateChange();
        }
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

        @Override
        public void run() {
            try {
                while (true) {
                    signalChange.get(new ActualField("Update"));
                    updateGameState();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public void updateGameState() throws InterruptedException {
            for (Player p : players.toList()) {
                //TODO: tilføj seneste action
                removeGameState(p.getSpace());
                sendGameState(p);
                p.getSpace().put("Update");
            }
        }

        public void sendGameState(Player p) throws InterruptedException {
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
                    getTurnNumber(p),
                    poker.currentPlayer().equals(p) //TODO: FIX - tror ikke det virker
            );
            for (Player ps : players.toList()) {
                if (!ps.equals(p)) {
                    p.getSpace().put("State", "Opponents",
                            ps.getId(),
                            ps.getName(),
                            ps.getCashInCents(),
                            ps.getBetInCents(),
                            ps.getStatus(),
                            getTurnNumber(ps),
                            poker.currentPlayer().equals(ps) //TODO: FIX - tror ikke det virker
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
        }

        public void removeGameState(Space channel) throws InterruptedException {
            channel.getp(
                    new ActualField("State"),
                    new ActualField("Game state"),
                    new FormalField(Integer.class), new FormalField(String.class),
                    new FormalField(Integer.class), new FormalField(String.class),
                    new FormalField(Integer.class), new FormalField(String.class),
                    new FormalField(Integer.class), new FormalField(String.class),
                    new FormalField(Integer.class), new FormalField(String.class),
                    new FormalField(Integer.class), new FormalField(Integer.class)
            );
            channel.getAll(
                    new ActualField("State"),
                    new ActualField("Opponents"),
                    new FormalField(Integer.class),
                    new FormalField(String.class),
                    new FormalField(Integer.class),
                    new FormalField(Integer.class),
                    new FormalField(String.class),
                    new FormalField(Integer.class),
                    new FormalField(Boolean.class)
            );
            channel.getp(
                    new ActualField("State"),
                    new ActualField("Player"),
                    new FormalField(Integer.class),
                    new FormalField(String.class),
                    new FormalField(Integer.class),
                    new FormalField(String.class),
                    new FormalField(Integer.class),
                    new FormalField(String.class),
                    new FormalField(Integer.class),
                    new FormalField(Integer.class),
                    new FormalField(String.class),
                    new FormalField(Integer.class),
                    new FormalField(Boolean.class)
            );
        }

        public int getTurnNumber(Player p) {
            for (int i = 0; i < blindOrder.size(); i++) {
                if (p.equals(blindOrder.get(i))) {
                    return i + 1;
                }
            }
            return blindOrder.size();
        }


    }
}

