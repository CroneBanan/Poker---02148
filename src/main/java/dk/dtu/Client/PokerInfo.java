package dk.dtu.Client;

import dk.dtu.Common.Card;
import dk.dtu.Server.CircularList;
import dk.dtu.Server.Player;

import java.util.ArrayList;

public class PokerInfo {
    private ArrayList<PlayerInfo> players;
    private ArrayList<PlayerInfo> blindOrder;
    private int pot;
    private CardInfo[] cardsInPlay;
    private int highestBet;

    public PokerInfo(ArrayList<PlayerInfo> players,
                     ArrayList<PlayerInfo> blindOrder,
                     int pot,
                     CardInfo[] cardsInPlay,
                     int highestBet) {
        this.players = players;
        this.blindOrder = blindOrder;
        this.pot = pot;
        this.cardsInPlay = cardsInPlay;
        this.highestBet = highestBet;
    }


    public ArrayList<PlayerInfo> getPlayers() {
        return players;
    }

    public ArrayList<PlayerInfo> getBlindOrder() {
        return blindOrder;
    }

    public int getPot() {
        return pot;
    }

    public CardInfo[] getCardsInPlay() {
        return cardsInPlay;
    }

    public int getHighestBet() {
        return highestBet;
    }

    public String blindsToString() {
        return "Big Blind: " + blindOrder.get(0).getName() + " (" + blindOrder.get(0).getId() + ")\n"
                + "Small Blind: " + blindOrder.get(0).getName() + " (" + blindOrder.get(0).getId() + ")";
    }

    public String getCurrentPlayer() {
        return "AD";
    }
}
