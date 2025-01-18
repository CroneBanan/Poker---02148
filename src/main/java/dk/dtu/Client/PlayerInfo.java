package dk.dtu.Client;

import dk.dtu.Common.Card;
import org.jspace.RemoteSpace;
import org.jspace.Space;

public class PlayerInfo {
    private int id;
    private String name;
    private int cashInCents;
    private CardInfo[] hand;
    private int bet;
    private String status;
    private int turnPosition;
    private double cashInDollars;
    private boolean toMove;

    public PlayerInfo(int id, String name, int cashInCents, CardInfo[] hand, int bet, String status, int turnPosition, boolean toMove) {
        this.id = id;
        this.name = name;
        this.cashInCents = cashInCents;
        this.hand = hand;
        this.bet = bet;
        this.status = status;
        this.turnPosition = turnPosition;
        this.cashInDollars = cashInCents / 100;
        this.toMove = toMove;
    }

    public PlayerInfo(int id, String name, int cashInCents, int bet, String status, int turnPosition,boolean toMove) {
        this.id = id;
        this.name = name;
        this.cashInCents = cashInCents;
        this.bet = bet;
        this.status = status;
        this.turnPosition = turnPosition;
        this.toMove = toMove;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCashInCents() {
        return cashInCents;
    }

    public double getCashInDollars() {
        return cashInDollars;
    }

    public String getStatus() {
        return status;
    }

    public int getBet() {
        return bet;
    }

    public CardInfo[] getHand() {
        return hand;
    }

    public boolean isToMove() {
        return toMove;
    }

}
