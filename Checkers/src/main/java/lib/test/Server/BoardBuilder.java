package lib.test.Server;

import java.net.ServerSocket;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class BoardBuilder {
    public BoardBuilder(String localhost, int port) {


        System.out.println("Let the game of checkers begin!");
        System.out.println("Enter the number of players");

        //var pool = Executors.newFixedThreadPool(20);

        Scanner input = new Scanner(System.in);
        int playerCount = 0;
        try{
            playerCount = input.nextInt();
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Number of players: "+playerCount);

        if(playerCount<=1||playerCount==5||playerCount>6){
            System.out.println("Invalid number of players");
            System.exit(0);
        }

        Board board = new Board(port,playerCount);

        input.close();
        System.out.println("Game is full");

    }
}