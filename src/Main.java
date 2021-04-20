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

    static String input;

    private static int maxNameLength = 8;

    public static void main(String[] args) throws FileNotFoundException {
        
        boolean repeatLoop = false;

        while(true){

            cust.clrscr();
            cust.PrintFromFile("ConnectFour.txt");

            System.out.printf("1. PLAY\n2. RULES\n3. LEADERBOARD\n4. EXIT\n\n");

            if(repeatLoop) System.out.printf("ERR: Please enter a valid input\n\n");
            repeatLoop = false;

            System.out.printf("Enter your choice - ");
            input = scan.nextLine();

            if(input.equals("1") || input.toUpperCase().equals("PLAY") || input.toUpperCase().equals("P")) { NewGame(); continue; }            
            if(input.equals("2") || input.toUpperCase().equals("RULES") || input.toUpperCase().equals("R")) { Rules(); continue; }
            if(input.equals("3") || input.toUpperCase().equals("LEADERBOARD") || input.toUpperCase().equals("L")) { Leaderboard(); continue; }        
            if(input.equals("4") || input.toUpperCase().equals("EXIT") || input.toUpperCase().equals("E")) return;
            
            if(input.equals("/clearlog")){
                PrintWriter clearLog = new PrintWriter(new File("leaderboardLog.txt"));
                clearLog.print("");
                clearLog.close();
                continue;
            }
            repeatLoop = true;
        }
    }

    private static void NewGame() throws FileNotFoundException {

        int xSize;
        int ySize;

        boolean repeatLoop = false;

        while(true){

            cust.clrscr();
            cust.PrintFromFile("NewGame.txt");
            
            System.out.printf("1. CLASSIC (7x6)\n2. CUSTOM\n3. BACK\n\n");

            if(repeatLoop) System.out.printf("ERR: Type the number/word/first letter of the choice of yours\n\n");

            System.out.printf("Enter your choice - ");
            
            input = scan.nextLine();
            
            if(input.equals("3") || input.toUpperCase().equals("BACK") || input.toUpperCase().equals("B")) return;
            if(input.equals("1") || input.toUpperCase().equals("CLASSIC") || input.toUpperCase().equals("CL")){
                    
                Game game = new Game();
                game.Start();
    
                SaveToLeaderboard(game.winnerIndex, game.timeElapsedString, game.gameMoves);
                return;
            }
            if(input.equals("2") || input.toUpperCase().equals("CUSTOM") || input.toUpperCase().equals("CU")){
                
                repeatLoop = false;
                do{
                    cust.clrscr();
                    cust.PrintFromFile("CustomGame.txt");
                    
                    System.out.printf("Enter game board horizontal size (%d - %d)\n\n", Game.minSize, Game.maxSize);
                    
                    if(repeatLoop) System.out.printf("ERR: Please enter a NUMBER\n\n");

                    System.out.printf("Enter your choice - ");
                    input = scan.nextLine();
    
                    repeatLoop = !cust.CheckInput(input);
                } while(repeatLoop);
                xSize = Integer.parseInt(input);
    
                repeatLoop = false;
                do{
                    cust.clrscr();
                    cust.PrintFromFile("CustomGame.txt");
    
                    System.out.printf("Enter game board vertical size (%d - %d)\n\n", Game.minSize, Game.maxSize);

                    if(repeatLoop) System.out.printf("ERR: Please enter a NUMBER\n\n");

                    System.out.printf("Enter your choice - ");
                    input = scan.nextLine();
    
                    repeatLoop = !cust.CheckInput(input);
                } while(repeatLoop);
                ySize = Integer.parseInt(input);
    
                Game game = new Game(xSize, ySize);
                game.Start();
                
                SaveToLeaderboard(game.winnerIndex, game.timeElapsedString, game.gameMoves);
                return;
            }
            repeatLoop = true;
        }

    }

    private static void SaveToLeaderboard(int winnerIndex, String elapsedTime, int gameMoves) throws FileNotFoundException {

        FileOutputStream fos = new FileOutputStream("leaderboardLog.txt", true);
        PrintWriter writer = new PrintWriter(fos);

        String playerName;

        boolean repeatLoop = false;
        boolean onlyLetters = true;

        if(winnerIndex != 0){
            do{
                cust.clrscr();
                cust.PrintFromFile("Leaderboard.txt");

                
                System.out.printf("Enter your name (max %d characters; )\n\n", maxNameLength);
                
                if(repeatLoop) System.out.printf("ERR: Your name must be inside the limits\n");
                if(!onlyLetters) System.out.printf("ERR: Please use only alphabetical letters\n");
                if(repeatLoop || !onlyLetters) System.out.printf("\n");

                System.out.printf(" - ");
                input = scan.nextLine();

                if(input.equals("")) input = "UNKNOWN";

                repeatLoop = input.length() > maxNameLength;
                onlyLetters = input.matches("[a-zA-Z]+");
            } while(repeatLoop || !onlyLetters);
            playerName = input.toUpperCase();

            writer.printf("DATE: %s %s | WINNER: %s | NAME: %-8s | TIME: %-7s | MOVES: %-4d\n", LocalDate.now(), LocalTime.now().truncatedTo(ChronoUnit.MINUTES), Game.chars[winnerIndex], playerName, elapsedTime, gameMoves);
        }
        else{
            writer.printf("DATE: %s %s | TIE       |                | TIME: %-7s | MOVES: %-4d\n", LocalDate.now(), LocalTime.now().truncatedTo(ChronoUnit.MINUTES), elapsedTime, gameMoves);
        }

        writer.close();
    }

    private static void Rules() throws FileNotFoundException {
        
        cust.clrscr();
        cust.PrintFromFile("Rules.txt");

        cust.PrintFromFile("RuleText.txt");

        cust.pressEnterToContinue();
        scan.nextLine();
    }

    private static void Leaderboard() throws FileNotFoundException{

        cust.clrscr();
        cust.PrintFromFile("Leaderboard.txt");

        if(new File("leaderboardLog.txt").length() == 0){
            System.out.printf("Leaderboard is empty\n");
        }
        else{
            cust.PrintFromFile("leaderboardLog.txt");
        }

        cust.pressEnterToContinue();
    }
}
