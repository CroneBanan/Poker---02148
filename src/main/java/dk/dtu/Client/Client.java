package dk.dtu.Client;

import dk.dtu.Common.Card;
import dk.dtu.Common.Suite;
import dk.dtu.Server.ShuffledDeck;
import org.jspace.*;

import java.io.IOException;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public static void main(String[] args) throws Exception {
        Screen screen = new Screen();
        UserInput userInput = new UserInput();
        userInput.start();
        String ip = "localhost";
        int port = 7324;
        String uri = "tcp://" + ip + ":" + port;
        screen.welcomeScreen();
        userInput.tryQueuePrompt("getName","What is your name? \n");
        String playerName = userInput.getInput("getName");
        RemoteSpace channel = connect(playerName, uri);
        screen.lobbyScreen();
        userInput.queuePrompt("lobbyAction","Waiting for Game to start. \navailable commands: \'ready\',\'disconnect\'");
        while (channel.queryp(new ActualField("lobbyState"),new ActualField("Closed")) == null) {
            if(userInput.isInputReady("lobbyAction")) {
                String lobbyAction = userInput.getInput("lobbyAction");

                lobbyAction = lobbyAction.toLowerCase().trim();
                if (lobbyAction.equals("ready")) {
                    ready(channel);
                } else if (lobbyAction.equals("disconnect")) {
                    disconnectFromLobby(channel);
                    break;
                } else {
                    System.out.println("command not recognized");
                }
                userInput.tryQueuePrompt("lobbyAction","Waiting for Game to start. \navailable commands: \'ready\',\'disconnect\'");
            }
        }
        System.out.println("Game has started. press any key and then ENTER to continue");
        userInput.getInput("lobbyAction");
        //userInput.restart();

        channel.query(new ActualField("lobbyState"),new ActualField("Closed"));
        GameStateListener listener = new GameStateListener(channel);
        listener.start();

        while (true) {
            updateGame(listener, screen);

            getPlayerAction(channel, userInput);
        }

        /*
        for(int i = 0; i < 100; i++) {
            PokerInfo game = getGameInfo(channel);
            screen.show(game);
        }
        */

        //ready(channel);
        //disconnectFromLobby(channel);
        // String test = (String) space.get(new ActualField("hej"))[0];

        //System.out.println(test);

        //Object[] cards = channel.get(new ActualField("Cards"), new FormalField(Card.class));
        //System.out.println(cards[0].toString());
        //System.out.println(cards[1].toString());
    }

    public static void updateGame(GameStateListener listener, Screen screen) throws InterruptedException {
        listener.getGameStateUpdate().get(new ActualField("Update"));
        PokerInfo game = listener.getNewestGameState();
        if (game != null) {
            screen.show(game);
        }
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

    public String getActionType(UserInput userInput) throws InterruptedException {
        userInput.queuePrompt("Action","What do you want to do? Raise, Check, Fold? \n");
        return userInput.getInput("Action");
    }

    public static void getPlayerAction(RemoteSpace channel, UserInput userInput) throws InterruptedException {
        String feedback;
        do {
            do {
                userInput.queuePrompt("Action","What do you want to do? Raise, Check, Fold? \n");
                //1
                String playerAction = userInput.getInput("Action");
                if (playerAction.equals("Raise")) {
                    int bet = 0;
                    do {
                        userInput.queuePrompt("Raise", "How much do you wanna bet?");
                        //2
                        String Sbet = userInput.getInput("Raise");
                        try{
                            bet = Integer.parseInt(Sbet);
                        }catch (Exception e) {
                            System.out.println("Input an integer");
                        }
                    }while(bet == 0);
                    action(channel, playerAction, bet);
                    break;
                } else {
                    action(channel, playerAction, 0);
                    break;
                }
            } while(validateAction());
            feedback = (String) channel.get(new ActualField("Action Feedback"), new FormalField(String.class))[1];
        } while (!feedback.equals("Valid Action"));

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