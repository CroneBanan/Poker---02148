package dk.dtu.Client;

import dk.dtu.Common.Suite;

public class CardInfo {
    private int value;
    private String suite;

    public CardInfo(int value, String suite) {
        this.value = value;
        this.suite = suite;
    }

    public String suiteAsString() {
        return switch (suite) {
            case "HEARTS" -> "♥";
            case "DIAMONDS" -> "♦";
            case "CLUBS" -> "♣";
            case "SPADES" -> "♠";
            default -> "";
        };
    }

    public String toString() {
        String s = suiteAsString();
        if (value <= 10) {
            return s + " " + value;
        } else {
            switch (value) {
                case 11:
                    return s + " Jack";
                case 12:
                    return s + " Queen";
                case 13:
                    return s + " King";
                case 14:
                    return s + " Ace";
            }
        }
        return "Invalid card info";
    }
}
