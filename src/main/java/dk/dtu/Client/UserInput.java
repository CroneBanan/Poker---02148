package dk.dtu.Client;
import org.jspace.*;

import java.util.Scanner;

public class UserInput {
    private QueueSpace instructions;
    private SequentialSpace currentWork;
    private SequentialSpace inputs;
    private Thread listener;

    public UserInput(QueueSpace instructions,SequentialSpace inputs, SequentialSpace currentWork) {
        this.instructions = instructions == null ? new QueueSpace() : instructions;
        this.inputs = inputs == null ? new SequentialSpace() : inputs;;
        this.currentWork = currentWork == null ? new SequentialSpace() : currentWork;;
        listener = null;
    }

    public UserInput() {
        this(null,null,null);
    }

    public void start() {
        if (listener == null) {
            listener = new Thread(new Listener(instructions,inputs,currentWork));
            listener.start();
        }
    }

    public boolean isInputReady(String id) {
        return !inputs.queryAll(new ActualField(id), new FormalField(String.class)).isEmpty();
    }

    public void stop() {
        listener.interrupt();
        currentWork.getAll(
            new FormalField(String.class),
            new FormalField(String.class)
        );
        listener = null;
    }

    public void restart() {
        stop();
        start();
    }

    /**
     * Adds prompt to queue if id is unique
     * @param id
     * @param prompt
     * @return succes. ie was it added
     * @throws InterruptedException
     */
    public boolean tryQueuePrompt(String id,String prompt) throws InterruptedException {
        boolean succes = false;
        if (idDoesNotExist(id)) {
            instructions.put(id,prompt);
            succes = true;
        }
        return succes;
    }

    public boolean idDoesNotExist(String id) {
        return (
                instructions.queryAll(new ActualField(id), new FormalField(String.class)).isEmpty() &&
                inputs.queryAll(new ActualField(id), new FormalField(String.class)).isEmpty() &&
                currentWork.queryAll(new ActualField(id), new FormalField(String.class)).isEmpty()
        );
    }

    public void queuePrompt(String id,String prompt) throws InterruptedException {
        boolean succes = tryQueuePrompt(id,prompt);
        if (!succes) {
            throw new IllegalArgumentException("id already exists: " + id);
        }
    }

    public String getInput(String id) throws InterruptedException {
        return (String) inputs.get(new ActualField(id), new FormalField(String.class))[1];
    }



    private class Listener implements Runnable{
        private Scanner console;
        private QueueSpace instructions;
        private SequentialSpace currentWork;
        private SequentialSpace inputs;
        public Listener(QueueSpace instructions,SequentialSpace inputs, SequentialSpace currentWork) {
            console = new Scanner(System.in);
            this.instructions = instructions;
            this.inputs = inputs;
            this.currentWork = currentWork;
        }

        private Object[] getAnInstruction(Space space) throws InterruptedException {
            return space.get(
                    new FormalField(String.class),
                    new FormalField(String.class)
            );
        }

        private void doAnInstruction() throws InterruptedException {
            //get instruction
            Object[] instruction = getAnInstruction(instructions);
            String instructionId = (String) instruction[0];
            String prompt = (String) instruction[1];
            currentWork.put(instructionId,prompt);

            //do work
            System.out.println(prompt);
            String result = null;
            while (!console.hasNext()) {
                Thread.sleep(20);
            }
            result = console.nextLine();

            //end work and give result
            inputs.put(instructionId, result);
            getAnInstruction(currentWork);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    doAnInstruction();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
