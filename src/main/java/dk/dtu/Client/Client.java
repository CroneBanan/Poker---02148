package dk.dtu.Client;

import org.jspace.*;

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
        System.out.println("Game has started. press ANY KEY THEN ENTER to continue");
        userInput.getInput("lobbyAction");

        channel.query(new ActualField("lobbyState"),new ActualField("Closed"));


        //the actual poker game::
        GameStateListener listener = new GameStateListener(channel);
        listener.start();
        while (true) {
            PokerInfo game = updateGame(listener, screen,userInput,channel);
            handleUser(game,channel,userInput,screen);
        }
    }

    public static PokerInfo updateGame(GameStateListener listener, Screen screen, UserInput userInput,Space channel) throws Exception {
        PokerInfo game = listener.getNewestGameState();
        if (listener.getGameStateUpdate().queryp(new ActualField("Update")) == null) {
            return game;
        }
        listener.getGameStateUpdate().get(new ActualField("Update"));
        refreshScreen(game,screen,userInput,channel);
        return game;

    }

    public static void refreshScreen(PokerInfo game, Screen screen,UserInput userInput, Space channel) throws Exception {
        if (game == null) {
            screen.clearScreen();
            System.out.println("Game state could not be loaded");
            return;
        }

        screen.show(game);
        System.out.println("Queued Action: " + getQueuedAction(channel));
        userInput.tryRepromptCurrent();
        System.out.println();
    }

    public static void handleUser(PokerInfo game, RemoteSpace channel, UserInput userInput,Screen screen) throws Exception {
        userInput.tryQueuePrompt("Action","What do you want to do? Raise <val>, Check, Fold? \n");
        if (!userInput.isInputReady("Action")) {
            return;
        }

        String playerAction = userInput.getInput("Action").trim();
        String ActionType = playerAction.split(" ")[0].trim();
        int val = 0;
        try {
            if (ActionType.equals("Raise")) {
                val = Integer.parseInt(playerAction.split(" ")[1].trim());
            }
        } catch (Exception e) {
            System.out.println("use of Raise is Raise <val> eg Raise 20");
            return;
        }
        //do client validation
        if (!validateAction(game, ActionType, val)){
            System.out.println("Invalid action. Try again");
            return;
        }

        //do action (ie. Send it to the server)
        setNextAction(channel, ActionType, val);
        refreshScreen(game,screen,userInput,channel);
        userInput.tryQueuePrompt("Action","What do you want to do? Raise <val>, Check, Fold? \n");
        //get action feedback
        String feedback = (String) channel.get(new ActualField("Action Feedback"), new FormalField(String.class))[1];
        //handle action feedback:
        if(feedback.equals("Invalid Action")) {
            System.out.println("Invalid action. Try again");
            return;
        } else {
            System.out.println("You performed action: " + ActionType);
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


    private static boolean validateAction(PokerInfo game, String ActionType, int val) {
        PlayerInfo currentPlayer = game.getCurrentPlayer();
        switch (ActionType) {
            case "Raise":
                return currentPlayer.getCashInCents() >= val && game.getHighestBet() <= val + currentPlayer.getBet();
            case "Check":
                return game.getHighestBet() == currentPlayer.getBet();
            case "Fold":
                return true;
        }
        return false;

    }

    public static String getQueuedAction(Space channel) throws InterruptedException {
        Object[] tuple = channel.queryp(
                new ActualField("Action"),
                new FormalField(String.class),
                new FormalField(Integer.class)
        );
        if (tuple == null) {
            return "";
        }
        String actionType = (String) tuple[1];
        String val = actionType.equals("Raise") ?  Integer.toString((int) tuple[2]) : "";
        return actionType + " " + val;
    }

    /**
     * Set the next action overriding any previously specified actions not yet read by the server
     * @param channel
     * @param playerAction
     * @param bet
     */
    public static void setNextAction(RemoteSpace channel, String playerAction, int bet) {
            try {
                channel.getAll(
                        new ActualField("Action"),
                        new FormalField(String.class),
                        new FormalField(Integer.class)
                );
                channel.put("Action", playerAction, bet);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
}