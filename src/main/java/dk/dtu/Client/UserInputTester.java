package dk.dtu.Client;

import org.jspace.FormalField;
import org.jspace.QueueSpace;
import org.jspace.SequentialSpace;
import org.jspace.Space;

public class UserInputTester {
    public static void main(String[] args) throws InterruptedException {
        Space instructions = new SequentialSpace();
        Space inputs = new SequentialSpace();
        instructions.put("getName","what is your name?");
        instructions.put("LobbyAction","Type Ready to ready og Disc to disconnect");
        //new UserInput(instructions,inputs).run();
        System.out.println(1);
        for (Object[] tuple : inputs.queryAll(new FormalField(String.class),new FormalField(String.class))) {
            System.out.println(2);
            String id =(String) tuple[0];
            String input =(String) tuple[1];
            System.out.println(id);
            System.out.println(input + "\n");
        }
        System.out.println(3);
    }
}
