package lib.test;

import java.net.*;
import java.util.Scanner;
import java.util.concurrent.Executors;



public class Server implements Runnable {
    public void run() {
        try (var listener = new ServerSocket(55555)) {

            System.out.println("Let the game of checkers begin!");
            System.out.println("Enter the number of players");

            var pool = Executors.newFixedThreadPool(20);

            Scanner input = new Scanner(System.in);
            int playerCount = input.nextInt();
            System.out.println("Number of players: "+playerCount);
            int i=0;

            if(playerCount<=1||playerCount==5||playerCount>6){
                System.out.println("Invalid number of players");
                System.exit(0);
            }

            Board board = new Board();

            while (i<playerCount) {
                //Akceptowanie graczy
                pool.execute(new Player(listener.accept(), board));
                    // Player player = new Player(listener.accept(), board);
                    // board.currentPlayer=player;
                    // player.run();
                System.out.println("Player "+(i+1)+" connected");
                i++;
            }
            input.close();
            System.out.println("Game is full");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}