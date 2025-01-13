package dk.dtu.Server;

import dk.dtu.Common.Card;
import org.jspace.RemoteSpace;

import java.io.IOException;

public class Player {
    public static int incrementer = 0;
    private int id;
    private String name;
    private int cashInCents;
    private Card[] hand;
    private String status;
    private String uriPart;
    private int bet;
    private RemoteSpace space;

    public Player(String name, int cash, String uriPart) {
        setCash(cash);
        setId();
        setName(name);
        setStatus("playing");
        this.uriPart = uriPart;
    }

    public void setSpace(String uri) throws IOException {
        this.space = new RemoteSpace(uri);
    }
    public RemoteSpace getSpace() {
        return this.space;
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

    public String getUriPart() {
        return uriPart;
    }

    public void makeBet(int bet) {
        if (enoughCash(bet)) {
            this.cashInCents -= bet;
            this.bet += bet;
            isAllIn();
        }
    }

    public boolean enoughCash(int bet) {
        return cashInCents >= bet;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public void fold() {
        setStatus("Fold");
    }

    private void setId() {
        this.id = incrementer;
        incrementer++;
    }

    public void isAllIn() {
        if (bet == cashInCents) {
            setStatus("All in");
        }
    }
}
