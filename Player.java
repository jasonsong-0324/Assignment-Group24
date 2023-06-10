import java.util.*;
import java.io.Serializable;


public class Player implements Serializable{
    private String name;
    private List<Card> hand;
    private List<Card> tricks;
    private int score;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.tricks = new ArrayList<>();
    }
    
    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public List<Card> getTricks() {
        return tricks;
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public void removeCardFromHand(Card card) {
        hand.remove(card);
    }

    public void addTrick(List<Card> trickCards) {
        tricks.addAll(trickCards);
    }

    public void clearTricks() {
        tricks.clear();
    }

    public Card getPlayableCard(Card leadCard) {
        for (Card card : hand) {
            if (card.getSuit().equals(leadCard.getSuit()) || card.getRank().equals(leadCard.getRank())) {
                return card;
            }
        }
        return null;
    }

    public Card drawCardFromDeck(List<Card> deck) {
        if (!deck.isEmpty()) {
            Card card = deck.get(0);
            deck.remove(0);
            return card;
        }
        return null;
    }

    public void incrementScore() {
        tricks.add(new Card("", "")); // Add a dummy card to represent a won trick
    }

    public void setScore(int newScore) {
        score = newScore;
    }
   
}