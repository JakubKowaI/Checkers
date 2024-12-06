//package lib.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Board {

    //Gracz ktor ma teraz ruch
    Player currentPlayer;

    public synchronized void say(String text, Player player) {
        if(player!=currentPlayer){
            System.out.println("Not your turn Player "+player.playerNumber);
        }else{
            //Wypisywanie ruchu gracza
        System.out.println(text);
        //Mowienie ruchu innym graczom
        currentPlayer.tellOponents("Player "+player.playerNumber+" says: "+text);
        //Przekazywanie ruchu do nastepnego gracza
        currentPlayer = currentPlayer.nextPlayer();
        }
    }
}
