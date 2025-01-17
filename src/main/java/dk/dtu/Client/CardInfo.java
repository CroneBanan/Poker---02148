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
            case "HEARTS" -> "\u001B[38;2;200;30;0m"+"♥"+"\u001B[0m";
            case "DIAMONDS" -> "\u001B[38;2;200;30;0m"+"♦" + "\u001B[0m";
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
                    return s + " J";
                case 12:
                    return s + " Q";
                case 13:
                    return s + " K";
                case 14:
                    return s + " A";
            }
        }
        return "Invalid card info";
    }

    public String valueToString() {
        switch (value) {
            case 10:
                return "10";
            case 11:
                return "J ";
            case 12:
                return "Q ";
            case 13:
                return "K ";
            case 14:
                return "A ";
            default:
                return  value + " ";
        }
    }
}
