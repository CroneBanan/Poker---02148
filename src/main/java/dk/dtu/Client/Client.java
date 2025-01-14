package dk.dtu.Client;

import dk.dtu.Common.Card;
import dk.dtu.Common.Suite;
import dk.dtu.Server.ShuffledDeck;
import org.jspace.*;

import java.io.IOException;

public class Client {
    public static void main(String[] args) throws Exception {
        Space instructions = new SequentialSpace();
        Space inputs = new SequentialSpace();
        new Thread(new UserInput(instructions,inputs)).start();
        String ip = "local host";
        int port = 7324;
        String uri = "tcp://" + ip + ":" + port;
        instructions.put("getName","What is your name? \n");
        String playerName = (String) inputs.get(new ActualField("getName"),new FormalField(String.class))[1];
        RemoteSpace channel = connect(playerName, uri);

        while (channel.queryp(new ActualField("lobbyState"),new ActualField("Open")) != null) {
            instructions.put("lobbyAction","Waiting for Game to start. \navailable commands: \'ready\',\'disconnect\'");
            String lobbyAction = (String) inputs.get(new ActualField("lobbyAction"),new FormalField(String.class))[1];
            lobbyAction = lobbyAction.toLowerCase().trim();
            if (lobbyAction.equals("ready")) {
                ready(channel);
            } else if (lobbyAction.equals("disconnect")) {
                disconnectFromLobby(channel);
            } else {
                System.out.println("command not recognized");
            }
        }

        Object[] t = channel.get(
                new ActualField("State"),
                new ActualField("Private player info"),
                new FormalField(Integer.class),
                new FormalField(String.class),
                new FormalField(Integer.class),
                new FormalField(String.class)
        );
        for (int i = 0; i < 6; i++) {
            System.out.println(t[i]);
        }


        //ready(channel);
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