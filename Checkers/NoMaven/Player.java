package lib.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Player implements Runnable {
    char mark; // Znacznik gracza (opcjonalne, do rozbudowy)
    static Player[] players = new Player[6]; // Tablica graczy w grze
    Socket socket;
    Scanner input; // Wejście od klienta
    PrintWriter output; // Wyjście do klienta
    static int playerCount = 0; // Liczba graczy w grze
    int playerNumber; // Numer gracza
    Board board; // Referencja do planszy

    // Konstruktor gracza
    public Player(Socket socket, Board board) {
        this.socket = socket;
        this.board = board;
        playerCount++;
        playerNumber = playerCount;
        players[playerNumber - 1] = this; // Dodanie gracza do tablicy
    }

    // Zwracanie następnego gracza
    public Player nextPlayer() {
        return players[(playerNumber) % playerCount];
    }

    // Uruchamianie wątku gracza
    @Override
    public void run() {
        try {
            setup(); // Inicjalizacja połączenia
            processCommands(); // Przetwarzanie komend gracza
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Ustawianie połączenia z graczem
    private void setup() throws IOException {
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);
        output.println("WELCOME Player " + playerNumber);
        output.println("Current board:\n" + board.getBoardState());
    }

    // Przetwarzanie komend od gracza
    private void processCommands() {
        while (input.hasNextLine()) {
            String command = input.nextLine();
            if (command.startsWith("QUIT")) {
                return;
            } else if (command.startsWith("SAY")) {
                handleSayCommand(command);
            } else if (command.startsWith("MOVE")) {
                handleMoveCommand(command);
            } else {
                output.println("INVALID_COMMAND");
            }
        }
    }

    // Obsługa ruchu gracza
    private void handleMoveCommand(String command) {
        try {
            String[] parts = command.split(" ");
            int startX = Integer.parseInt(parts[1]);
            int startY = Integer.parseInt(parts[2]);
            int endX = Integer.parseInt(parts[3]);
            int endY = Integer.parseInt(parts[4]);

            if (board.currentPlayer != this) {
                output.println("NOT_YOUR_TURN");
                return;
            }

            Move move = new Move(startX, startY, endX, endY, board);

            if (move.execute()) {
                output.println("MOVE_OK");
                notifyAllPlayers("BOARD\n" + board.getBoardState());
                board.currentPlayer = nextPlayer();
            } else {
                output.println("INVALID_MOVE");
            }
        } catch (Exception e) {
            output.println("ERROR: Invalid move command.");
        }
    }



    // Wysyłanie wiadomości do innych graczy
    public void notifyAllPlayers(String message) {
        for (Player player : players) {
            if (player != null) {
                player.output.println(message);
            }
        }
    }

    // Obsługa komendy SAY
    private void handleSayCommand(String command) {
        String message = "Player " + playerNumber + ": " + command.substring(4).trim();
        notifyAllPlayers(message);
    }
}
