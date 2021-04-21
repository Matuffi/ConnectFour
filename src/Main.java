// ###      MAIN PROGRAM      ###

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Main {

    static Scanner scan = new Scanner(System.in);
    static CustomFunctions cust = new CustomFunctions();

    // Universal input varriable
    static String input;

    private static final int maxNameLength = 8;

    public static void main(String[] args) throws FileNotFoundException {
        
        // Tells if the while loop has looped
        boolean repeatLoop = false;

        while(true){

            // MAIN MENU
            // Menu image
            cust.clrscr();
            cust.PrintFromFile("ConnectFour.txt");

            // Choices and user input
            System.out.printf("1. PLAY\n2. RULES\n3. LEADERBOARD\n4. EXIT\n\n");

            // Error code
            if(repeatLoop) System.out.printf("ERR: Please enter a valid input\n\n");
            repeatLoop = false;

            System.out.printf("Enter your choice - ");
            input = scan.nextLine();

            // Directs the user to the next menu
            if(input.equals("1") || input.toUpperCase().equals("PLAY") || input.toUpperCase().equals("P")) { NewGame(); continue; }            
            if(input.equals("2") || input.toUpperCase().equals("RULES") || input.toUpperCase().equals("R")) { Rules(); continue; }
            if(input.equals("3") || input.toUpperCase().equals("LEADERBOARD") || input.toUpperCase().equals("L")) { DisplayLeaderboard(); continue; }        
            if(input.equals("4") || input.toUpperCase().equals("EXIT") || input.toUpperCase().equals("E")) return;
            
            // Commands
            if(input.equals("/clearlog")){
                PrintWriter clearLog = new PrintWriter(new File("leaderboardLog.txt"));
                clearLog.print("");
                clearLog.close();
                continue;
            }

            // Program never gets here unless the input was invalid
            repeatLoop = true;
        }
    }

    // Start a game
    private static void NewGame() throws FileNotFoundException {

        int xSize;
        int ySize;

        // Tells if the while loop has looped
        boolean repeatLoop = false;

        while(true){

            // Menu image
            cust.clrscr();
            cust.PrintFromFile("NewGame.txt");
            
            // Choices and user input
            System.out.printf("1. CLASSIC (7x6)\n2. CUSTOM\n3. BACK\n\n");

            // Error code
            if(repeatLoop) System.out.printf("ERR: Type the number/word/first letter of the choice of yours\n\n");

            System.out.printf("Enter your choice - ");
            input = scan.nextLine();
            
            // Directs user to the next menu
            if(input.equals("3") || input.toUpperCase().equals("BACK") || input.toUpperCase().equals("B")) return;
            if(input.equals("1") || input.toUpperCase().equals("CLASSIC") || input.toUpperCase().equals("CL")){
                
                // Start a classic game
                Game game = new Game();
                game.Start();
                
                // Save game stats to leaderboard and exit back to the Main menu
                SaveToLeaderboard(game.winnerIndex, game.timeElapsedString, game.gameMoves, game.gameWasHalted);
                return;
            }
            if(input.equals("2") || input.toUpperCase().equals("CUSTOM") || input.toUpperCase().equals("CU")){
                
                // Get custom game settings with validated user input
                // NB! Validation is checking only if the input is an integer. If it's out of board size limits, it is gapped to the max or min

                // Horizontal setting
                repeatLoop = false;
                do{
                    // Menu image
                    cust.clrscr();
                    cust.PrintFromFile("CustomGame.txt");
                    
                    // Ask user input
                    System.out.printf("Enter game board horizontal size (%d - %d)\n\n", Game.minSize, Game.maxSize);
                    
                    // Error code
                    if(repeatLoop) System.out.printf("ERR: Please enter a NUMBER\n\n");

                    System.out.printf("Enter your choice - ");
                    input = scan.nextLine();
    
                    repeatLoop = !cust.CheckInput(input);
                } while(repeatLoop);
                // Save the input and continue
                xSize = Integer.parseInt(input);
                
                // Vertical setting
                repeatLoop = false;
                do{
                    // Menu image
                    cust.clrscr();
                    cust.PrintFromFile("CustomGame.txt");
                    
                    // Ask for input
                    System.out.printf("Enter game board vertical size (%d - %d)\n\n", Game.minSize, Game.maxSize);

                    // Error code
                    if(repeatLoop) System.out.printf("ERR: Please enter a NUMBER\n\n");

                    System.out.printf("Enter your choice - ");
                    input = scan.nextLine();
    
                    repeatLoop = !cust.CheckInput(input);
                } while(repeatLoop);
                // Save the input and continue
                ySize = Integer.parseInt(input);
                
                // Start a custom game
                Game game = new Game(xSize, ySize);
                game.Start();
                
                // Save game stats to leaderboard and exit back to the Main menu
                SaveToLeaderboard(game.winnerIndex, game.timeElapsedString, game.gameMoves, game.gameWasHalted);
                return;
            }
            repeatLoop = true;
        }

    }

    // Add game stats to leaderboard
    private static void SaveToLeaderboard(int winnerIndex, String elapsedTime, int gameMoves, boolean gameWasHalted) throws FileNotFoundException {

        FileOutputStream fos = new FileOutputStream("leaderboardLog.txt", true);
        PrintWriter writer = new PrintWriter(fos);

        String playerName;

        boolean repeatLoop = false;
        boolean onlyLetters = true;

        // If the game was canceled with a command
        if(gameWasHalted){
            // Write to leaderboard
            writer.printf("DATE: %s %s | CANCELLED |                | TIME: %-7s | MOVES: %-4d\n", LocalDate.now(), LocalTime.now().truncatedTo(ChronoUnit.MINUTES), elapsedTime, gameMoves);
        } 
        // Game was won
        else if(winnerIndex != 0){
            do{
                // Menu image
                cust.clrscr();
                cust.PrintFromFile("Leaderboard.txt");

                // Ask for name
                System.out.printf("Enter your name (max %d characters; )\n\n", maxNameLength);
                
                // Error codes
                if(repeatLoop) System.out.printf("ERR: Your name must be inside the limits\n");
                if(!onlyLetters) System.out.printf("ERR: Please use only alphabetical letters\n");
                if(repeatLoop || !onlyLetters) System.out.printf("\n");

                System.out.printf(" - ");
                input = scan.nextLine();

                if(input.equals("")) input = "UNKNOWN";

                // Name validation
                repeatLoop = input.length() > maxNameLength;
                onlyLetters = input.matches("[a-zA-Z]+");
            } while(repeatLoop || !onlyLetters);
            playerName = input.toUpperCase();

            // Writo to leaderboard
            writer.printf("DATE: %s %s | WINNER: %s | NAME: %-8s | TIME: %-7s | MOVES: %-4d\n", LocalDate.now(), LocalTime.now().truncatedTo(ChronoUnit.MINUTES), Game.chars[winnerIndex], playerName, elapsedTime, gameMoves);
        }
        // Tie
        else{
            // Write to leaderboard
            writer.printf("DATE: %s %s | TIE       |                | TIME: %-7s | MOVES: %-4d\n", LocalDate.now(), LocalTime.now().truncatedTo(ChronoUnit.MINUTES), elapsedTime, gameMoves);
        }

        writer.close();
    }

    // Display rules
    private static void Rules() throws FileNotFoundException {
        
        // Menu image and rules
        cust.clrscr();
        cust.PrintFromFile("Rules.txt");

        cust.PrintFromFile("RuleText.txt");

        cust.pressEnterToContinue();
        scan.nextLine();
    }

    // Show leaderboard
    private static void DisplayLeaderboard() throws FileNotFoundException{

        // Menu image
        cust.clrscr();
        cust.PrintFromFile("Leaderboard.txt");

        // Check if the leaderboard has info and displays it
        if(new File("leaderboardLog.txt").length() == 0){
            System.out.printf("Leaderboard is empty\n");
        }
        else{
            cust.PrintFromFile("leaderboardLog.txt");
        }

        cust.pressEnterToContinue();
    }
}
