//package lib.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Player implements Runnable {
        char mark;
        Player opponent;
        static Player[] opponents = new Player[6];
        Socket socket;
        Scanner input;
        PrintWriter output;
        static int playerCount = 0;
        int playerNumber;
        Board board;

        //Konstruktor gracza
        public Player(Socket socket, Board board) {
            this.socket = socket;
            this.board = board;
            playerCount++;
            playerNumber = playerCount;
            opponents[playerNumber-1] = this;
            board.currentPlayer = this;
        }

        //Zwracanie nastepnego gracza
        public Player nextPlayer() {
            if(playerNumber==playerCount){
                return opponents[0];
            }else {
                return opponents[playerNumber];
            }
        }

        //Uruchamianie watku gracza
        @Override
        public void run() {
            try {
                setup();
                processCommands();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (opponent != null && opponent.output != null) {
                    opponent.output.println("OTHER_PLAYER_LEFT");
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

        //Ustawianie polaczenia z graczem
        private void setup() throws IOException {
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("WELCOME " + playerNumber);

        }

        //Przetwarzanie komend od gracza
        private void processCommands() {
            while (input.hasNextLine()) {
                var command = input.nextLine();
                if (command.startsWith("QUIT")) {
                    return;
                } else if (command.startsWith("SAY")) {
                    doWhenSay(command);
                }
            }
        }

    private void processMoveCommand(String command) {
        try {
            // Parsowanie komendy: np. MOVE startX startY endX endY
            String[] parts = command.split(" ");
            int startX = Integer.parseInt(parts[1]);
            int startY = Integer.parseInt(parts[2]);
            int endX = Integer.parseInt(parts[3]);
            int endY = Integer.parseInt(parts[4]);

            // Tworzenie obiektu ruchu
            Move move = new Move(startX, startY, endX, endY, board);

            // Wykonanie ruchu
            if (move.execute()) {
                output.println("MOVE_OK");
            } else {
                output.println("INVALID_MOVE");
            }
        } catch (Exception e) {
            output.println("ERROR: Invalid move command.");
        }
    }

        //Wysylanie wiadomosci do innych graczy
        public void tellOponents(String message){
            for(int i=0;i<playerCount;i++){
                if(opponents[i]!=this){
                    opponents[i].output.println(message);
                }
            }
        }

        //Zajmowanie sie przypadkiem SAY
        private void doWhenSay(String text) {
                board.say(text,this);
                //output.println("Player "+playerNumber+" said something");
                //tellOponents("Opponent "+playerNumber+" said something");
            
        }

        
    }