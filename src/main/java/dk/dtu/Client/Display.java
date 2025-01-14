package dk.dtu.Client;


import java.util.ArrayList;

public class Display {
    private PokerInfo gameState;
    private ArrayList<PlayerInfo> players;

    private CardInfo[] cardsInPlay;

    public void display(PokerInfo gameState) {
        this.gameState = gameState;
        this.players = this.gameState.getPlayers();
        this.cardsInPlay = gameState.getCardsInPlay();
    }

    public void displayPlayerInfo() {
        for(PlayerInfo player: players) {
            System.out.println(player.getName() + ":" + player.getStatus());
            System.out.println("Cash:"+player.getCashInCents());
            System.out.println("Current Bet" + player.getBet());
        }
    }

    public void displayCards() {
        for(CardInfo card: cardsInPlay) {
            if(card != null) {
                System.out.println(card.toString());
            }
        }
    }

    public void displayPot() {
        System.out.println(gameState.getPot());
    }

    public void displayPlayerCard() {
        for(PlayerInfo player: players) {
            if(player.getHand() != null) {
                System.out.println(player.handToString());
            }
        }
    }

}
