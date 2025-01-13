package dk.dtu.Server;

import dk.dtu.Client.Client;
import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;

import java.io.IOException;

public class Server {
    public static String ip = "localhost";
    public static int port = 7324;
    public static String generalUri = "tcp://" + ip + ":" + port + "/?conn";

    public static void main(String[] args) throws IOException, InterruptedException {
        SpaceRepository repository = new SpaceRepository();

        SequentialSpace player1 = new SequentialSpace();
        repository.add("player1", player1);
        SequentialSpace player2 = new SequentialSpace();
        repository.add("player2", player2);
        SequentialSpace gameState = new SequentialSpace();
        repository.add("gameState", gameState);
        SequentialSpace turnSpace = new SequentialSpace();
        repository.add("turn", turnSpace);

        repository.addGate(generalUri);
        Poker game = new Poker("tcp://" + ip + ":" + port);
        game.run();
        gameState.put("hej");
        System.out.println("Bastian is cool");
    }


}
