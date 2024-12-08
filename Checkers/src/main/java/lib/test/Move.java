package lib.test;

// Plik: Move.java
public class Move {
    private int startX, startY; // Początkowe współrzędne ruchu
    private int endX, endY;     // Końcowe współrzędne ruchu
    private Board board;        // Referencja do planszy

    // Konstruktor klasy Move
    public Move(int startX, int startY, int endX, int endY, Board board) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.board = board;
    }

    // Metoda do sprawdzenia poprawności ruchu
    public boolean isValid() {
        // Przykładowa logika: sprawdź czy współrzędne są na planszy
        if (!isWithinBounds(startX, startY) || !isWithinBounds(endX, endY)) {
            return false;
        }

        // Sprawdź, czy pole startowe nie jest puste (tu potrzebna byłaby logika planszy)
        if (!board.isOccupied(startX, startY)) {
            return false;
        }

        // Sprawdź, czy pole końcowe jest puste
        if (board.isOccupied(endX, endY)) {
            return false;
        }

        // Sprawdź reguły gry - np. ruch tylko na sąsiednie pola (dla uproszczenia)
        if (Math.abs(startX - endX) > 1 || Math.abs(startY - endY) > 1) {
            return false;
        }

        // Jeśli wszystkie testy przeszły, ruch jest poprawny
        return true;
    }

    // Metoda do wykonania ruchu
    public boolean execute() {
        if (!isValid()) {
            return false; // Jeśli ruch jest niepoprawny, nie wykonuj go
        }

        // Aktualizuj planszę: przenieś pionek z pola startowego na końcowe
        board.movePiece(startX, startY, endX, endY);
        return true;
    }

    // Pomocnicza metoda do sprawdzania, czy współrzędne są w granicach planszy
    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < board.getSize() && y >= 0 && y < board.getSize();
    }
}

