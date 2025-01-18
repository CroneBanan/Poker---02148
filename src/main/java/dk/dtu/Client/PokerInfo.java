package dk.dtu.Client;

import dk.dtu.Common.Card;
import dk.dtu.Server.CircularList;
import dk.dtu.Server.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PokerInfo {
    private ArrayList<PlayerInfo> players;
    private PlayerInfo currentPlayer;
    private int pot;
    private CardInfo[] cardsInPlay;
    private int highestBet;

    public PokerInfo(ArrayList<PlayerInfo> players,
                     PlayerInfo currentPlayer,
                     int pot,
                     CardInfo[] cardsInPlay,
                     int highestBet) {
        this.players =  players;
        players.sort(Comparator.comparing(PlayerInfo::getTurnPosition));
        this.currentPlayer = currentPlayer;
        this.pot = pot;
        this.cardsInPlay = cardsInPlay;
        this.highestBet = highestBet;
    }


    public ArrayList<PlayerInfo> getPlayers() {
        return players;
    }

    public int getPot() {
        return pot;
    }

    public CardInfo[] getCardsInPlay() {return cardsInPlay;
    }

    public int getHighestBet() {
        return highestBet;
    }

    public String currentPlayerToString() {
        return currentPlayer.getName() + " (" + currentPlayer.getId() + ")";
    }

    public PlayerInfo getCurrentPlayer() {
        return currentPlayer;
    }


}
