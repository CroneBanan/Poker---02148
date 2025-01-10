package dk.dtu.Server;

import org.jspace.SequentialSpace;

public class Player {
    public static int incrementer = 0;
    private int id;
    private String name;
    private int cashInCents;
    private Card[] hand;
    private String status;
    private String uriPart;
    private SequentialSpace channel;

    public Player(String name, int cash) {
        setCash(cash);
        setId();
        setName(name);
        setStatus("playing");
        setUriPart();
        this.channel = new SequentialSpace();
    }

    public Player(String name,String status) {
        setName(name);
        setId();
        setStatus(status);
        setUriPart();
        this.channel = new SequentialSpace();
    }


    public void setCash(int dollars) {
        this.cashInCents = dollars * 100;
    }

    public int getCash() {
        return  cashInCents / 100;
    }

    public Card[] getHand() {
        return hand;
    }

    public void setHand(Card[] hand) {
        this.hand = hand;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    private void setId() {
        this.id = incrementer;
        incrementer++;
    }

    private void setUriPart() {
        this.uriPart = "/player" + this.id;
    }

    public String getUriPart() {
        return uriPart;
    }

    public SequentialSpace getChannel() {
        return channel;
    }
}
