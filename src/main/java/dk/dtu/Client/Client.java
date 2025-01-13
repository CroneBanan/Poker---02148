package dk.dtu.Client;

import dk.dtu.Common.Card;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import java.io.IOException;

import org.jspace.ActualField;

public class Client {
    public static void main(String[] args) throws Exception {
        RemoteSpace space;
        RemoteSpace player;
        String ip = "localhost";
        int port = 7324;
        String uri = "tcp://" + ip + ":" + port;
        String player1 = "player1";

        space = new RemoteSpace(uri + "/gameState?conn");
        RemoteSpace channel = connect(player1, uri);
        ready(channel);
        //disconnectFromLobby(channel);
       // String test = (String) space.get(new ActualField("hej"))[0];

        //System.out.println(test);

        //Object[] cards = channel.get(new ActualField("Cards"), new FormalField(Card.class));
        //System.out.println(cards[0].toString());
        //System.out.println(cards[1].toString());
    }

    // OBS skal nok bruge en playerInfo klasse som har det her navn.
    public static RemoteSpace connect(String playerName,String uri) throws Exception{
        System.out.println("Connecting to lobby");
        RemoteSpace lobby = new RemoteSpace(uri + "/lobby?conn");
        lobby.put("connection request", playerName);
        System.out.println("attempting to connect");
        Object[] tuple = lobby.get(new ActualField("Player connected"), new FormalField(String.class), new ActualField(playerName));
        String uriPart = (String) tuple[1];
        RemoteSpace channel = new RemoteSpace(uri + "/" + uriPart + "?conn");;
        System.out.println("Connected to lobby");
        return channel;
    }

    public static void ready(RemoteSpace channel) {
        try {
            channel.put("lobby","ready");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void disconnectFromLobby(RemoteSpace channel) {
        try {
            channel.put("lobby","disconnect");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
