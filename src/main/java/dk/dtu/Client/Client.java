package dk.dtu.Client;

import dk.dtu.Common.Card;
import dk.dtu.Common.Suite;
import dk.dtu.Server.ShuffledDeck;
import org.jspace.*;

import java.io.IOException;

public class Client {
    public static void main(String[] args) throws Exception {
        UserInput userInput = new UserInput();
        userInput.start();
        String ip = "localhost";
        int port = 7324;
        String uri = "tcp://" + ip + ":" + port;
        userInput.tryQueuePrompt("getName","What is your name? \n");
        String playerName = userInput.getInput("getName");
        RemoteSpace channel = connect(playerName, uri);

        while (true) {
            userInput.tryQueuePrompt("lobbyAction","Waiting for Game to start. \navailable commands: \'ready\',\'disconnect\'");
            String lobbyAction = userInput.getInput("lobbyAction");
            lobbyAction = lobbyAction.toLowerCase().trim();
            if (lobbyAction.equals("ready")) {
                ready(channel);
                break;
            } else if (lobbyAction.equals("disconnect")) {
                disconnectFromLobby(channel);
                break;
            } else {
                System.out.println("command not recognized");
            }
        }

        channel.query(new ActualField("lobbyState"),new ActualField("Closed"));

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

    public static void getPlayerAction(RemoteSpace channel, Space instructions, Space inputs) throws InterruptedException {
        do {
            instructions.put("Action","What do you want to do? Raise, Check, Fold? \n");
            String playerAction = (String) inputs.get(new ActualField("Action"),new FormalField(String.class))[1];
            if (playerAction.equals("Raise")) {
                instructions.put("Raise", "How much do you wanna bet?");
                String Sbet = (String) inputs.get(new ActualField("Raise"), new FormalField(String.class))[1];
                int bet = Integer.parseInt(Sbet);
                action(channel, playerAction, bet);
            } else {
                action(channel, playerAction, 0);
            }
        } while(validateAction());
    }

    private static boolean validateAction() {
        //TODO: Action skal valideres, selvom det ikke er spillerens tur.
        return true;

    }

    public static void action(RemoteSpace channel, String playerAction, int bet) {
            try {
                channel.put("Action", playerAction, bet);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
}