package dk.dtu.Common;

public class Card {
    private int value;
    private Suite suite;

    public Card(int value, Suite suite) {
        this.value = value;
        this.suite = suite;
    }

    public int getValue() {
        return value;
    }

    public Suite getSuite() {
        return suite;
    }

    public String toString() {
        return value + " of " + suite;
    }

}
