package dk.dtu.Client;
import org.jspace.FormalField;
import org.jspace.QueueSpace;
import org.jspace.Space;

import java.util.Scanner;

public class UserInput implements Runnable{
    private Scanner console;
    private Space instructions;
    private Space inputs;

    public UserInput(Space instructions,Space inputs) {
        console = new Scanner(System.in);
        this.instructions = instructions;
        this.inputs = inputs;
    }

    public String getInput(String prompt) {
        System.out.println(prompt);
        return console.nextLine();
    }


    @Override
    public void run() {
        try {
            while (true) {
                Object[] instruction = instructions.get(
                        new FormalField(String.class),
                        new FormalField(String.class)
                );
                String instructionId = (String) instruction[0];
                String prompt = (String) instruction[1];
                String result = getInput(prompt);
                inputs.put(instructionId, result);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
