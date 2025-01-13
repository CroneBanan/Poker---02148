package dk.dtu.Client;

import dk.dtu.Common.Card;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import java.io.IOException;

import org.jspace.ActualField;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        RemoteSpace space;
        RemoteSpace player;
        String ip = "localhost";
        int port = 7324;
        String uri = "tcp://" + ip + ":" + port;
        String player1 = "player1";

        space = new RemoteSpace(uri + "/gameState?conn");
        player = new RemoteSpace(uri + "/" + player1 + "?conn");;
        String test = (String) space.get(new ActualField("hej"))[0];

        System.out.println(test);

        Object[] cards = player.get(new ActualField("Cards"), new FormalField(Card.class));
        System.out.println(cards[0].toString());
        System.out.println(cards[1].toString());
    }
}
