package dk.dtu.Client;


import java.util.ArrayList;

public class Display {
    private PokerInfo gameState;
    private ArrayList<PlayerInfo> players;
    private CardInfo[] cardsInPlay;

    public Display(PokerInfo gameState) {
        this.gameState = gameState;
        this.players = this.gameState.getPlayers();
        this.cardsInPlay = gameState.getCardsInPlay();
    }

    public void show() {
        displayPlayerInfo();
        displayPot();
        displayCards();
        displayPlayerCard();
    }

    public void displayPlayerInfo() {
        for(PlayerInfo player: players) {
            System.out.println("\n" + player.getName() + ":" + player.getStatus());
            System.out.println("Cash:"+player.getCashInCents());
            System.out.println("Current Bet:" + player.getBet());
        }
    }

    public void displayCards() {
        cardStackToString(cardsInPlay);
    }

    public void displayPot() {
        System.out.println("Pot: " + gameState.getPot());
    }

    public void displayPlayerCard() {
        for(PlayerInfo player: players) {
            if(player.getHand() != null) {
                cardStackToString(player.getHand());
                System.out.println("\n" + player.getName() + ":" + player.getStatus());
                System.out.println("Cash:"+player.getCashInCents());
                System.out.println("Current Bet:" + player.getBet());
            }
        }

    }

    public void cardStackToString(CardInfo[] stack) {
        for (CardInfo c : stack) {
            if (c != null) {
                System.out.print("|" + c + "|");
            }
        }
        System.out.println();
    }
}
