package dk.dtu.Client;

import dk.dtu.Common.Card;
import dk.dtu.Common.Suite;
import dk.dtu.Server.Poker;

//TODO rund op til 2 decimaler på alle pengebeløb

public class Screen {

    public Screen() {

    }


    private void gameNoCards(PokerInfo pokerInfo) {
        String x = "\u001B[38;2;200;30;0m" + "██" + "\u001B[0m";
        String y = "\u001B[38;2;32;28;36m" + "██" + "\u001B[0m";

        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
        System.out.println(x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y);
        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");

        for (int i = 0; i < pokerInfo.getPlayers().size(); i++) {
            System.out.println(playerString(pokerInfo.getPlayers().get(i)));
        }

        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
        System.out.println("Table cards:" + "\t Current Pot: " + (double) pokerInfo.getPot() / 100 + "\t Highest bet: " + (double) pokerInfo.getHighestBet() / 100);

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();


        System.out.println("Your cards:");
        System.out.println("    ┌──────┐   ┌──────┐");
        System.out.println("    │ " + pokerInfo.getCurrentPlayer().getHand()[0].suiteAsString() + "    │   │ " + pokerInfo.getCurrentPlayer().getHand()[1].suiteAsString() + "    │");
        System.out.println("\u001B[38;2;196;162;86m"+"════"+"\u001B[0m"+"│  "+pokerInfo.getCurrentPlayer().getHand()[0].valueToString()+"  │"+"\u001B[38;2;196;162;86m"+"═══"+"\u001B[0m"+"│  "+pokerInfo.getCurrentPlayer().getHand()[1].valueToString()+"  │"+"\u001B[38;2;196;162;86m"+"═══════════════════════════════════════════════════════════"+"\u001B[0m");
        System.out.println("    │    " + pokerInfo.getCurrentPlayer().getHand()[0].suiteAsString() + " │   │    " + pokerInfo.getCurrentPlayer().getHand()[1].suiteAsString() + " │\t   Cash: " + (double) pokerInfo.getCurrentPlayer().getCashInCents() / 100);
        System.out.println("    └──────┘   └──────┘\t   Bet: " + (double) pokerInfo.getCurrentPlayer().getBet() / 100);

        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
        System.out.println(x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y);
        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
    }


    private void gameThreeCards(PokerInfo pokerInfo) {
        String x = "\u001B[38;2;200;30;0m" + "██" + "\u001B[0m";
        String y = "\u001B[38;2;32;28;36m" + "██" + "\u001B[0m";

        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
        System.out.println(x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y);
        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");

        for (int i = 0; i < pokerInfo.getPlayers().size(); i++) {
            System.out.println(playerString(pokerInfo.getPlayers().get(i)));
        }

        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
        System.out.println("Table cards:" + "\t Current Pot: " + (double) pokerInfo.getPot() / 100 + "\t Highest bet: " + (double) pokerInfo.getHighestBet() / 100);

        System.out.println("\t\t┌──────┐\t┌──────┐\t┌──────┐");
        System.out.println("\t\t│ " + pokerInfo.getCardsInPlay()[0].suiteAsString() + "    │\t│ " + pokerInfo.getCardsInPlay()[1].suiteAsString() + "    │\t│ " + pokerInfo.getCardsInPlay()[2].suiteAsString() + "    │");
        System.out.println("\t\t│  " + pokerInfo.getCardsInPlay()[0].valueToString() + "  │\t│  " + pokerInfo.getCardsInPlay()[1].valueToString() + "  │\t│  " + pokerInfo.getCardsInPlay()[2].valueToString() + "  │");
        System.out.println("\t\t│    " + pokerInfo.getCardsInPlay()[0].suiteAsString() + " │\t│    " + pokerInfo.getCardsInPlay()[1].suiteAsString() + " │\t│    " + pokerInfo.getCardsInPlay()[2].suiteAsString() + " │");
        System.out.println("\t\t└──────┘\t└──────┘\t└──────┘");

        System.out.println("Your cards:");
        System.out.println("    ┌──────┐   ┌──────┐");
        System.out.println("    │ " + pokerInfo.getCurrentPlayer().getHand()[0].suiteAsString() + "    │   │ " + pokerInfo.getCurrentPlayer().getHand()[1].suiteAsString() + "    │");
        System.out.println("\u001B[38;2;196;162;86m"+"════"+"\u001B[0m"+"│  "+pokerInfo.getCurrentPlayer().getHand()[0].valueToString()+"  │"+"\u001B[38;2;196;162;86m"+"═══"+"\u001B[0m"+"│  "+pokerInfo.getCurrentPlayer().getHand()[1].valueToString()+"  │"+"\u001B[38;2;196;162;86m"+"═══════════════════════════════════════════════════════════"+"\u001B[0m");
        System.out.println("    │    " + pokerInfo.getCurrentPlayer().getHand()[0].suiteAsString() + " │   │    " + pokerInfo.getCurrentPlayer().getHand()[1].suiteAsString() + " │\t   Cash: " + (double) pokerInfo.getCurrentPlayer().getCashInCents() / 100);
        System.out.println("    └──────┘   └──────┘\t   Bet: " + (double) pokerInfo.getCurrentPlayer().getBet() / 100);

        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
        System.out.println(x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y);
        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
    }



    private void gameFourCards(PokerInfo pokerInfo) {
        String x = "\u001B[38;2;200;30;0m" + "██" + "\u001B[0m";
        String y = "\u001B[38;2;32;28;36m" + "██" + "\u001B[0m";

        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
        System.out.println(x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y);
        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");

        for (int i = 0; i < pokerInfo.getPlayers().size(); i++) {
            System.out.println(playerString(pokerInfo.getPlayers().get(i)));
        }

        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
        System.out.println("Table cards:" + "\t Current Pot: " + (double) pokerInfo.getPot() / 100 + "\t Highest bet: " + (double) pokerInfo.getHighestBet() / 100);

        System.out.println("\t\t┌──────┐\t┌──────┐\t┌──────┐\t┌──────┐");
        System.out.println("\t\t│ " + pokerInfo.getCardsInPlay()[0].suiteAsString() + "    │\t│ " + pokerInfo.getCardsInPlay()[1].suiteAsString() + "    │\t│ " + pokerInfo.getCardsInPlay()[2].suiteAsString() + "    │\t│ " + pokerInfo.getCardsInPlay()[3].suiteAsString() + "    │");
        System.out.println("\t\t│  " + pokerInfo.getCardsInPlay()[0].valueToString() + "  │\t│  " + pokerInfo.getCardsInPlay()[1].valueToString() + "  │\t│  " + pokerInfo.getCardsInPlay()[2].valueToString() + "  │\t│  " + pokerInfo.getCardsInPlay()[3].valueToString() + "  │");
        System.out.println("\t\t│    " + pokerInfo.getCardsInPlay()[0].suiteAsString() + " │\t│    " + pokerInfo.getCardsInPlay()[1].suiteAsString() + " │\t│    " + pokerInfo.getCardsInPlay()[2].suiteAsString() + " │\t│    " + pokerInfo.getCardsInPlay()[3].suiteAsString() + " │");
        System.out.println("\t\t└──────┘\t└──────┘\t└──────┘\t└──────┘");

        System.out.println("Your cards:");
        System.out.println("    ┌──────┐   ┌──────┐");
        System.out.println("    │ " + pokerInfo.getCurrentPlayer().getHand()[0].suiteAsString() + "    │   │ " + pokerInfo.getCurrentPlayer().getHand()[1].suiteAsString() + "    │");
        System.out.println("\u001B[38;2;196;162;86m"+"════"+"\u001B[0m"+"│  "+pokerInfo.getCurrentPlayer().getHand()[0].valueToString()+"  │"+"\u001B[38;2;196;162;86m"+"═══"+"\u001B[0m"+"│  "+pokerInfo.getCurrentPlayer().getHand()[1].valueToString()+"  │"+"\u001B[38;2;196;162;86m"+"═══════════════════════════════════════════════════════════"+"\u001B[0m");
        System.out.println("    │    " + pokerInfo.getCurrentPlayer().getHand()[0].suiteAsString() + " │   │    " + pokerInfo.getCurrentPlayer().getHand()[1].suiteAsString() + " │\t   Cash: " + (double) pokerInfo.getCurrentPlayer().getCashInCents() / 100);
        System.out.println("    └──────┘   └──────┘\t   Bet: " + (double) pokerInfo.getCurrentPlayer().getBet() / 100);

        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
        System.out.println(x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y);
        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
    }


    private void gameAllCards(PokerInfo pokerInfo) {
        String x = "\u001B[38;2;200;30;0m" + "██" + "\u001B[0m";
        String y = "\u001B[38;2;32;28;36m" + "██" + "\u001B[0m";

        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
        System.out.println(x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y);
        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");

        for (int i = 0; i < pokerInfo.getPlayers().size(); i++) {
            System.out.println(playerString(pokerInfo.getPlayers().get(i)));
        }

        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
        System.out.println("Table cards:" + "\t Current Pot: " + (double) pokerInfo.getPot() / 100 + "\t Highest bet: " + (double) pokerInfo.getHighestBet() / 100);

        System.out.println("\t\t┌──────┐\t┌──────┐\t┌──────┐\t┌──────┐\t┌──────┐");
        System.out.println("\t\t│ " + pokerInfo.getCardsInPlay()[0].suiteAsString() + "    │\t│ " + pokerInfo.getCardsInPlay()[1].suiteAsString() + "    │\t│ " + pokerInfo.getCardsInPlay()[2].suiteAsString() + "    │\t│ " + pokerInfo.getCardsInPlay()[3].suiteAsString() + "    │\t│ " + pokerInfo.getCardsInPlay()[4].suiteAsString() + "    │");
        System.out.println("\t\t│  " + pokerInfo.getCardsInPlay()[0].valueToString() + "  │\t│  " + pokerInfo.getCardsInPlay()[1].valueToString() + "  │\t│  " + pokerInfo.getCardsInPlay()[2].valueToString() + "  │\t│  " + pokerInfo.getCardsInPlay()[3].valueToString() + "  │\t│  " + pokerInfo.getCardsInPlay()[4].valueToString() + "  │");
        System.out.println("\t\t│    " + pokerInfo.getCardsInPlay()[0].suiteAsString() + " │\t│    " + pokerInfo.getCardsInPlay()[1].suiteAsString() + " │\t│    " + pokerInfo.getCardsInPlay()[2].suiteAsString() + " │\t│    " + pokerInfo.getCardsInPlay()[3].suiteAsString() + " │\t│    " + pokerInfo.getCardsInPlay()[4].suiteAsString() + " │");
        System.out.println("\t\t└──────┘\t└──────┘\t└──────┘\t└──────┘\t└──────┘");

        System.out.println("Your cards:");
        System.out.println("    ┌──────┐   ┌──────┐");
        System.out.println("    │ " + pokerInfo.getCurrentPlayer().getHand()[0].suiteAsString() + "    │   │ " + pokerInfo.getCurrentPlayer().getHand()[1].suiteAsString() + "    │");
        System.out.println("\u001B[38;2;196;162;86m"+"════"+"\u001B[0m"+"│  "+pokerInfo.getCurrentPlayer().getHand()[0].valueToString()+"  │"+"\u001B[38;2;196;162;86m"+"═══"+"\u001B[0m"+"│  "+pokerInfo.getCurrentPlayer().getHand()[1].valueToString()+"  │"+"\u001B[38;2;196;162;86m"+"═══════════════════════════════════════════════════════════"+"\u001B[0m");
        System.out.println("    │    " + pokerInfo.getCurrentPlayer().getHand()[0].suiteAsString() + " │   │    " + pokerInfo.getCurrentPlayer().getHand()[1].suiteAsString() + " │\t   Cash: " + (double) pokerInfo.getCurrentPlayer().getCashInCents() / 100);
        System.out.println("    └──────┘   └──────┘\t   Bet: " + (double) pokerInfo.getCurrentPlayer().getBet() / 100);

        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
        System.out.println(x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y);
        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
    }


    public void welcomeScreen() {
        clearScreen();
        String x = "\u001B[38;2;200;30;0m" + "██" + "\u001B[0m";
        String y = "\u001B[38;2;32;28;36m" + "██" + "\u001B[0m";


        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
        System.out.println(x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y);
        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
        System.out.println(" █████   ███   █████       ████                                            ");
        System.out.println("░░███   ░███  ░░███       ░░███                                            ");
        System.out.println(" ░███   ░███   ░███ ██████ ░███   ██████   ██████  █████████████    ██████ ");
        System.out.println(" ░███   ░███   ░██████░░███░███  ███░░███ ███░░███░░███░░███░░███  ███░░███");
        System.out.println(" ░░███  █████  ███░███████ ░███ ░███ ░░░ ░███ ░███ ░███ ░███ ░███ ░███████ ");
        System.out.println("  ░░░█████░█████░ ░███░░░  ░███ ░███  ███░███ ░███ ░███ ░███ ░███ ░███░░░  ");
        System.out.println("    ░░███ ░░███   ░░██████ █████░░██████ ░░██████  █████░███ █████░░██████ ");
        System.out.println("     ░░░   ░░░     ░░░░░░ ░░░░░  ░░░░░░   ░░░░░░  ░░░░░ ░░░ ░░░░░  ░░░░░░  ");
        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
        System.out.println(x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y+x+y);
        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
    }



    public void lobbyScreen() {
        clearScreen();
        String x = "\u001B[38;2;200;30;0m" + "██" + "\u001B[0m";
        String y = "\u001B[38;2;32;28;36m" + "██" + "\u001B[0m";

        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
        System.out.println(x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y);
        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
        System.out.println("\t\t\t\t\t\t\t         ████         \n" +
                "\t\t\t\t\t\t\t       ████████       \n" +
                "\t\t\t\t\t\t\t       ████████       \n" +
                "\t\t\t\t\t\t\t        ██████        \n" +
                "\t\t\t\t\t\t\t    ██████████████    \n" +
                "\t\t\t\t\t\t\t   ████████████████   \n" +
                "\t\t\t\t\t\t\t   ████████████████   \n" +
                "\t\t\t\t\t\t\t     ████ ██ ████     \n" +
                "\t\t\t\t\t\t\t         ████         \n" +
                "\t\t\t\t\t\t\t        ██████        ");
        System.out.println("Weclome to Poker!");
        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
        System.out.println(x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y + x + y);
        System.out.println("\u001B[38;2;196;162;86m" + "══════════════════════════════════════════════════════════════════════════════════" + "\u001B[0m");
    }


    public void clearScreen() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }


    public void show(PokerInfo pokerInfo){
        int cardsInPlay = 0;

        for (int i = 0; i < pokerInfo.getCardsInPlay().length; i++) {
            if(pokerInfo.getCardsInPlay()[i] != null){
                cardsInPlay++;
            }
        }
        clearScreen();
        switch(cardsInPlay){
            case 0:
                gameNoCards(pokerInfo);
                break;
            case 3:
                gameThreeCards(pokerInfo);
                break;
            case 4:
                gameFourCards(pokerInfo);
                break;
            case 5:
                gameAllCards(pokerInfo);
                break;
        }
    }

    public String playerString(PlayerInfo playerInfo){
        return  "Player: " + playerInfo.getName() + "\tCash: " + (double) playerInfo.getCashInCents() / 100 + "\t\tBet: " + (double) playerInfo.getBet() / 100 + "\tStatus: " + playerInfo.getStatus() + (playerInfo.isToMove() ? "   TO MOVE" : "");
    }

    public void showWinner(PlayerInfo playerInfo){
        clearScreen();
        System.out.println("The winner is: " + playerInfo.getName());
    }



}

