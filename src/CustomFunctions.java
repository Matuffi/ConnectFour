// ###      CUSTOM FUNCTIONS      ###

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class CustomFunctions {

    // Print a given board and adapt if the board needs to have bigger grid spaces if the column indexes are 2 didgit numbers
    public void PrintBoard(int[][] currentBoard, int boardSizeX, int boardSizeY, String[] chars, boolean biggerSpacing){

        int space = biggerSpacing ? 3 : 2;

        for(int y = boardSizeY - 1; y >= 0; y--){
            for(int x = 0; x < boardSizeX; x++){

                System.out.printf("%" + space + "s", chars[currentBoard[x][y]]);
            }
            System.out.printf("\n");
        }   

        System.out.printf("\n");
    }

    // Print column indexes (blacklist)
    public void PrintIndexes(int[] numberBlacklist, int boardSizeX, boolean biggerSpacing) {

        int space = biggerSpacing ? 3 : 2;

        for(int i = 0; i < boardSizeX; i++){

            String out = String.valueOf(i + 1);

            if(Contains(numberBlacklist, i + 1)){
                out = "#";
            }

            System.out.printf("%" + space + "s", out);
        }
        System.out.printf("\n\n");
    }

    // Gets a file and prints it line by line. Escapes \, " and '
    public void PrintFromFile(String fileName) throws FileNotFoundException{

        Scanner fileWrite = new Scanner(GetFile(fileName));

        String line;
        while(fileWrite.hasNextLine()){
            line = fileWrite.nextLine();

            line.replace("\\", "\\\\");
            line.replace("\"", "\\\"");
            line.replace("\'", "\\\'");

            System.out.println(line);
        }
    }

    public File GetFile(String fileName){

        File file;

        if(FindFilePath(fileName) != null){
            return new File(FindFilePath(fileName));
        }
        
        ClassLoader classLoader = getClass().getClassLoader();
        file = new File(classLoader.getResource(fileName).getFile());

        if(file.exists()){
            return file;
        }

        return null;
    }

    public String FindFilePath(String fileName){

        String filePath = fileName;

        if(new File(filePath).exists()) return filePath;
        filePath = "src/" + fileName;
        if(new File(filePath).exists()) return filePath;
        filePath = "textFiles/" + fileName;
        if(new File(filePath).exists()) return filePath;
        filePath = "../textFiles/" + fileName;
        if(new File(filePath).exists()) return filePath;

        return null;
    }


    // Add a piece to the board and return y position
    public int AddToBoardAndReturnPos(int[][] gameBoard, int player, int xIndex){
        
        int yPos = 0;
        while(true){
            if(gameBoard[xIndex][yPos] == 0){
                gameBoard[xIndex][yPos] = player;
                break;
            }
            yPos++;
        }
        return yPos;
    }

    // Check if an added piece finished the game
    public boolean CheckWin(int[][] currentBoard, int lastX, int lastY, int player){

        // Horizontal check
        int count = 0;
        int iY;

        for(int i = 0; i < currentBoard.length; i++){
            if(currentBoard[i][lastY] == player){
                count++;
                if(count == 4){
                    return true;
                }
            }
            else{
                count = 0;
            }
        }

        // Vertical check
        count = 0;

        for(int i = 0; i < currentBoard[lastX].length; i++){
            if(currentBoard[lastX][i] == player){
                count++;
                if(count == 4){
                    return true;
                }
            }
            else{
                count = 0;
            }
        }

        // Diagonal (top-left to bottom-right) check
        count = 0;
        int offset = lastY - lastX;

        for(int iX = 0; iX < currentBoard.length; iX++){
            iY = iX + offset;

            if(iY > currentBoard[iX].length - 1){
                break;
            }
            if(iY < 0){
                continue;
            }

            if(currentBoard[iX][iY] == player){
                count++;
                if(count == 4){
                    return true;
                }
            }
            else{
                count = 0;
            }
        }

        // Diagonal (bottom-left to top-right) check
        count = 0;
        int sum = lastX + lastY;
        for(int iX = 0; iX < currentBoard.length; iX++){
            if(iX > sum){
                break;
            }
            if(sum - iX > currentBoard[iX].length - 1){
                continue;
            }

            iY = sum - iX;

            if(currentBoard[iX][iY] == player){
                count++;
                if(count == 4){
                    return true;
                }
            }
            else{
                count = 0;
            }
        }

        // If none of the checks were positive, the function returns false
        return false;
    }

    // Check if an array contains a integer
    public boolean Contains(int[] array, int value){

        boolean state = false;

        for(int i = 0; i < array.length; i++){
            state = value == array[i] ? true : false;
            if(state){
                break;
            }
        }
        return state;
    }

    // Check if input is a integer
    public boolean CheckInput(String in){
        
        if (in == null) {
            return false;
        }
        try {
            Integer.parseInt(in);
        } catch (NumberFormatException ex){
            return false;
        }
        return true;
    }

    // Check if input is a integer & isn't in blacklist & fits in the board
    public boolean CheckInput(String in, int[] blacklist, int boardSizeX){

        if (in == null) {
            return false;
        }
        try {
            int num = Integer.parseInt(in);
            if(!Contains(blacklist, num) && num > 0 && num <= boardSizeX){
                return true;
            }
            else{
                return false;
            }
        } catch (NumberFormatException ex){
            return false;
        }
    }

    // Clears console screen
    public void clrscr(){

        try {
            if (System.getProperty("os.name").contains("Windows")){
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else{
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException ex){

        }
    }

    // Pauses program until user presses Enter
    // After calling this function, there must be a Scanner.ReadLine() after it to eat the "\n" that System.in.read() didn't catch
    public void pressEnterToContinue(){ 

        System.out.printf("\nPress Enter to continue... ");
        try {
            System.in.read();
        }  
        catch(Exception e){
        }  
    }
}
