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

        public Player(Socket socket, Board board) {
            this.socket = socket;
            this.board = board;
            playerCount++;
            playerNumber = playerCount;
            opponents[playerNumber-1] = this;
            board.currentPlayer = this;
        }

        public Player nextPlayer() {
            if(playerNumber==playerCount){
                return opponents[0];
            }else {
                return opponents[playerNumber];
            }
        }

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

        private void setup() throws IOException {
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("WELCOME " + playerNumber);

        }

        private void processCommands() {
            while (input.hasNextLine()) {
                var command = input.nextLine();
                if (command.startsWith("QUIT")) {
                    return;
                } else if (command.startsWith("SAY")) {
                    processMoveCommand();
                }
            }
        }
        public void tellOponents(String message){
            for(int i=0;i<playerCount;i++){
                if(opponents[i]!=this){
                    opponents[i].output.println(message);
                }
            }
        }
        private void processMoveCommand() {
                board.say("test "+playerNumber,this);
                output.println("Player "+playerNumber+" said something");
                tellOponents("Opponent "+playerNumber+" said something");
            
        }

        
    }