//package lib.test;

import java.util.Arrays;

public class Board {

    // Gracz, który ma teraz ruch
    Player currentPlayer;

    // Tablica przechowująca stan planszy (np. "P1", "P2", null dla pustych pól)
    private String[][] boardState;

    // Rozmiar planszy
    private static final int BOARD_SIZE = 17;

    // Konstruktor klasy Board
    public Board() {
        boardState = new String[BOARD_SIZE][BOARD_SIZE]; // Inicjalizacja tablicy 17x17
        initializeBoard(); // Ustawienie kształtu planszy i początkowego układu pionków
    }

    // Inicjalizacja planszy (ustawienie pionków i pól gry)
    private void initializeBoard() {
        // Wypełnij całą tablicę nullami (pola poza planszą są puste)
        for (int i = 0; i < BOARD_SIZE; i++) {
            Arrays.fill(boardState[i], null);
        }

        // Ustaw pionki dla dwóch graczy (przykład dla górnego i dolnego promienia gwiazdy)
        placeInitialPieces(0, 4, "P1");  // Gracz 1 - górny promień
        placeInitialPieces(16, 12, "P2"); // Gracz 2 - dolny promień
    }

    // Ustawianie pionków na promieniu gwiazdy
    private void placeInitialPieces(int startX, int startY, String playerMark) {
        for (int i = 0; i < 4; i++) { // Ustawiamy pionki na 4 wierszach promienia
            for (int j = startY - i; j <= startY + i; j++) {
                // Sprawdź, czy indeksy nie wychodzą poza zakres planszy
                if (startX - i >= 0 && startX - i < BOARD_SIZE && j >= 0 && j < BOARD_SIZE) {
                    boardState[startX - i][j] = playerMark; // Ustaw pionek dla gracza
                }
            }
        }
    }


    // Sprawdza, czy pole na planszy jest zajęte
    public boolean isOccupied(int x, int y) {
        return isValidPosition(x, y) && boardState[x][y] != null;
    }

    // Przenosi pionek z pola startowego na docelowe
    public void movePiece(int startX, int startY, int endX, int endY) {
        if (isValidPosition(startX, startY) && isValidPosition(endX, endY)) {
            boardState[endX][endY] = boardState[startX][startY]; // Przenieś pionek
            boardState[startX][startY] = null;                  // Wyczyść pole startowe
        }
    }

    // Sprawdza, czy współrzędne są w granicach planszy i mieszczą się w jej kształcie
    public boolean isValidPosition(int x, int y) {
        if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) return false;

        // Sprawdź, czy pole jest w kształcie sześciokąta (logika dla planszy chińskich warcabów)
        int distanceFromCenter = Math.abs(BOARD_SIZE / 2 - x) + Math.abs(BOARD_SIZE / 2 - y);
        return distanceFromCenter <= BOARD_SIZE / 2;
    }

    // Zwraca rozmiar planszy
    public int getSize() {
        return BOARD_SIZE;
    }

    // Wyświetla aktualny stan planszy (pomocne do debugowania)
    public void printBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (boardState[i][j] == null) {
                    System.out.print(". "); // Puste pole
                } else {
                    System.out.print(boardState[i][j] + " "); // Pionek gracza
                }
            }
            System.out.println();
        }
    }

    // Obsługuje komunikaty od gracza
    public synchronized void say(String text, Player player) {
        if (player != currentPlayer) {
            System.out.println("Not your turn Player " + player.playerNumber);
        } else {
            // Wypisywanie ruchu gracza
            System.out.println(text);
            // Informowanie innych graczy o ruchu
            currentPlayer.tellOponents("Player " + player.playerNumber + " says: " + text);
            // Przekazanie ruchu do następnego gracza
            currentPlayer = currentPlayer.nextPlayer();
        }
    }
}

