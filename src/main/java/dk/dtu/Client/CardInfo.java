package dk.dtu.Client;

import dk.dtu.Common.Suite;

public class CardInfo {
    private int value;
    private Suite suite;

    public CardInfo(int value, Suite suite) {
        this.value = value;
        this.suite = suite;
    }

    public String suiteAsString() {
        switch (suite) {
            case HEARTS:
                return "♥";
            case DIAMONDS:
                return "♦";
            case CLUBS:
                return "♣";
            case SPADES:
                return "♠";
        }
        return "";
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
