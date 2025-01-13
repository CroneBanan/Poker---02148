package dk.dtu.Common;

import org.apache.commons.text.similarity.HammingDistance;

import java.util.*;

public class HandComparator implements IHandComparator {
    ArrayList<Card> bestHand = new ArrayList<Card>();


    // Input Card[7] Output Card[7] - Makes straight a lot easier to check
    private Card[] sortedHand(Card[] hand) {
        Arrays.sort(hand, new Comparator<Card>() {
            public int compare(Card c1, Card c2) {
                return c1.getValue() - c2.getValue();
            }
        });
        return hand;
    }


    //Card[7] -> Card[21][5]
    public Card[][] permutations(Card[] hand){
        Card[][] combinations = new Card[21][5];

        int index = 0; // Index to keep track of the current position in combinations
        for (int i = 0; i < hand.length - 4; i++) {
            for (int j = i + 1; j < hand.length - 3; j++) {
                for (int k = j + 1; k < hand.length - 2; k++) {
                    for (int l = k + 1; l < hand.length - 1; l++) {
                        for (int m = l + 1; m < hand.length; m++) {
                            combinations[index][0] = hand[i];
                            combinations[index][1] = hand[j];
                            combinations[index][2] = hand[k];
                            combinations[index][3] = hand[l];
                            combinations[index][4] = hand[m];
                            index++;
                        }
                    }

                }
            }
        }
        return combinations;
    }

    // Card[21][5] -> Card[5] - Returns the best hand of 5
    public Card[] bestHand(Card[][] combinations) {
        Card[] bestHand = combinations[0];
        HandType bestHandType = evaluateHand(combinations[0]);
        for(int i = 1; i < combinations.length; i++) {
            HandType currentHandType = evaluateHand(combinations[i]);
            if(currentHandType.compare(bestHandType) == ComparisonResult.Larger) {
                bestHand = combinations[i];
                bestHandType = currentHandType;
            }
        }
        return bestHand;
    }


    // Input Card[5]
    private ComparisonResult handCompare(Card[] combination5_1, Card[] combination5_2) {
        HandType h1 = evaluateHand(combination5_1);
        HandType h2 = evaluateHand(combination5_2);

        return h1.compare(h2);
    }

    // Card[7] * Card[7] -> ComparisonResult

    // It is hand1 versus hand2 -> larger = hand1 wins, lesser = hand 2 wins, equal = tie
    public ComparisonResult compareFinalHands(Card[] hand1, Card[] hand2) {
        Card[][] permutations1 = permutations(hand1);
        Card[][] permutations2 = permutations(hand2);

        Card[] bestHand1 = bestHand(permutations1);
        Card[] bestHand2 = bestHand(permutations2);

        return handCompare(bestHand1, bestHand2);
    }



    // Card[5] -> type * int
    public HandType evaluateHand(Card[] h) {
        Card[] hand = sortedHand(h);

        if(isRoyalFlush(hand)) {
            return new HandType(9, highCard(hand));
        } else if(isStraightFlush(hand)) {
            return new HandType(8, highCard(hand));
        } else if(isFourOfAKind(hand)) {
            return new HandType(7, highCard(hand));
        } else if(isFullHouse(hand)) {
            return new HandType(6, highCard(hand));
        } else if(isFlush(hand)) {
            return new HandType(5, highCard(hand));
        } else if(isStraight(hand)) {
            return new HandType(4, highCard(hand));
        } else if(isThreeOfAKind(hand)) {
            return new HandType(3, highCard(hand));
        } else if(isTwoPair(hand)) {
            return new HandType(2, highCard(hand));
        } else if(isPair(hand)) {
            return new HandType(1, highCard(hand));
        } else {
            return new HandType(0, highCard(hand));
        }
    }





    // Input Card[5] Output boolean - for all of the following methods
    // All arrays of cards are sorted from lowest to highest value

    private boolean isRoyalFlush(Card[] hand) {
        return isStraightFlush(hand) && hand[0].getValue() == 10;
    }


    private boolean isStraightFlush(Card[] hand) {
        return isStraight(hand) && isFlush(hand);
    }

    private boolean isFourOfAKind(Card[] hand) {
        for(int i = 0; i < hand.length; i++) {
            int count = 0;
            for(int j = 0; j < hand.length; j++) {
                if(hand[i].getValue() == hand[j].getValue()) {
                    count++;
                }
            }
            if(count == 4) {
                return true;
            }
        }
        return false;
    }

    private boolean isFullHouse(Card[] hand) {
        if(isThreeOfAKind(hand)) {
            for(int i = 0; i < hand.length; i++) {
                int count = 0;
                for(int j = 0; j < hand.length; j++) {
                    if(hand[i].getValue() == hand[j].getValue()) {
                        count++;
                    }
                }
                if(count == 2) {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean isFlush(Card[] hand) {
        for(int i = 1; i < hand.length; i++) {
            if(hand[i].getSuite() != hand[0].getSuite()) {
                return false;
            }
        }
        return true;
    }


   private boolean isStraight(Card[] hand) {
        for(int i = 0; i < hand.length-1; i++) {
            if(hand[i].getValue() != hand[i+1].getValue() - 1) {
                return false;
            }
        }
        return true;
   }

    private boolean isThreeOfAKind(Card[] hand) {
        for(int i = 0; i < hand.length; i++) {
            int count = 0;
            for(int j = 0; j < hand.length; j++) {
                if(hand[i].getValue() == hand[j].getValue()) {
                    count++;
                }
            }
            if(count == 3) {
                return true;
            }
        }
        return false;
    }

    private boolean isTwoPair(Card[] hand) {
        int pairCount = 0;
        for(int i = 0; i < hand.length; i++) {
            for(int j = i + 1; j < hand.length; j++) {
                if(hand[i].getValue() == hand[j].getValue()) {
                    pairCount++;
                }
            }
        }
        return pairCount == 2;
    }

    private boolean isPair(Card[] hand) {
        for(int i = 0; i < hand.length; i++) {
            for(int j = i + 1; j < hand.length; j++) {
                if(hand[i].getValue() == hand[j].getValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    private int highCard(Card[] hand) {
        return hand[hand.length - 1].getValue();
    }



    // FOR TEST PURPOSES
    public void evaluateHandTest(Card[] h) {
        Card[] hand = sortedHand(h);

        if(isRoyalFlush(hand)) {
            System.out.println("Royal Flush - High card:" + highCard(hand));
        } else if(isStraightFlush(hand)) {
            System.out.println("Straight Flush - High card:" + highCard(hand));
        } else if(isFourOfAKind(hand)) {
            System.out.println("Four of a Kind - High card:" + highCard(hand));
        } else if(isFullHouse(hand)) {
            System.out.println("Full House - High card:" + highCard(hand));
        } else if(isFlush(hand)) {
            System.out.println("Flush - High card:" + highCard(hand));
        } else if(isStraight(hand) ) {
            System.out.println("Straight - High card:" + highCard(hand));
        } else if(isThreeOfAKind(hand)) {
            System.out.println("Three of a Kind - High card:" + highCard(hand));
        } else if(isTwoPair(hand)) {
            System.out.println("Two Pair - High card:" + highCard(hand));
        } else if(isPair(hand)) {
            System.out.println("Pair - High card:" + highCard(hand));
        } else {
            System.out.println("High Card - High card: " + highCard(hand));
        }
    }
}
