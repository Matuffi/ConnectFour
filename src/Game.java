
// ### GAME CONNECT FOUR ###

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

public class Game {

    // ### BEHIND THE SCENES ###

    Scanner scan = new Scanner(System.in);
    CustomFunctions cust = new CustomFunctions();
    
    private int defaultSize = 6;
    private int minSize = 4;
    private int maxSize = 32;
    private int sizeX = defaultSize;
    private int sizeY = defaultSize;

    public int winnerIndex;

    /* 
    index 0: empty
    index 1: player 1
    index 2: player 2
    */
    private String[] chars = {
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

        sizeX = sizeX < minSize ? minSize : sizeX;
        sizeY = sizeY < minSize ? minSize : sizeY;
        sizeX = sizeX > maxSize ? maxSize : sizeX;
        sizeY = sizeY > maxSize ? maxSize : sizeY;
        
        MainLoop(new int[sizeX][sizeY]);
    }


    // ### MAIN GAME ###

    private void MainLoop(int[][] gameBoard) throws FileNotFoundException{

        int[] numberBlacklist = new int[sizeX];
        int yPos;
        int xPos;
        int playerIndex = 1;
        
        String input;

        boolean gameRunning = true;
        boolean won = false;
        boolean inputRepeat;
        
        Instant start = Instant.now();

        gameloop:
        do{
            // Get player input
            inputRepeat = false;
            do{
                cust.clrscr();

                // If previous input failed
                if(inputRepeat){
                    System.out.printf("\n");
                    System.out.println("ERR: Please input a NUMBER that isn't crossed out.");
                }

                // Print board and ask input
                cust.PrintBoard(gameBoard, sizeX, sizeY, chars);
                System.out.println();
                cust.PrintIndexes(numberBlacklist, sizeX);
                System.out.printf("\n\nYour input - ");
                input = scan.nextLine();

                if(input.equals("/end")){
                    break gameloop;
                }
                if(input.equals("/tie")){
                    gameRunning = false;
                    break gameloop;
                }
                if(input.equals("/p1won")){
                    won = true;
                    playerIndex = 1;
                    break gameloop;
                }
                if(input.equals("/p2won")){
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


            // Add piece to board and log the y position
            xPos = Integer.parseInt(input) - 1;
            yPos = cust.AddToBoard(gameBoard, playerIndex, xPos);

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

        Instant end = Instant.now();
        long timeElapsed = Duration.between(start, end).toSeconds();

        cust.clrscr();

        // Game over screen

        // cust.PrintFromFile("GameOver.txt");
        cust.PrintBoard(gameBoard, sizeX, sizeY, chars);

        if(won){
            System.out.printf("\n\nPlayer  - %s -  won the game", chars[playerIndex]);
            winnerIndex = playerIndex;
        }
        else if(!gameRunning){
            System.out.printf("\n\nGame ended with a tie\n");
            winnerIndex = 0;
        }

        System.out.println("Ellapsed time - " + String.format("%d:%02d:%02d", timeElapsed / 3600, (timeElapsed % 3600) / 60, (timeElapsed % 60)));
    }
}
