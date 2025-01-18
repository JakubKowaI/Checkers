package lib.test.Server;

import lib.test.Communication.Packet;
import lib.test.Server.Validator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerHandler extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private static int playerCount = 0;
    private int playerNumber;
    private char playerColor; // Dodano: kolor gracza
    private Board board;
    public Validator validate = new Validator();
    int lastX = -1;
    int lastY = -1;

    public PlayerHandler(Socket accept, Board board) {
        this.socket = accept;
        this.board = board;
        this.playerNumber = playerCount++;
        this.playerColor = board.assignColor(playerNumber); // Przypisanie koloru
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // Informowanie klienta o przypisanym kolorze
            out.writeObject(new Packet("ASSIGN_COLOR", playerColor));
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            Packet packet;
            try {
                packet = (Packet) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                break;
            }

            if (packet.command.equals("QUIT")) {
                break;
            } else if (packet.command.equals("SAY")) {
                board.broadcast(packet);
            } else if (packet.command.equals("GET_BOARD")) {
                try {
                    out.reset();
                    out.writeObject(new Packet(board.getBoard()));
                    out.flush();
                    out.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (packet.command.equals("MOVE")) {
                if (board.isPlayerTurn(playerNumber)) { // Sprawdzenie, czy jest tura gracza
                    if (board.isValidMove(packet)) { // Sprawdzenie legalności ruchu
                        board.updateBoard(packet); // Aktualizacja planszy
                        //board.broadcast(new Packet(board.getBoard())); // Wysłanie planszy do wszystkich

                        // Koniec tury gracza
                        board.nextTurn();
                    } else {
                        send(new Packet("INVALID_MOVE", "Ruch nie jest dozwolony!"));
                        board.broadcast(new Packet(board.getBoard()));
                    }
                } else {
                    send(new Packet("NOT_YOUR_TURN", "Nie Twoja tura!"));
                    board.broadcast(new Packet(board.getBoard()));
                }
            } else if (packet.command.equals("GIVE_TURN")) {
                if (board.isPlayerTurn(playerNumber)) {
                    board.nextTurn();
                } else {
                    send(new Packet("NOT_YOUR_TURN", "Nie Twoja tura!"));
                    board.broadcast(new Packet(board.getBoard()));
                }
            }
        }

        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void move(Packet packet) {
        board.isValidMove(packet);
    }

    public void send(Packet packet) {
        try {
//            if(packet.board != null) {
//                System.out.println("Sending board to player " + playerNumber);
//                for(int i = 0; i < 17; i++) {
//                    for(int j = 0; j < 25; j++) {
//                        System.out.print(packet.board[i][j]);
//                    }
//                    System.out.println();
//                }
//            }
            out.reset();
            out.writeObject(packet);
            out.flush();
            out.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
