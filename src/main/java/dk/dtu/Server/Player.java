package dk.dtu.Server;

import dk.dtu.Common.Card;
import org.jspace.SequentialSpace;
import org.jspace.Space;

import java.io.IOException;

public class Player {
    public static int incrementer = 0;
    private int id;
    private String name;
    private int cashInCents;
    private Card[] hand;
    private String status;
    private String uriPart;
    private Space channel;
    private int betInCents;
    private Space space;

    public Player(String name,String status) {
        setName(name);
        setId();
        setStatus(status);
        setUriPart();
        this.channel = new SequentialSpace();
    }

    private void setUriPart() {
        this.uriPart = "/player" + id;
    }

    public void setSpace(String uri) throws IOException {
        this.space = channel;
    }
    public Space getSpace() {
        return this.space;
    }

    public void setCashInCents(int cents) {
        if (cents < 0) {
            throw new IllegalArgumentException("Cash must be positive, not " + cents);
        }
        this.cashInCents = cents;
    }

    public int getCashInCents() {
        return  cashInCents;
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

    public String getUriPart() {
        return uriPart;
    }

    public void addToBet(int bet) {
        if (enoughCash(bet)) {
            this.cashInCents -= bet;
            this.betInCents += bet;
            if (cashInCents == 0) {
                setStatus("All in");
            }
        }
    }

    public boolean enoughCash(int bet) {
        return cashInCents >= bet;
    }

    public int getBetInCents() {
        return betInCents;
    }

    public void setBetInCents(int bet) {
        this.betInCents = bet;
    }

    private void setId() {
        this.id = incrementer;
        incrementer++;
    }


    public void addCashInCents(int winningsInCents) {
        this.cashInCents += winningsInCents;
    }


    public Space getChannel() {
        return channel;
    }

    public void setChannel(Space channel) {
        this.channel = channel;
    }

    public boolean equals(Player p) {
        return this.id == p.getId();
    }
}
