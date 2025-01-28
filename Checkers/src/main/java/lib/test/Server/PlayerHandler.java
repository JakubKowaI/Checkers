package lib.test.Server;

import lib.test.Communication.Packet;
import lib.test.Server.Validator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class PlayerHandler extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private static int playerCount = 0;
    private final int playerNumber;
    public final char playerColor; // Kolor gracza
    private final Board board;

    public Validator validate = new Validator();

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
            send(new Packet("ASSIGN_COLOR", playerColor));

            while (true) {
                Packet packet = (Packet) in.readObject();
                switch (packet.command) {
                    case "QUIT":
                        return; // Zakończenie gry dla gracza
                    case "SAY":
                        board.broadcast(packet);
                        break;
                    case "GET_BOARD":
                        send(new Packet(board.getBoard())); // Wysłanie planszy
                        break;
                    case "MOVE":
                        handleMove(packet);
                        break;
                    case "GIVE_TURN":
                        handleGiveTurn();
                        break;
                    default:
                        send(new Packet("ERROR", "Nieznane polecenie: " + packet.command));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    private void handleMove(Packet packet) {
        if (!board.isPlayerTurn(playerNumber)) {
            send(new Packet("NOT_YOUR_TURN", "To nie jest Twoja tura!"));
            return;
        }

        if (board.didPlayerWin(this.playerColor)) {
            System.out.println("This player tried to move but its not his turn");
            return;
        }

        if (board.isValidMove(packet)) {
            board.updateBoard(packet);

            board.refreshWinners();

            if (board.didPlayerWin(this.playerColor)) {
                System.out.println("This player won, how cool: " + this.playerColor);
                // gracz wlasnie wygral, nie ma potrzeby sprawdzania dalej czy moze wykonac wiecej ruchow
                send(new Packet("YOU_WON", "Wygrales, brawo"));
                board.nextTurn();
                return;
            }

            // Rozróżnienie ruchu pojedynczego i skoku
            boolean isJump = Math.abs(packet.newX - packet.oldX) > 1 || Math.abs(packet.newY - packet.oldY) > 1; // TODO(ja): store this and check before each move to ensure that the player cant move afer he jumped prev

            if (isJump && board.hasMoreJumps(packet.newX, packet.newY, packet.oldX, packet.oldY)) {
                // Wielokrotny skok - gracz może kontynuować
                send(new Packet("MORE_JUMPS", "Możesz kontynuować skoki."));
            } else {
                // Ruch pojedynczy lub brak kolejnych skoków - zakończenie tury
                board.nextTurn();
            }
        } else {
            send(new Packet("INVALID_MOVE", "Nieprawidłowy ruch. Spróbuj ponownie."));
        }
    }

    private void handleGiveTurn() {
        if (board.isPlayerTurn(playerNumber)) {
            board.nextTurn();
        } else {
            send(new Packet("NOT_YOUR_TURN", "Nie Twoja tura!"));
        }
    }

    public void send(Packet packet) {
        try {
            out.reset();
            out.writeObject(packet);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnections() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}