package dk.dtu.Common;

public class Tester {

    public static void main(String[] args) {
        HandComparator hc = new HandComparator();

        //Test cases

        //Royal flush
        Card card1 = new Card(10, Suite.HEARTS);
        Card card2 = new Card(11, Suite.HEARTS);
        Card card3 = new Card(12, Suite.HEARTS);
        Card card4 = new Card(13, Suite.HEARTS);
        Card card5 = new Card(14, Suite.HEARTS);
        Card card6 = new Card(2, Suite.HEARTS);
        Card card7 = new Card(3, Suite.SPADES);

        Card[] handRoyal = {card1, card2, card3, card4, card5, card6, card7};

        System.out.println("Should be royal flush - High card should be 14");
        hc.evaluateHandTest(hc.bestHand(hc.permutations(handRoyal)));
        System.out.println();

        //Straight flush
        card1 = new Card(2, Suite.HEARTS);
        card2 = new Card(3, Suite.HEARTS);
        card3 = new Card(4, Suite.HEARTS);
        card4 = new Card(5, Suite.HEARTS);
        card5 = new Card(6, Suite.HEARTS);
        card6 = new Card(10, Suite.HEARTS);
        card7 = new Card(11, Suite.SPADES);

        Card[] handStraightFlush = {card1, card2, card3, card4, card5, card6, card7};

        System.out.println("Should be straight flush - High card should be 6");
        hc.evaluateHandTest(hc.bestHand(hc.permutations(handStraightFlush)));
        System.out.println();


        //Four of a kind
        card1 = new Card(3, Suite.HEARTS);
        card2 = new Card(3, Suite.SPADES);
        card3 = new Card(3, Suite.DIAMONDS);
        card4 = new Card(3, Suite.CLUBS);
        card5 = new Card(9, Suite.SPADES);
        card6 = new Card(6, Suite.HEARTS);
        card7 = new Card(7, Suite.SPADES);

        Card[] handFourOfAKind = {card1, card2, card3, card4, card5, card6, card7};

        System.out.println("Should be four of a kind - High card should be 9");
        hc.evaluateHandTest(hc.bestHand(hc.permutations(handFourOfAKind)));
        System.out.println();

        //Full House
        card1 = new Card(3, Suite.HEARTS);
        card2 = new Card(3, Suite.SPADES);
        card3 = new Card(3, Suite.DIAMONDS);
        card4 = new Card(4, Suite.CLUBS);
        card5 = new Card(4, Suite.SPADES);
        card6 = new Card(6, Suite.HEARTS);
        card7 = new Card(6, Suite.SPADES);

        Card[] handFullHouse = {card1, card2, card3, card4, card5, card6, card7};

        System.out.println("Should be full house - High card should be 6");
        hc.evaluateHandTest(hc.bestHand(hc.permutations(handFullHouse)));
        System.out.println();

        //Flush
        card1 = new Card(3, Suite.HEARTS);
        card2 = new Card(7, Suite.HEARTS);
        card3 = new Card(8, Suite.HEARTS);
        card4 = new Card(4, Suite.HEARTS);
        card5 = new Card(9, Suite.SPADES);
        card6 = new Card(6, Suite.HEARTS);
        card7 = new Card(7, Suite.SPADES);

        Card[] handFlush = {card1, card2, card3, card4, card5, card6, card7};

        System.out.println("Should be flush - High card should be 8");
        hc.evaluateHandTest(hc.bestHand(hc.permutations(handFlush)));
        System.out.println();

        //Straight
        card1 = new Card(2, Suite.HEARTS);
        card2 = new Card(3, Suite.SPADES);
        card3 = new Card(4, Suite.DIAMONDS);
        card4 = new Card(5, Suite.CLUBS);
        card5 = new Card(9, Suite.SPADES);
        card6 = new Card(6, Suite.HEARTS);
        card7 = new Card(7, Suite.SPADES);

        Card[] handStraight = {card1, card2, card3, card4, card5, card6, card7};

        System.out.println("Should be straight - High card should be 7");
        hc.evaluateHandTest(hc.bestHand(hc.permutations(handStraight)));
        System.out.println();


        //Three of a kind
        card1 = new Card(3, Suite.HEARTS);
        card2 = new Card(3, Suite.SPADES);
        card3 = new Card(3, Suite.DIAMONDS);
        card4 = new Card(4, Suite.CLUBS);
        card5 = new Card(9, Suite.SPADES);
        card6 = new Card(6, Suite.HEARTS);
        card7 = new Card(7, Suite.SPADES);

        Card[] handThreeOfAKind = {card1, card2, card3, card4, card5, card6, card7};

        System.out.println("Should be three of a kind - High card should be 9");
        hc.evaluateHandTest(hc.bestHand(hc.permutations(handThreeOfAKind)));
        System.out.println();

        //Two pair
        card1 = new Card(1, Suite.HEARTS);
        card2 = new Card(11, Suite.SPADES);
        card3 = new Card(3, Suite.DIAMONDS);
        card4 = new Card(3, Suite.CLUBS);
        card5 = new Card(9, Suite.SPADES);
        card6 = new Card(6, Suite.HEARTS);
        card7 = new Card(6, Suite.SPADES);

        Card[] handTwoPair = {card1, card2, card3, card4, card5, card6, card7};

        System.out.println("Should be two pairs - High card should be 11");
        hc.evaluateHandTest(hc.bestHand(hc.permutations(handTwoPair)));
        System.out.println();


        //Pair
        card1 = new Card(1, Suite.HEARTS);
        card2 = new Card(11, Suite.SPADES);
        card3 = new Card(3, Suite.DIAMONDS);
        card4 = new Card(3, Suite.CLUBS);
        card5 = new Card(9, Suite.SPADES);
        card6 = new Card(6, Suite.HEARTS);
        card7 = new Card(7, Suite.SPADES);

        Card[] handPair = {card1, card2, card3, card4, card5, card6, card7};

        System.out.println("Should be pair - High card should be 11");
        hc.evaluateHandTest(hc.bestHand(hc.permutations(handPair)));
        System.out.println();

        //High card
        card1 = new Card(1, Suite.HEARTS);
        card2 = new Card(11, Suite.SPADES);
        card3 = new Card(3, Suite.DIAMONDS);
        card4 = new Card(4, Suite.CLUBS);
        card5 = new Card(9, Suite.SPADES);
        card6 = new Card(6, Suite.HEARTS);
        card7 = new Card(7, Suite.SPADES);

        Card[] handHighCard = {card1, card2, card3, card4, card5, card6, card7};

        System.out.println("Should be high card - High card should be 11");
        hc.evaluateHandTest(hc.bestHand(hc.permutations(handHighCard)));
        System.out.println();

        // Find a winner
        ComparisonResult cr = hc.compareFinalHands(handRoyal, handFlush);
        System.out.println(cr);


    }

}