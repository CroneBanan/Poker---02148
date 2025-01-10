package dk.dtu.Server;

import org.jspace.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lobby{
    SequentialSpace space;
    List<Player> players = new ArrayList<>();
    SequentialSpace readySpace;
    SpaceRepository spaceRepository;

    public Lobby(String uri, SpaceRepository spaceRepository) throws IOException {
        this.space = new SequentialSpace();
        this.spaceRepository = spaceRepository;
        spaceRepository.add("lobby", space);
        this.readySpace = new SequentialSpace();
    }

    public void open() throws IOException, Exception {
        System.out.println("open lobby");
        while(true){
            System.out.println("waiting for player to connect");
            Object[] t = space.get(new ActualField("connection request"), new FormalField(String.class));
            Player player = new Player(t[1].toString(), "connected");
            players.add(player);
            Space playerChannel = new SequentialSpace();
            spaceRepository.add(player.getUriPart(),playerChannel);
            space.put("Player connected",player.getUriPart(), player.getName());
            System.out.println("player "+ player.getName() + " connected");

            handlePlayerChannel(playerChannel, player);
        }
    }

    public void handlePlayerChannel(Space playerChannel,Player player) throws Exception {
        while(true){
            Object[] t = playerChannel.get(new ActualField("lobby"), new FormalField(String.class));
            if(t[1].equals("ready")){
                System.out.println("ready");
                player.setStatus("ready");
                readySpace.put(player.getId(),player.getName());
            }
            if(t[1].equals("disconnect")){
                System.out.println("disconnect");
                player.setStatus("disconnected");
                readySpace.getp(new ActualField(player.getId()), new ActualField(player.getName()));
                players.remove(player);
                break;
            }
        }
    }


}
