package lib.test.Server;

import lib.test.Communication.Packet;
import lib.test.DB.AppConfig;
import lib.test.DB.MyService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.*;
import java.io.IOException;
import java.net.ServerSocket;

public class Board {
    private ServerSocket listener; // Poprawione odniesienie do ServerSocket
    private PlayerHandler[] playerHandler; // Tablica handlerów graczy
    private final char[][] board = new char[17][25]; // Plansza gry
    private final int playerCount; // Liczba graczy
    private int currentTurn = 0; // Tura gracza
    private final Map<Character, Pair> colorToHomeMap = new HashMap<>();
    private final char[] colors = {'r', 'b', 'g', 'y', 'o', 'v'}; // Kolory graczy

    private ArrayList<Character> winners = new ArrayList<>();

    public record Pair(int x, int y) {
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    public void refreshWinners() {
        this.winners = getAllWinningColors();
    }

    public boolean didPlayerWin(Character color) {
        return this.winners.contains(color);
    }
    // Konstruktor
    public Board(int port, int playerCount) {
        this.playerCount = playerCount;
        this.playerHandler = new PlayerHandler[playerCount]; // Inicjalizacja tablicy handlerów
        resetBoard();
        fillBoard();

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Retrieve the MyService bean
        myService = context.getBean(MyService.class);

        // Use the service
        myService.addSession(playerCount);
        int id = myService.getSessionID();
        myService.createTable();
        System.out.println("Session ID: " + id);

        try {
            this.listener = new ServerSocket(port); // Tworzenie gniazda serwera
            int i = 0;

            while (i < playerCount) {
                System.out.println("Czekam na połączenie z graczem " + (i + 1));
                PlayerHandler player = new PlayerHandler(listener.accept(), this);

                //Bot bot = new Bot(this);
                //bot.setStrategy(new NormalStrategy());
                //bot.start();

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
        broadcast(new Packet("TURN", "Tura gracza: " + (currentTurn + 1))); // TODO(ja): ensure that the player that we just chose did not yet win
    }

    public Boolean isValidMove(Packet packet) {
        return new Validator().isMoveValid(packet, board); // Walidacja ruchu
    }

    public void updateBoard(Packet packet) {
        board[packet.newY][packet.newX] = board[packet.oldY][packet.oldX];
        board[packet.oldY][packet.oldX] = 'p';

        char color = playerHandler[currentTurn].playerColor;
        myService.updateTable(packet.oldX, packet.oldY, packet.newX, packet.newY, color);

        broadcast(new Packet(board));
    }

    public boolean hasMoreJumps(int x, int y, int excludeX, int excludeY) {
        // Directions for possible jumps
        int[][] directions = {
                {-2, -2},
                {-2, 2},
                {2, -2},
                {2, 2},
        //        {-4, 0},
        //        {4, 0}  // TODO(ja): uncomment after fixing validator codes (adds ability to hop to the sides)
        };

        // Loop through each direction
        for (int[] dir : directions) {
            int newX = x + dir[0]; // Target position X
            int newY = y + dir[1]; // Target position Y
            int midX = x + dir[0] / 2; // Middle position X
            int midY = y + dir[1] / 2; // Middle position Y

            // Check if the target position is within board bounds
            if (newX >= 0 && newX < board[0].length && newY >= 0 && newY < board.length &&
                    midX >= 0 && midX < board[0].length && midY >= 0 && midY < board.length) {

                // Exclude the jump if the target position matches (excludeX, excludeY)
                if (newX == excludeX && newY == excludeY) {
                    continue; // Skip this jump direction
                }

                // Check if the target position is empty ('p') and middle position contains another piece
                if (board[newY][newX] == 'p' && board[midY][midX] != ' ' && board[midY][midX] != 'p') {
                    return true; // Jump is possible
                }
            }
        }

        // If no valid jumps are possible
        return false;
    }

    public ArrayList<Character> getAllWinningColors() {
        System.out.println("getAllWinningColors called");
        ArrayList<Character> winners = new ArrayList<>();


        for (Map.Entry<Character, Pair> entry : colorToHomeMap.entrySet()) {
            Character color = entry.getKey();
            Pair originPoint = entry.getValue();

            if (!hasTenOfColor(color, originPoint)) continue;

            winners.add(color);
        }
        return winners;
    }

    public boolean hasTenOfColor(Character color, Pair originPoint) {
        Queue<int[]> coordinateQueue = new LinkedList<>();
        boolean[][] visited = new boolean[board.length][board[0].length];

        coordinateQueue.add(new int[]{originPoint.x, originPoint.y});
        visited[originPoint.y][originPoint.x] = true;

        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}, {2, 0}, {-2, 0}};

        if (board[originPoint.y][originPoint.x] != color)  return false;

        int discoveredFields = 1;

        while (!coordinateQueue.isEmpty()) {
            int[] current = coordinateQueue.poll();
            int currentX = current[0];
            int currentY = current[1];
            for (int[] dir : directions) {
                int newX = currentX + dir[0];
                int newY = currentY + dir[1];

                if (newX >= 0 && newX < board[0].length && newY >= 0 && newY < board.length) {
                    if (!visited[newY][newX]) {
                        if (!Character.isLetter(board[newY][newX])) {
                            continue;
                        }
                        if (board[newY][newX] != color) {
                            return false;
                        }
                        visited[newY][newX] = true;
                        coordinateQueue.add(new int[]{newX, newY});
                        discoveredFields++;
                        if (discoveredFields == 10) {
                            System.out.println("Detected " + color + " win");
                            return true;
                        }
                    }
                }
            }
        }

        return false;
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
                fillTriangleTop(0, 12, 0 + 4, 'r');  // accessed by board[y][x]
                colorToHomeMap.put('r', new Pair(12, 16));

                fillTriangleBottom(16, 12, 16 - 4, 'b');  // accessed by board[y][x]
                colorToHomeMap.put('b', new Pair(12, 0));
                break;

            case 3:
                fillTriangleTop(0, 12, 0 + 4, 'r');
                colorToHomeMap.put('r', new Pair(16, 12));

                fillTriangleTop(9, 3, 9 + 4, 'b');
                colorToHomeMap.put('b', new Pair(9, 21));

                fillTriangleTop(9, 21, 9 + 4, 'g');
                colorToHomeMap.put('g', new Pair(9, 3));
                break;

            case 4:
                fillTriangleTop(9, 3, 9 + 4, 'r');
                colorToHomeMap.put('r', new Pair(7, 21));

                fillTriangleTop(9, 21, 9 + 4, 'b');
                colorToHomeMap.put('b', new Pair(7, 3));

                fillTriangleBottom(7, 3, 7 - 4, 'g');
                colorToHomeMap.put('g', new Pair(9, 21));

                fillTriangleBottom(7, 21, 7 - 4, 'y');
                colorToHomeMap.put('y', new Pair(9, 3));
                break;

            case 6:
                fillTriangleTop(0, 12, 0 + 4, 'r');
                colorToHomeMap.put('r', new Pair(16, 12));

                fillTriangleBottom(16, 12, 16 - 4, 'b');
                colorToHomeMap.put('b', new Pair(0, 12));

                fillTriangleTop(9, 3, 9 + 4, 'g');
                colorToHomeMap.put('g', new Pair(7, 21));

                fillTriangleTop(9, 21, 9 + 4, 'y');
                colorToHomeMap.put('y', new Pair(7, 3));

                fillTriangleBottom(7, 3, 7 - 4, 'o');
                colorToHomeMap.put('o', new Pair(9, 21));

                fillTriangleBottom(7, 21, 7 - 4, 'v');
                colorToHomeMap.put('v', new Pair(9, 3));
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