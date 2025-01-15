package dk.dtu.Server;

import org.jspace.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lobby{
    private SequentialSpace space;
    private List<Player> players;
    private SequentialSpace readySpace;
    private SequentialSpace state;
    private SpaceRepository spaceRepository;

    public Lobby(String uri, SpaceRepository spaceRepository) throws IOException {
        this.space = new SequentialSpace();
        this.spaceRepository = spaceRepository;
        spaceRepository.add("lobby", space);
        this.readySpace = new SequentialSpace();
        this.state = new SequentialSpace();
        players = new ArrayList<>();
    }

    public void open() throws IOException, Exception {
        System.out.println("open lobby");
        state.put("Open");
        do {
            Object[] t = space.getp(new ActualField("connection request"), new FormalField(String.class));
            if (t != null) {
                System.out.println("waiting for player to connect");
                Player player = addPlayer(t[1].toString());
                space.put("Player connected",
                        player.getUriPart(),
                        player.getName()
                );
                System.out.println("player "+ player.getName() + " connected");
                new Thread(new ChannelHandler(player,readySpace,state)).start();
            }
        } while (!ArePlayersReady());
        state.get(new ActualField("Open"));
        space.put("Status","Closed");
    }

    private boolean ArePlayersReady() {
        int readyPlayers = readySpace.queryAll(
                new FormalField(Integer.class),
                new FormalField(String.class)
        ).size();
        return (readyPlayers >= 2 && readyPlayers == players.size());
    }

    public Player addPlayer(String name) {
        Player player = new Player(name, "connected");
        players.add(player);
        spaceRepository.add(player.getUriPart(),player.getChannel());
        return player;
    }

    public List<Player> getPlayers() {
        return players;
    }



    private class ChannelHandler implements Runnable {
        private Space readySpace;
        private Space lobbyState;
        Player player;
        public ChannelHandler(Player player, Space readySpace,Space lobbyState) {
            this.player = player;
            this.readySpace = readySpace;
            this.lobbyState = lobbyState;
        }

        @Override
        public void run() {
            try {
                handlePlayerChannel();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void handlePlayerChannel() throws Exception {
            player.getChannel().put("lobbyState","Open");
            while(lobbyState.queryp(new ActualField("Open")) != null){
                Object[] t = player.getChannel().getp(new ActualField("lobby"), new FormalField(String.class));
                if (t == null) {
                    continue;
                }
                if(t[1].equals("ready")){
                    System.out.println("ready");
                    player.setStatus("ready");
                    readySpace.put(player.getId(),player.getName());
                } else if(t[1].equals("disconnect")){
                    System.out.println("disconnect");
                    player.setStatus("disconnected");
                    readySpace.getp(new ActualField(player.getId()), new ActualField(player.getName()));
                    players.remove(player);
                    break;
                }
            }
            player.getChannel().get(new ActualField("lobbyState"),new ActualField("Open"));
            player.getChannel().put("lobbyState", "Closed");
        }
    }


}
