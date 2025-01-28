package lib.test.Replay;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import lib.test.DB.AppConfig;
import lib.test.DB.MyService;
import lib.test.Player.ClientHandler;
import lib.test.Player.FXClasses.MyCircle;
import lib.test.Server.Board;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sound.midi.SysexMessage;
import java.util.Arrays;
import java.util.Scanner;

public class ReplayClient extends Application {
    //getboarddoer funkcja ktora wywoluje doer
    private static String serverAddress;
    private static int port;
    Pane background;
    VBox root = new VBox();
    private Stage primaryStage;
    public char myColor;
    public MyService myService;
    private final char[][] board = new char[17][25];

    public void printBoard(char[][] board) {
        for(int i=0;i<17;i++) {
            for (int j = 0; j < 25; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    public void resetBoard() {
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 25; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public void fillBoard(int playerCount) {
        fillFromTop(0, 12);
        fillFromBottom(16, 12);
        switch (playerCount) {
            case 2:
                fillTriangleTop(0, 12, 0 + 4, 'r');  // accessed by board[y][x]

                fillTriangleBottom(16, 12, 16 - 4, 'b');  // accessed by board[y][x]
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

    public void refreshBoard(char[][] board) {
        this.background.getChildren().clear();
        new MyCircle(-1,-1).flushCircles();

        for(int i=0;i<17;i++) {
            for (int j = 0; j < 25; j++) {
                Circle circle = new Circle(10);
                circle.setFill(javafx.scene.paint.Color.WHITE);
                circle.setStroke(javafx.scene.paint.Color.BLACK);

                Circle myCircle = null;
                if(board[i][j]!=' ') {
                    background.getChildren().add(circle);
                    circle.setCenterX((j * 13) + 15);
                    circle.setCenterY((i * 20) + 15);
                }
            }
        }
        for(int i=0;i<17;i++) {
            for (int j = 0; j < 25; j++) {
                Circle circle = new Circle(10);
                Circle myCircle = null;
                switch (board[i][j]) {
                    case 'p':
                        break;
                    case 'r':
                        myCircle = new Circle(10);
                        myCircle.setFill(javafx.scene.paint.Color.RED);
                        break;
                    case 'b':
                        myCircle = new Circle(10);
                        myCircle.setFill(Color.BLUE);
                        break;
                    case 'y':
                        myCircle = new Circle(10);
                        myCircle.setFill(Color.YELLOW);
                        break;
                    case 'g':
                        myCircle = new Circle(10);
                        myCircle.setFill(Color.GREEN);
                        break;
                    case 'o':
                        myCircle = new Circle(10);
                        myCircle.setFill(Color.ORANGE);
                        break;
                    case 'v':
                        myCircle = new Circle(10);
                        myCircle.setFill(Color.PURPLE);
                        break;
                    default:
                        break;
                }
                if(myCircle != null) {
                    background.getChildren().add(myCircle);
                    myCircle.setStroke(Color.BLACK);
                    myCircle.setCenterX((j*13)+15);
                    myCircle.setCenterY((i*20)+15);
                }
            }
        }
        primaryStage.sizeToScene();
    }

    // Static method for initialization
    public static void launchClient() {
        launch(); // Launch JavaFX application
    }

    @Override
    public void start(Stage mainStage) { // Gui INIT here

        System.out.println("ReplayClient started. Choose replay type: \n1. last position \n2. full replay");
        Scanner input = new Scanner(System.in);
        int choice = 0;
        choice = input.nextInt();
        System.out.println("Input desired game id:");
        int game_id = input.nextInt();

        background = new Pane();
        System.out.println(myColor+" my color");
        primaryStage = mainStage;

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Retrieve the MyService bean
        myService = context.getBean(MyService.class);
        resetBoard();
        fillBoard(myService.getPlayerCount(game_id));
        primaryStage.setScene(new Scene(background, 350, 350));
        primaryStage.sizeToScene();
        primaryStage.show();

        // Add event filter for key presses
        primaryStage.getScene().addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.SPACE) {
                // Define the action to be performed when space is pressed
                System.out.println("Space key was pressed");
            }
        });

        primaryStage.getScene().addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.E) {
                // Define the action to be performed when space is pressed
                System.out.println("E key was pressed");
            }
        });

        final Move[][] moves = {myService.getFullReplay(game_id)};
        switch (choice) {
            case 1:
                for(Move move : moves[0]) {
                    //System.out.println("Move: "+ move.getOldX() +" "+ move.getOldY() +" "+ move.getNewX() +" "+ move.getNewY());
                    board[move.getNewY()][move.getNewX()] = board[move.getOldY()][move.getOldX()];
                    board[move.getOldY()][move.getOldX()] = 'p';
                }
                refreshBoard(board);
                break;
            case 2:
                for(Move move : moves[0]) {
//                    System.out.println("Move: "+ move.getOldX() +" "+ move.getOldY() +" "+ move.getNewX() +" "+ move.getNewY());
//                    board[move.getNewY()][move.getNewX()] = board[move.getOldY()][move.getOldX()];
//                    board[move.getOldY()][move.getOldX()] = 'p';
//                    refreshBoard(board);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    //choice = input.nextInt();
                }
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                        if (moves[0].length > 0) {
                            Move move1 = moves[0][0];
                            System.out.println("Move: " + move1.getOldX() + " " + move1.getOldY() + " " + move1.getNewX() + " " + move1.getNewY());
                            board[move1.getNewY()][move1.getNewX()] = board[move1.getOldY()][move1.getOldX()];
                            board[move1.getOldY()][move1.getOldX()] = 'p';
                            refreshBoard(board);
                            moves[0] = Arrays.copyOfRange(moves[0], 1, moves[0].length);
                        }
                    }));
                    timeline.setCycleCount(moves[0].length);
                    timeline.play();


                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }

    private void showWinPopup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("You win!");
        alert.showAndWait(); // Wait for the user to close the popup
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
