import java.util.*;

public class GoBoomGame {
    private static List<Player> players;
    private static List<Card> deck;
    private static List<Card> center;
    private static int currentPlayerIndex;
    private static int trickNumber;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Go Boom Game!");
        System.out.println("Enter 's' to start a new game or 'x' to exit.");

        String input = scanner.nextLine();

        while (!input.equals("x")) {
            if (input.equals("s")) {
                startNewGame();
            }

            System.out.println("Enter 's' to start a new game or 'x' to exit.");
            input = scanner.nextLine();
        }

        System.out.println("Thanks for playing Go Boom Game!");

        scanner.close();
    }

    private static void startNewGame() {
        initializeGame();
        dealCards();

        trickNumber = 1; // Update the existing trickNumber variable

        while (!isGameOver()) {
            System.out.println("Trick #" + trickNumber);
            System.out.println("--------------");

            playTrick();

            trickNumber++;

            System.out.println();
            if (isGameOver()) {
                break; // Exit the loop if the game is over
            }
        }

        displayScores();
    }

    private static void initializeGame() {
        players = new ArrayList<>();
        players.add(new Player("Player1"));
        players.add(new Player("Player2"));
        players.add(new Player("Player3"));
        players.add(new Player("Player4"));

        deck = createDeck();
        shuffleDeck(deck);

        center = new ArrayList<>();
        // center.add(firstLeadCard);

        currentPlayerIndex = determineFirstPlayer();

        firstLeadCard = deck.get(0);
        Card centerCard = new Card(firstLeadCard.getSuit(), firstLeadCard.getRank());
        center.add(centerCard);
        deck.remove(0);

        System.out.println(
                "At the beginning of the game, the first lead card " + firstLeadCard + " is placed at the center.");
        System.out.println("Player" + (currentPlayerIndex + 1) + " is the first player because of " + firstLeadCard);
        System.out.println("c=club");
        System.out.println("d=diamond");
        System.out.println("h=heart");
        System.out.println("s=spade");
        System.out.println("User can press 'd' to draw card");
        System.out.println();
    }

    private static Card firstLeadCard;

    private static List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();

        String[] suits = { "c", "d", "h", "s" };
        String[] ranks = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };

        for (String suit : suits) {
            for (String rank : ranks) {
                deck.add(new Card(suit, rank));
            }
        }

        return deck;
    }

    private static void shuffleDeck(List<Card> deck) {
        Collections.shuffle(deck);
    }

    private static int determineFirstPlayer() {
        Card firstLeadCard = deck.get(0);
        center.add(firstLeadCard);
        deck.remove(0);

        String rank = firstLeadCard.getRank();

        if (rank.equals("A") || rank.equals("5") || rank.equals("9") || rank.equals("K")) {
            return 0; // Player1
        } else if (rank.equals("2") || rank.equals("6") || rank.equals("10")) {
            return 1; // Player2
        } else if (rank.equals("3") || rank.equals("7") || rank.equals("J")) {
            return 2; // Player3
        } else if (rank.equals("4") || rank.equals("8") || rank.equals("Q")) {
            return 3; // Player4
        }

        return -1;
    }

    private static void dealCards() {
        for (int i = 0; i < 7; i++) {
            for (Player player : players) {
                Card card = deck.get(0);
                player.addCardToHand(card);
                deck.remove(0);
            }
        }
    }

    
    private static void playTrick() {

        center.clear(); // Clear the center before each trick starts
        if (trickNumber > 1) {
            center.clear();
        } // Clear the center before each trick starts (except Trick 1)
        if (trickNumber == 1) {
            center.add(firstLeadCard);
        } // Add firstLeadCard to center only for Trick 2 onwards

        // int trickWinnerIndex = currentPlayerIndex;
        // Scanner scanner = new Scanner(System.in);

        Scanner scanner = new Scanner(System.in);
        int trickWinnerIndex = currentPlayerIndex;
        Card firstCard = null;
        Card highestCard = null;

        for (int i = 0; i < players.size(); i++) {
            Player currentPlayer = players.get(currentPlayerIndex);
        System.out.println("Turn: " + currentPlayer.getName());

        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getHand());
        }

        System.out.println("Center: " + center);
        System.out.println("Deck: " + deck);
        System.out.println("Score: " + getScores());

        System.out.println("Player" + (currentPlayerIndex + 1) + " Cards: " + currentPlayer.getHand());

            Card playedCard = null;
            boolean hasValidCard = false;

            while (!hasValidCard) {
                System.out.print(">");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("d")) {
                    Card drawnCard = currentPlayer.drawCardFromDeck(deck);
    
                    if (drawnCard != null) {
                        currentPlayer.addCardToHand(drawnCard);
                        System.out.println("Player" + (currentPlayerIndex + 1) + " draws " + drawnCard + ".");
                    } else {
                        System.out.println("No cards left in the deck.");
                        break;
                    }
                } else {
                    if (isValidInput(input, currentPlayer.getHand())) {
                        playedCard = findCardInHand(input, currentPlayer.getHand());
        
                        if (center.isEmpty()) {
                            // First player plays any card as the leading card
                            currentPlayer.removeCardFromHand(playedCard);
                            center.add(playedCard);
                            System.out.println("Player" + (currentPlayerIndex + 1) + " plays " + playedCard + ".");
                            System.out.println("Center: " + center);
                            firstCard = playedCard; // Store the first card played in the trick
                            hasValidCard = true;
                        } else {
                            Card leadingCard = center.get(0);
                            if (playedCard.getSuit().equals(leadingCard.getSuit()) || playedCard.getRank().equals(leadingCard.getRank())) {
                                // Subsequent players must follow the leading suit
                                currentPlayer.removeCardFromHand(playedCard);
                                center.add(playedCard);
                                System.out.println("Player" + (currentPlayerIndex + 1) + " plays " + playedCard + ".");
                                System.out.println("Center: " + center);
                                hasValidCard = true;
                            } else {
                                System.out.println("Invalid card. Please choose a card of the leading suit or press 'd' to draw a card.");
                            }
                        }
                    } else {
                        System.out.println("Invalid input. Please enter a valid card or press 'd' to draw a card.");
                    }
                }
            }

            if (i == 0) {
                firstCard = playedCard; // Assign the first card played in the trick
                highestCard = playedCard; // Assign the highest card as the first card
            } else {
                if (playedCard != null && playedCard.getSuit().equals(firstCard.getSuit())) {
                    if (playedCard.getRank().compareTo(highestCard.getRank()) > 0) {
                        highestCard = playedCard;
                        trickWinnerIndex = currentPlayerIndex;
                    }
                }
            }
    
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    
        Player currentTrickWinner = players.get(trickWinnerIndex);
        System.out.println("Trick winner: " + currentTrickWinner.getName());
    
        
        currentPlayerIndex = trickWinnerIndex;
    }
    

    private static String getScores() {
        StringBuilder scoreBuilder = new StringBuilder();
        for (Player player : players) {
            int score = player.getTricks().size();
            scoreBuilder.append(player.getName()).append(" = ").append(score).append(" | ");
        }
        return scoreBuilder.toString();
    }

    // private static int findTrickWinnerIndex(List<Card> center) {
    // Card leadCard = center.get(0);
    // String leadSuit = leadCard.getSuit();
    // String highestRank = "A";
    // int winnerIndex = -1;

    // for (int i = 0; i < center.size(); i++) {
    // Card card = center.get(i);
    // if (card.getSuit().equals(leadSuit) && compareRanks(card.getRank(),
    // highestRank) > 0) {
    // highestRank = card.getRank();
    // winnerIndex = (currentPlayerIndex + i) % players.size();
    // }
    // }

    // return winnerIndex;
    // }

    // private static int compareRanks(String rank1, String rank2) {
    // String[] ranks = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J",
    // "Q", "K" };
    // return Arrays.asList(ranks).indexOf(rank1) -
    // Arrays.asList(ranks).indexOf(rank2);
    // }

    // private static int getRankIndex(String rank) {
    // String[] ranks = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J",
    // "Q", "K" };
    // for (int i = 0; i < ranks.length; i++) {
    // if (rank.equals(ranks[i])) {
    // return i;
    // }
    // }
    // return -1;
    // }

    private static boolean isValidInput(String input, List<Card> hand) {
        for (Card card : hand) {
            if (card.toString().equalsIgnoreCase(input)) {
                return true;
            }
        }
        return input.equalsIgnoreCase("d");
    }

    private static Card findCardInHand(String input, List<Card> hand) {
        for (Card card : hand) {
            if (card.toString().equalsIgnoreCase(input)) {
                return card;
            }
        }
        return null;
    }

    // private static int compareRanks(String rank1, String rank2) {
    //     String[] ranks = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
    //     int index1 = Arrays.asList(ranks).indexOf(rank1);
    //     int index2 = Arrays.asList(ranks).indexOf(rank2);
    //     return Integer.compare(index1, index2);
    // }

    // private static int findTrickWinnerIndex(List<Card> trickCards, String currentSuit) {
    //     int winningIndex = 0;
    //     Card winningCard = null;

    //     for (int i = 0; i < trickCards.size(); i++) {
    //         Card currentCard = trickCards.get(i);

    //         if (currentCard.getSuit().equals(currentSuit)) {
    //             if (winningCard == null || compareRanks(currentCard.getRank(), winningCard.getRank()) > 0) {
    //                 winningCard = currentCard;
    //                 winningIndex = i;
    //             }
    //         }
    //     }

    //     return winningIndex;
    // }

    private static boolean isGameOver() {
        for (Player player : players) {
            if (player.getHand().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private static void displayScores() {
        System.out.println("Game Over!");
        System.out.println("Final Scores:");

        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getTricks().size() + " tricks");
        }
    }
}