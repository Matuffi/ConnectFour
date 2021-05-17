// ###      GAME CONNECT FOUR      ###

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

public class Game {

    // ### BEHIND THE SCENES ###

    Scanner scan = new Scanner(System.in);
    CustomFunctions cust = new CustomFunctions();
    
    public static final int minSize = 4;
    public static final int maxSize = 32;
    public int sizeX = 7;
    public int sizeY = 6;
    private boolean biggerSpacing;

    // Varriables for logging
    public int winnerIndex;
    public String timeElapsedString;
    public int gameMoves;
    public boolean gameWasCancelled;
    public boolean gameEnded;

    /* 
    index 0: empty
    index 1: player 1
    index 2: player 2
    */
    public static final String[] chars = {
        ".", "O", "X"
    };


    // Constructors
    public Game(){
    }
    public Game(int boardSize){

        sizeX = boardSize;
        sizeY = boardSize;
    }
    public Game(int boardSizeX, int boardSizeY){

        sizeX = boardSizeX;
        sizeY = boardSizeY;
    }


    // Initsialize game
    public void Start() throws FileNotFoundException{

        // Keeping the board size in limits
        sizeX = sizeX < minSize ? minSize : sizeX;
        sizeY = sizeY < minSize ? minSize : sizeY;
        sizeX = sizeX > maxSize ? maxSize : sizeX;
        sizeY = sizeY > maxSize ? maxSize : sizeY;

        biggerSpacing = sizeX >= 10 ? true : false;
        
        MainLoop(new int[sizeX][sizeY]);
    }


    // ### MAIN GAME ###

    private void MainLoop(int[][] gameBoard) throws FileNotFoundException{

        // numberBlacklist is used for marking full columns
        int[] numberBlacklist = new int[sizeX];
        int yPos;
        int xPos;
        int playerIndex = 1;
        
        String input;

        boolean gameRunning = true;
        boolean won = false;
        boolean inputRepeat;
        
        // For logging the game time and moves that the game has taken
        Instant start = Instant.now();
        gameMoves = 0;
        gameWasCancelled = false;
        gameEnded = false;

        gameloop:
        do{
            gameMoves++;

            // Get player input with validated input
            inputRepeat = false;
            do{
                cust.clrscr();
                
                // Print board and ask input
                cust.PrintBoard(gameBoard, sizeX, sizeY, chars, biggerSpacing);
                cust.PrintIndexes(numberBlacklist, sizeX, biggerSpacing);
                
                System.out.printf("Player - %s\n\n", chars[playerIndex]);

                // Error code
                if(inputRepeat) System.out.printf("ERR: Please enter a NUMBER of a column of your choice that isn't crossed out\n\n");

                System.out.printf("Your input - ");
                input = scan.nextLine();

                // Commands
                if(input.equals("/end")){
                    break gameloop;
                }
                if(input.equals("/tie")){
                    gameRunning = false;
                    break gameloop;
                }
                if(input.equals("/p1")){
                    won = true;
                    playerIndex = 1;
                    break gameloop;
                }
                if(input.equals("/p2")){
                    won = true;
                    playerIndex = 2;
                    break gameloop;
                }
                if(input.equals("/restart")){
                    MainLoop(new int[sizeX][sizeY]);
                }
                if(input.equals("/exit")){
                    return;
                }

                // If input is invalid - repeat
                inputRepeat = !cust.CheckInput(input, numberBlacklist, sizeX);
            } while(inputRepeat);


            // Add piece to board and get the y position
            xPos = Integer.parseInt(input) - 1;
            yPos = cust.AddToBoardAndReturnPos(gameBoard, playerIndex, xPos);

            // If a row is full - add the row to blacklist
            if(yPos + 1 == sizeY){
                numberBlacklist[xPos] = xPos + 1;
            }

            // If a player won the game is finished
            if(cust.CheckWin(gameBoard, xPos, yPos, playerIndex)){
                won = true;
                break;
            }

            // If every row is full - the game ended with a tie
            if(!cust.Contains(numberBlacklist, 0)){
                gameRunning = false;
            }

            // Next player
            playerIndex = playerIndex == 1 ? 2 : 1;

        } while(gameRunning);

        gameEnded = true;

        // Get the time and format it to a String
        Instant end = Instant.now();
        long timeElapsed = Duration.between(start, end).toSeconds();
        timeElapsedString = String.format("%d:%02d:%02d", timeElapsed / 3600, (timeElapsed % 3600) / 60, (timeElapsed % 60));

        cust.clrscr();

        // Game over screen
        cust.PrintFromFile("GameOver.txt");
        cust.PrintBoard(gameBoard, sizeX, sizeY, chars, biggerSpacing);

        // If the game exited with won set to true, there is a winner. Otherwise it was a tie. If gameRunning is true, the game was exited with a command.
        if(won){
            winnerIndex = playerIndex;
            System.out.printf("WINNER  -  %s\n", chars[playerIndex]);
        }
        else if(!gameRunning){
            System.out.printf("Game ended with a TIE\n");
            winnerIndex = 0;
        }
        else{
            gameWasCancelled = true;
        }

        System.out.printf("Ellapsed time - %s\nMoves - %d\n", timeElapsedString, gameMoves);

        cust.pressEnterToContinue();
        scan.nextLine();
    }
}
