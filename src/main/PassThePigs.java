import java.util.ArrayList;
import java.util.Scanner;

class Player {
    private String name;
    private String strategy;
    private int handScore;

    public Player(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public String getStrategy() {
        return strategy;
    }
    public void implementStrategy(String strategy) {
        this.strategy = strategy;
    }
    public void updateScore(int rollScore) {
        handScore += rollScore;
    }
    public int getHandScore() {
        return handScore;
    }
    public void resetHandScore() {
        handScore = 0;
    }
    public boolean wantsToRoll(int playerScore, int handScore, ArrayList<Integer> allScores, int winningScore) {
        return false;
    }
}

class BotPlayer extends Player {
    private int rollCounter = 0;
    private static final int maxRolls =3;
    public BotPlayer(String name) {
        super(name);
        implementStrategy(name);
    }
    public boolean wantsToRoll(int playerScore, int handScore, ArrayList<Integer> allScores, int winningScore) {
        int cap = 45;
        if(handScore < cap && rollCounter < maxRolls) {
            rollCounter++;
            return true;
        } else {
            rollCounter = 0;
            return false;
        }
    }
}

class DumbBot extends Player {
    public DumbBot(String name) {
        super(name);
    }
    public boolean wantsToRoll(int playerScore, int handScore, ArrayList<Integer> allScores, int winningScore) {
        return handScore < 20 && playerScore + handScore < winningScore;
    }
    public void updateScore(int rollScore) {
        super.updateScore(rollScore);
    }
}

class HumanPlayer extends Player {
    private static Scanner scanner = new Scanner(System.in);

    public HumanPlayer(String name) {
        super(name);
        implementStrategy("Human");
    }

    public boolean wantsToRoll(int playerScore, int handScore, ArrayList<Integer> allScores, int winningScore) {
        //put strategy here
        System.out.println("Do you want to roll again? (yes/no)");
        String userInput = scanner.nextLine();
        if(userInput.equalsIgnoreCase("yes")) {
            return true;
        } else {
            System.out.println(getName() + " passes.");
            return false;
        }
    }
}

// deals with the moving of each player and their turns
class GameManager {
    private ArrayList<Player> players;
    private int[] scores;
    private int winningScore = 100;
    private boolean gameOn = true;
    private int currentPlayerIndex = 0;

    public GameManager(ArrayList<Player> players) {
        this.players = players;
        this.scores = new int[players.size()];
    }

    public void welcomeMessage() {
        System.out.println("Let's play Pass the Pigs!");
    }

    //does this every time there is a player's turn
    private void playerTurn(Player currentPlayer) {
        currentPlayer.resetHandScore();
        ArrayList<Integer> otherScores = everyOtherScores(currentPlayer);
        int playerIndex = players.indexOf(currentPlayer);
        int playerScore = scores[playerIndex];
        boolean wantsToRoll = currentPlayer.wantsToRoll(playerScore, currentPlayer.getHandScore(), otherScores, winningScore);

        while(wantsToRoll) {
            int rollOutcome1 = rollPiggy();
            int rollOutcome2 = rollPiggy();
            System.out.println(currentPlayer.getName() + " rolls: " + getRollName(rollOutcome1) + " and " + getRollName(rollOutcome2));
            int rollScore = calcRollScore(rollOutcome1, rollOutcome2);
            currentPlayer.updateScore(rollScore);

            if(rollScore == 0) {
                System.out.println("That's a PIG OUT !");
                break;
            } else {
                playerScore = scores[playerIndex];
                displayScores();
                System.out.println(currentPlayer.getName() + " rolls for a total of " + rollScore + ". Handscore is now " + currentPlayer.getHandScore() + ".");
            }

            if(!wantsToRoll) {
                playerScore += currentPlayer.getHandScore(); 
                scores[playerIndex] = playerScore;
                System.out.println(currentPlayer.getName() + " passes.");
                break;
            }
        }
    }

    //returns array of every score other than the current player's
    private ArrayList<Integer> everyOtherScores(Player currentPlayer) {
        ArrayList<Integer> otherScores = new ArrayList<>(scores.length - 1);

        for(Player player : players) {
            if(player != currentPlayer && scores[players.indexOf(player)] > 0) {
                otherScores.add(scores[players.indexOf(player)]);
            }
        }
        return otherScores;
    }

    private void displayScores() {
        System.out.print("Scores: ");
        for(Player player : players) {
            System.out.print(player.getName() + ": " + player.getHandScore() + " | ");
        }
        System.out.println();
    }

    private String getRollName(int rollNum) {
        String[] pigRollNames = {"Dot", "No Dot", "RazorBack", "Trotter", "Snouter", "Leaning Jowsler"};
        return pigRollNames[rollNum];
    }

    //calculates the roll scores  of each roll outcome
    private static int calcRollScore(int roll1, int roll2) {
        if((roll1 == 0 && roll2 == 0) || (roll1 == 1 && roll2 == 1)) {
            return 1;
        } else if ((roll1 == 0 && roll2 == 1) || (roll1 == 1 && roll2 == 0)) {
            return 0; //pig out.
        } else if (roll1 == roll2) {
            int[] possibleScores = {0, 0, 5, 5, 10, 15};
            return possibleScores[roll1] * 20;
        } else {
            int[] possibleScores = {0, 0, 5, 5, 10, 15};
            return possibleScores[roll1] + possibleScores[roll2];
        }
    }

    //simulates rolling a pig
    private static int rollPiggy() {
        double dot = 34.90;
        double noDot = 30.20;
        double razorBack = 22.40;
        double trotter = 8.80;
        double snouter = 3.00;
        double rollProbability = (double) (Math.random() * 100);
        if(rollProbability < dot) {
            return 0; //dot
        } else if (rollProbability < dot + noDot) {
            return 1; // no dot
        } else if (rollProbability < dot + noDot + razorBack) {
            return 2; //razorback
        } else if (rollProbability < dot + noDot + razorBack + trotter) {
            return 3; //trotter
        } else if (rollProbability < dot + noDot + razorBack + trotter + snouter) {
            return 4; //snouter
        } else {
            return 5; // leaning jowsler
        }
    }

    public void playGame() {
        welcomeMessage();
        while(gameOn) {
            Player currentPlayer = players.get(currentPlayerIndex);
            playerTurn(currentPlayer);
            if(scores[currentPlayerIndex] >= winningScore) {
                System.out.println("Game over! Winner is: " + currentPlayer.getName());
                gameOn = false;
            }
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }
}

public class PassThePigs {
    public static void main(String[] args) {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new BotPlayer("Generic Bot"));
        players.add(new HumanPlayer("Human"));
        players.add(new DumbBot("Dummy"));


        GameManager gameManager = new GameManager(players);
        gameManager.playGame();
    }
}