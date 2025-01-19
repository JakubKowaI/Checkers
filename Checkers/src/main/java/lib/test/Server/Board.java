package lib.test.Server;

import lib.test.Communication.Packet;

import java.io.IOException;
import java.net.ServerSocket;

public class Board {
    private ServerSocket listener; // Poprawione odniesienie do ServerSocket
    private PlayerHandler[] playerHandler; // Tablica handlerów graczy
    private final char[][] board = new char[17][25]; // Plansza gry
    private final int playerCount; // Liczba graczy
    private int currentTurn = 0; // Tura gracza

    private final char[] colors = {'r', 'b', 'g', 'y', 'o', 'v'}; // Kolory graczy

    // Konstruktor
    public Board(int port, int playerCount) {
        this.playerCount = playerCount;
        this.playerHandler = new PlayerHandler[playerCount]; // Inicjalizacja tablicy handlerów
        resetBoard();
        fillBoard();

        try {
            this.listener = new ServerSocket(port); // Tworzenie gniazda serwera
            int i = 0;

            while (i < playerCount) {
                System.out.println("Czekam na połączenie z graczem " + (i + 1));
                PlayerHandler player = new PlayerHandler(listener.accept(), this);
                player.start(); // Uruchomienie handlera dla gracza
                playerHandler[i] = player; // Zapisanie handlera do tablicy
                System.out.println("Gracz " + (i + 1) + " połączony.");
                i++;
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas uruchamiania serwera: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public char assignColor(int playerNumber) {
        return colors[playerNumber]; // Przypisanie koloru graczowi
    }

    public boolean isPlayerTurn(int playerNumber) {
        return playerNumber == currentTurn; // Sprawdzanie, czy to tura gracza
    }

    public void nextTurn() {
        playerHandler[currentTurn].validate = new Validator(); // Reset walidatora
        currentTurn = (currentTurn + 1) % playerCount; // Przechodzenie do kolejnej tury
        broadcast(new Packet("TURN", "Tura gracza: " + (currentTurn + 1)));
    }

    public Boolean isValidMove(Packet packet) {
        return new Validator().isMoveValid(packet, board); // Walidacja ruchu
    }

    public void updateBoard(Packet packet) {
        board[packet.newY][packet.newX] = board[packet.oldY][packet.oldX];
        board[packet.oldY][packet.oldX] = 'p';
        broadcast(new Packet(board));
    }

    public boolean hasMoreJumps(int x, int y) {
        // Sprawdzenie wszystkich możliwych kierunków dla kolejnych skoków
        int[][] directions = {{-2, -2}, {-2, 2}, {2, -2}, {2, 2}};
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            int midX = x + dir[0] / 2;
            int midY = y + dir[1] / 2;

            // Sprawdź, czy nowa pozycja jest w granicach planszy
            if (newX >= 0 && newX < board[0].length && newY >= 0 && newY < board.length) {
                // Sprawdź, czy miejsce docelowe jest puste, a pozycja środkowa zajęta
                if (board[newY][newX] == ' ' && board[midY][midX] != ' ' && board[midY][midX] != 'p') {
                    return true; // Możliwy kolejny skok
                }
            }
        }
        return false; // Brak możliwych kolejnych skoków
    }


    public char[][] getBoard() {
        return board;
    }

    public void resetBoard() {
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 25; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public void fillBoard() {
        fillFromTop(0, 12);
        fillFromBottom(16, 12);
        switch (playerCount) {
            case 2:
                fillTriangleTop(0, 12, 0 + 4, 'r');
                fillTriangleBottom(16, 12, 16 - 4, 'b');
                break;
            case 3:
                fillTriangleTop(0, 12, 0 + 4, 'r');
                fillTriangleTop(9, 3, 9 + 4, 'b');
                fillTriangleTop(9, 21, 9 + 4, 'g');
                break;
            case 4:
                fillTriangleTop(9, 3, 9 + 4, 'r');
                fillTriangleTop(9, 21, 9 + 4, 'b');
                fillTriangleBottom(7, 3, 7 - 4, 'g');
                fillTriangleBottom(7, 21, 7 - 4, 'y');
                break;
            case 6:
                fillTriangleTop(0, 12, 0 + 4, 'r');
                fillTriangleBottom(16, 12, 16 - 4, 'b');
                fillTriangleTop(9, 3, 9 + 4, 'g');
                fillTriangleTop(9, 21, 9 + 4, 'y');
                fillTriangleBottom(7, 3, 7 - 4, 'o');
                fillTriangleBottom(7, 21, 7 - 4, 'v');
                break;
            default:
                break;
        }
    }

    public void broadcast(Packet packet) {
        for (PlayerHandler player : playerHandler) {
            if (player != null) {
                player.send(packet);
            }
        }
    }

    private void fillFromTop(int y, int x) {
        if (y == 13) return;
        if (board[y][x] == ' ') {
            board[y][x] = 'p';
        }
        fillFromTop(y + 1, x - 1);
        fillFromTop(y + 1, x + 1);
    }

    private void fillFromBottom(int y, int x) {
        if (y == 3) return;
        if (board[y][x] == ' ') {
            board[y][x] = 'p';
        }
        fillFromBottom(y - 1, x - 1);
        fillFromBottom(y - 1, x + 1);
    }

    private void fillTriangleTop(int y, int x, int end, char fill) {
        if (y == end) return;
        if (board[y][x] == 'p') {
            board[y][x] = fill;
        }
        fillTriangleTop(y + 1, x - 1, end, fill);
        fillTriangleTop(y + 1, x + 1, end, fill);
    }

    private void fillTriangleBottom(int y, int x, int end, char fill) {
        if (y == end) return;
        if (board[y][x] == 'p') {
            board[y][x] = fill;
        }
        fillTriangleBottom(y - 1, x - 1, end, fill);
        fillTriangleBottom(y - 1, x + 1, end, fill);
    }
}
