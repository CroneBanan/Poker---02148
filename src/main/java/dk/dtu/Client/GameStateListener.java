package dk.dtu.Client;

import org.jspace.*;
import java.util.ArrayList;
import java.util.List;

public class GameStateListener {

    private PokerInfo newestGameState;
    private Space gameStateUpdate;
    private Listener listener;
    private int updateVersion;

    public GameStateListener(Space channel) {
        this.newestGameState = null;
        this.gameStateUpdate = new SequentialSpace();
        this.listener = new Listener(channel, gameStateUpdate);
    }

    public void start() {
        new Thread(listener).start();
    }

    public PokerInfo getNewestGameState() {
        return newestGameState;
    }

    public Space getGameStateUpdate() {
        return gameStateUpdate;
    }

    public void setNewestGameState(PokerInfo gameState) {
        newestGameState = gameState;
    }

    private class Listener implements Runnable {
        private Space channel;
        private Space gameStateUpdate;

        public Listener(Space channel, Space gameStateUpdate) {
            this.channel = channel;
            this.gameStateUpdate = gameStateUpdate;
        }

        public static PlayerInfo getPlayerInfo(Space channel) throws InterruptedException {
            Object[] t = channel.query(
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
            CardInfo[] hand = new CardInfo[]{
                    new CardInfo((int) t[2], (String) t[3]),
                    new CardInfo((int) t[4], (String) t[5])};
            return new PlayerInfo(
                    (int) t[6], (String) t[7], (int) t[8], hand,
                    (int) t[9], (String) t[10], (int) t[11],
                    (boolean) t[12]);
        }

        public static PokerInfo getGameInfo(Space channel) throws InterruptedException {
            List<Object[]> ps = channel.queryAll(
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
            ArrayList<PlayerInfo> players = new ArrayList<>();
            for (Object[] t : ps) {
                players.add(
                        new PlayerInfo(
                            (int) t[2], (String) t[3], (int) t[4],
                            (int) t[5], (String) t[6], (int) t[7], (boolean) t[8]
                        )
                );
            }
            PlayerInfo currentPlayer = getPlayerInfo(channel);
            players.add(currentPlayer);
            CardInfo[] cardsInPlay = new CardInfo[5];
            Object[] t = channel.query(
                    new ActualField("State"),
                    new ActualField("Game state"),
                    new FormalField(Integer.class), new FormalField(String.class),
                    new FormalField(Integer.class), new FormalField(String.class),
                    new FormalField(Integer.class), new FormalField(String.class),
                    new FormalField(Integer.class), new FormalField(String.class),
                    new FormalField(Integer.class), new FormalField(String.class),
                    new FormalField(Integer.class), new FormalField(Integer.class));
            for (int i = 1; i <= 5; i++) {
                int value = (int) t[i * 2];
                if (value != 0) {
                    cardsInPlay[i - 1] = new CardInfo(value, (String) t[i * 2 + 1]);
                }
            }
            return new PokerInfo(players, currentPlayer, (int) t[12], cardsInPlay, (int) t[13]);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    channel.get(new ActualField("Update"));
                    setNewestGameState(getGameInfo(channel));
                    gameStateUpdate.put("Update");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
