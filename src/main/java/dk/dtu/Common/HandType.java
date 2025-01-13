package dk.dtu.Common;


// Used for determining best hand of 5
public class HandType {
    private int highCard;
    private int type;

    public HandType(int type, int highCard) {
        this.type = type;
        this.highCard = highCard;
    }

    public int getHighCard() {
        return highCard;
    }

    public int getType() {
        return type;
    }

    public ComparisonResult compare(HandType handType)  {
        if(this.getType() > handType.getType()) {
            return ComparisonResult.Larger;
        } else if(handType.getType() > this.getType()) {
            return ComparisonResult.Smaller;
        } else if(this.getHighCard() > handType.getHighCard()){
            return ComparisonResult.Larger;
        } else if(handType.getHighCard() > this.getHighCard()){
            return ComparisonResult.Smaller;
        } else {
            return ComparisonResult.Equal;
        }
    }
}
