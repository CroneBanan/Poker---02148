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

    public PlayerInfo(int id, String name, int cashInCents, CardInfo[] hand, int bet, String status) {
        this.id = id;
        this.name = name;
        this.cashInCents = cashInCents;
        this.hand = hand;
        this.bet = bet;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public int getBet() {
        return bet;
    }

    public String handToString() {
        return "|" + hand[0].toString() + "||" + hand[1].toString() + "|";
    }
}
