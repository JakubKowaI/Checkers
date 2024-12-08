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
        // Sprawdź, czy współrzędne początkowe i końcowe są w granicach planszy
        if (!isWithinBounds(startX, startY) || !isWithinBounds(endX, endY)) {
            return false;
        }

        // Sprawdź, czy pole startowe nie jest puste
        if (!board.isOccupied(startX, startY)) {
            return false;
        }

        // Sprawdź, czy pole końcowe jest puste
        if (board.isOccupied(endX, endY)) {
            return false;
        }

        // Sprawdź, czy ruch odbywa się na sąsiednie pole (dla uproszczenia)
        int dx = Math.abs(startX - endX);
        int dy = Math.abs(startY - endY);
        if (dx > 1 || dy > 1 || (dx == 1 && dy == 1)) {
            return false; // Niedozwolony ruch ukośny lub na odległość większą niż 1
        }

        // W przyszłości: można dodać logikę obsługującą przeskakiwanie pionków
        return true;
    }

    // Metoda do wykonania ruchu
    public boolean execute() {
        if (!isValid()) {
            return false; // Jeśli ruch jest niepoprawny, nie wykonuj go
        }

        // Przenieś pionek na planszy
        board.movePiece(startX, startY, endX, endY);
        return true;
    }

    // Pomocnicza metoda do sprawdzania, czy współrzędne są w granicach planszy
    private boolean isWithinBounds(int x, int y) {
        return board.isValidPosition(x, y);
    }
}
