//package lib.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Board {

    Player currentPlayer;

    public synchronized void say(String text, Player player) {
        if(player!=currentPlayer){
            System.out.println("Not your turn");
        }else{
        System.out.println(text);
        currentPlayer = currentPlayer.nextPlayer();
        }
    }
}
