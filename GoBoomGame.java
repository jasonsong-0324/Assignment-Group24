import java.util.*;

import java.io.*;

    public class GoBoomGame implements Serializable{
        private static List<Player> players;
        private static List<Card> deck;
        private static List<Card> center;
        private static int currentPlayerIndex;
        private static int trickNumber;
        private static boolean isFirstTrickWon;
        private static Map<String, Integer> scores = new HashMap<>();

        public static void main(String[] args) {
            GoBoomGame game = new GoBoomGame();
            String fileName = "game.ser";
            Scanner scanner = new Scanner(System.in);

            System.out.println("Welcome to Go Boom Game!");
            System.out.println("Enter 's' to start a new game or 'x' to exit.");

            String input = scanner.nextLine();

            while (!input.equals("x")) {
                if (input.equals("s")) {
                    game.startNewGame();
                }else if (input.equals("load")) {
                    System.out.print("Enter the file name to load the game: ");
                    fileName = scanner.nextLine();
                    game.loadGame(fileName);
                } else if (input.equals("save")) {
                    System.out.print("Enter the file name to save the game: ");
                    fileName = scanner.nextLine();
                    game.saveGame(fileName);
                } else if (input.equals("reset")){
                    game.resetgame();
                }else if (input.equals("quit")) {
                    System.out.println("Are you sure you want to quit? (y/n)");
                    String quitConfirmation = scanner.nextLine();
                    if (quitConfirmation.equalsIgnoreCase("y")) {
                        break; // exit the game loop
                    }else {
                        // Handle other game logic here
                        // ...
                        System.out.println("Performing game actions...");
                    }
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
            trickNumber = 1;
            
            isFirstTrickWon = false;
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

        private static void resetgame() {
            trickNumber = 1;
            for (Player player : players) {
                player.clearTricks(); // Clear the tricks
            }
            isFirstTrickWon = false;
        
            while (!isGameOver()) {
                System.out.println("Trick #" + trickNumber);
                System.out.println("--------------");
        
                playTrick();
                center.clear();
        
                trickNumber++;
        
                System.out.println();
        
                if (isGameOver()) {
                    break; // Exit the loop if the game is over
                }
            }
        
            for (Player player : players) {
                player.setScore(0); // Reset the score to 0
            }
        
            displayScores();
        }

        private static void initializeGame() {
            players = new ArrayList<>();
            players.add(new Player("Player1"));
            players.add(new Player("Player2"));
            players.add(new Player("Player3"));
            players.add(new Player("Player4"));

            GoBoomGame game = new GoBoomGame();
            deck = game.createDeck();
            shuffleDeck(deck);

            center = new ArrayList<>();
            // center.add(firstLeadCard);

            currentPlayerIndex = determineFirstPlayer();

            firstLeadCard = deck.get(0);
            Card centerCard = new Card(firstLeadCard.getSuit(), firstLeadCard.getRank());
            //center.add(centerCard);
            deck.remove(0);

            System.out.println(
                    "At the beginning of the game, the first lead card " + firstLeadCard + " is placed at the center.");
            System.out.println("Player" + (currentPlayerIndex + 1) + " is the first player because of " + firstLeadCard);
            System.out.println("c=club");
            System.out.println("d=diamond");
            System.out.println("h=heart");
            System.out.println("s=spade");
            System.out.println("User can press 'd' to draw card");
            System.out.println("User can press 'save' to save card");
            System.out.println("User can press 'load' to load card");
            System.out.println("User can press 'reset' to reset game");
            System.out.println();
        }

        private static Card firstLeadCard;

        // private static List<Card> createDeck() {
        //     List<Card> deck = new ArrayList<>();

        //     String[] suits = { "c", "d", "h", "s" };
        //     String[] ranks = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };

        //     for (String suit : suits) {
        //         for (String rank : ranks) {
        //             deck.add(new Card(suit, rank));
        //         }
        //     }

        //     return deck;
        // }

        private List<Card> createDeck() {
            List<Card> deck = new ArrayList<>();

            String[] suits ={"c", "d", "h", "s"};
            String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

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
            Card firstLeadCard = deck.iterator().next();
            center.add(firstLeadCard);
            // deck.remove(firstLeadCard);
        
            String rank = firstLeadCard.getRank();
        
            Set<String> player1Ranks = new HashSet<>(Arrays.asList("A", "5", "9", "K"));
            Set<String> player2Ranks = new HashSet<>(Arrays.asList("2", "6", "10"));
            Set<String> player3Ranks = new HashSet<>(Arrays.asList("3", "7", "J"));
            Set<String> player4Ranks = new HashSet<>(Arrays.asList("4", "8", "Q"));
        
            if (player1Ranks.contains(rank)) {
                return 0; // Player1
            } else if (player2Ranks.contains(rank)) {
                return 1; // Player2
            } else if (player3Ranks.contains(rank)) {
                return 2; // Player3
            } else if (player4Ranks.contains(rank)) {
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
        // private static void dealCards() {
        //     List<Card> deckList = new GoBoomGame().createDeck();
        //     Set<Card> deckSet = new HashSet<>(deckList);
        
        //     // Remove the center card from the deck
        //     Card centerCard = deckSet.iterator().next();
        //     deckSet.remove(centerCard);
        //     deckList.remove(centerCard);
        
        //     for (Player player : players) {
        //         for (int i = 0; i < 7; i++) {
        //             Card card = deckSet.iterator().next();
        //             player.addCardToHand(card);
        //             deckSet.remove(card);
        //             deckList.remove(card);
        //         }
        //     }
        
        //     // Update the center list
        //     center.clear();
        //     center.add(centerCard);
        
        //     // Remove the center card from players' hands
        //     for (Player player : players) {
        //         player.removeCardFromHand(centerCard);
        //     }
        
        //     // Remove the center card from the deck list
        //     deckList.remove(centerCard);
        
        //     // Update the deck reference if required
        //     deck = new ArrayList<>(deckList);
        // }
        

        
        private static void playTrick() {
            
            //center.clear(); // Clear the center before each trick starts
            if (trickNumber > 1) {
                center.clear();
            } // Clear the center before each trick starts (except Trick 1)
            // if (trickNumber == 1) {
            //     center.add(firstLeadCard);
            // } // Add firstLeadCard to center only for Trick 2 onwards

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
                    } else if (input.equalsIgnoreCase("save")) {
                        System.out.print("Enter the file name to save the game: ");
                        String fileName = scanner.nextLine();
                        saveGame(fileName);
                        
                        //saveCurrentPlayerIndex(fileName, currentPlayerIndex);
                        continue; // Continue to the next iteration of the loop
                    } else if (input.equalsIgnoreCase("load")) {
                        System.out.print("Enter the file name to load the game: ");
                        String fileName = scanner.nextLine();
                        loadGame(fileName);
                        //currentPlayerIndex = loadCurrentPlayerIndex(fileName); // Update currentPlayerIndex
                        
                        break; // Break out of the loop and return to the main menu
                    } else if (input.equalsIgnoreCase("reset")){
                        resetgame();
                    }  else if (input.equalsIgnoreCase("quit")) {
                        System.out.println("Are you sure you want to quit? (y/n)");
                        String quitConfirmation = scanner.nextLine();
                        if (quitConfirmation.equalsIgnoreCase("y")) {
                            System.out.println("Thanks for playing Go Boom Game!");
                            System.exit(0); // Terminate the program
                        } else {
                            System.out.println("Continuing the game...");
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
                        if (compareRanks(playedCard.getRank(), highestCard.getRank()) > 0) {
                            highestCard = playedCard;
                            trickWinnerIndex = currentPlayerIndex;
                        }
                    }
                    
                }
        
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            }
        
            Player currentTrickWinner = players.get(trickWinnerIndex);
            System.out.println("Trick winner: " + currentTrickWinner.getName());
            currentTrickWinner.incrementScore();
        
            
            currentPlayerIndex = trickWinnerIndex;
            if (trickNumber == 1 && trickWinnerIndex == 0) {
                isFirstTrickWon = true; // Set isFirstTrickWon to true if the first trick is won by Player1
            }
        }
        

        // private static String getScores() {
        //     StringBuilder scoreBuilder = new StringBuilder();
        //     for (Player player : players) {
        //         int score = player.getTricks().size();
        //         scoreBuilder.append(player.getName()).append(" = ").append(score).append(" | ");
        //     }
        //     return scoreBuilder.toString();
        // }
        private static Map<String, Integer> getScores() {
            Map<String, Integer> scores = new HashMap<>();
            for (Player player : players) {
                int score = player.getTricks().size();
                scores.put(player.getName(), score);
            }
            return scores;
        }

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

        private static int compareRanks(String rank1, String rank2) {
            String[] ranks = {  "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K","A" };
            int index1 = Arrays.asList(ranks).indexOf(rank1);
            int index2 = Arrays.asList(ranks).indexOf(rank2);
            return Integer.compare(index1, index2);
        }


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
                int score = player.getTricks().size();
                if (isFirstTrickWon && player == players.get(0)) {
                    score++; // Increase the score of the first trick winner if isFirstTrickWon is true
                }
                System.out.println(player.getName() + ": " + score + " tricks");
            }
        }
        private static int trickWinnerIndex;


       public static void saveGame(String fileName) {
            try {
                FileOutputStream fileOut = new FileOutputStream(fileName);
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                
                objectOut.writeObject(players);
                objectOut.writeObject(deck);
                objectOut.writeObject(center);
                objectOut.writeObject(trickNumber);
                objectOut.writeInt(currentPlayerIndex); // Save currentPlayerIndex
                objectOut.writeObject(players.get(currentPlayerIndex).getName()); // Save currentPlayer
                objectOut.close();
                fileOut.close();
                System.out.println("Game saved successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @SuppressWarnings("unchecked")
        public static void loadGame(String fileName) {
            try {
                FileInputStream fileIn = new FileInputStream(fileName);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
        
                Object rawPlayers = objectIn.readObject();
                Object rawDeck = objectIn.readObject();
                Object rawCenter = objectIn.readObject();
                Object rawTrickNumber = objectIn.readObject(); // Read trickNumber
                trickNumber = (int) rawTrickNumber; // Update trickNumber

                int loadedCurrentPlayerIndex = objectIn.readInt(); // Read currentPlayerIndex
                String loadedCurrentPlayerName = (String) objectIn.readObject(); // Read currentPlayerName
                // int loadedTrickWinnerIndex = objectIn.readInt(); // Read trickWinnerIndex
                // Card loadedFirstCard = (Card) objectIn.readObject(); // Read firstCard
        
                players = (List<Player>) rawPlayers;
                deck = (List<Card>) rawDeck;
                center = (List<Card>) rawCenter;
        
                objectIn.close();
                fileIn.close();
        
                currentPlayerIndex = loadedCurrentPlayerIndex; // Update currentPlayerIndex
                // trickWinnerIndex = loadedTrickWinnerIndex; // Update trickWinnerIndex
                // firstLeadCard = loadedFirstCard; // Update firstLeadCard
                
                // Find the player index based on the loaded player name
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getName().equals(loadedCurrentPlayerName)) {
                    currentPlayerIndex = i;
                    break;
                }
            }
        
                // Print the loaded player's turn
                Player currentTurnPlayer = players.get(currentPlayerIndex);
                System.out.println("Game loaded successfully.");
                System.out.println(center);
                System.out.println("Trick #" + trickNumber);
                System.out.println("--------------");
                 
                // Continue the game
                playTrick();
        
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Failed to load the game: " + e.getMessage());
            }
        }
        
    }
