import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {

        CustomFunctions cust = new CustomFunctions();
        
        while(true){

            // cust.PrintFromFile("ConnectFour.txt");
            
            Game game = new Game();
    
            game.Start();

            Thread.sleep(5000);;

        }
    }
}
