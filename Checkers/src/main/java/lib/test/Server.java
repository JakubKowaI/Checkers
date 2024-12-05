package lib.test;

import java.net.*;
import java.io.*;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws Exception {
        try (var listener = new ServerSocket(696969)) {
            System.out.println("Tic Tac Toe Server is Running...");
            var pool = Executors.newFixedThreadPool(200);
            while (true) {
                Board board = new Board();
                pool.execute(game.new Player(listener.accept(), 'X'));
                pool.execute(game.new Player(listener.accept(), 'O'));
            }
        }
    }

}