package lib.test.Player;

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
import lib.test.Player.FXClasses.MyCircle;

import javax.sound.midi.SysexMessage;

public class Client extends Application {
//getboarddoer funkcja ktora wywoluje doer
    private static String serverAddress;
    private static int port;
    Pane background;
    public ClientHandler doer;
    VBox root = new VBox();
    private Stage primaryStage;
    public char myColor;

    public void printBoard(char[][] board) {
        for(int i=0;i<17;i++) {
            for (int j = 0; j < 25; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    public void refreshBoard(char[][] board) {
        //root.getChildren().clear();
        //printBoard(board);
        this.background.getChildren().clear();
        new MyCircle(-1,-1).flushCircles();

        for(int i=0;i<17;i++) {
            for (int j = 0; j < 25; j++) {
                MyCircle circle = new MyCircle(j, i);
                circle.addCircle();
                circle.setFill(javafx.scene.paint.Color.WHITE);
                circle.setStroke(javafx.scene.paint.Color.BLACK);
                //StackPane stack = new StackPane();
                //stack.getChildren().add(circle);

                MyCircle myCircle = null;
                if(board[i][j]!=' ') {

                        //row.getChildren().add(circle);
                        background.getChildren().add(circle);
                        circle.setCenterX((j * 13) + 15);
                        circle.setCenterY((i * 20) + 15);

//                    case 'r':

                }
            }
        }
        for(int i=0;i<17;i++) {
            for (int j = 0; j < 25; j++) {
                MyCircle circle = new MyCircle(j, i);
                //circle.addCircle();
                //circle.setFill(javafx.scene.paint.Color.WHITE);
                //circle.setStroke(javafx.scene.paint.Color.BLACK);
                //StackPane stack = new StackPane();
                //stack.getChildren().add(circle);

                MyCircle myCircle = null;
                switch (board[i][j]) {
                    case 'p':
                        //row.getChildren().add(circle);
                        //background.getChildren().add(circle);
                        //circle.setCenterX((j*13)+15);
                        //circle.setCenterY((i*20)+15);
                        break;
                    case 'r':
                        //background.getChildren().add(circle);
                        //circle.setCenterX((j*13)+15);
                        //circle.setCenterY((i*20)+15);
                        myCircle = new MyCircle(j, i);
                        myCircle.setFill(javafx.scene.paint.Color.RED);
                        break;
                    case 'b':
                        //background.getChildren().add(circle);
                        //circle.setCenterX((j*13)+15);
                        //circle.setCenterY((i*20)+15);
                        myCircle = new MyCircle(j, i);
                        myCircle.setFill(Color.BLUE);
                        break;
                    case 'y':
                        //background.getChildren().add(circle);
                        //circle.setCenterX((j*13)+15);
                        //circle.setCenterY((i*20)+15);
                        myCircle = new MyCircle(j, i);
                        myCircle.setFill(Color.YELLOW);
                        break;
                    case 'g':
                        //background.getChildren().add(circle);
                        //circle.setCenterX((j*13)+15);
                        //circle.setCenterY((i*20)+15);
                        myCircle = new MyCircle(j, i);
                        myCircle.setFill(Color.GREEN);
                        break;
                    case 'o':
                        //background.getChildren().add(circle);
                        //circle.setCenterX((j*13)+15);
                        //circle.setCenterY((i*20)+15);
                        myCircle = new MyCircle(j, i);
                        myCircle.setFill(Color.ORANGE);
                        break;
                    case 'v':
                        //background.getChildren().add(circle);
                        //circle.setCenterX((j*13)+15);
                        //circle.setCenterY((i*20)+15);
                        myCircle = new MyCircle(j, i);
                        myCircle.setFill(Color.PURPLE);
                        break;
                    default:
                        //stack.getChildren().clear();
                        break;
                }
                if(myCircle != null) {
                    background.getChildren().add(myCircle);
                    myCircle.setStroke(Color.BLACK);
                    myCircle.addCircle();
                    //background.getChildren().add(circle);
                    myCircle.setCenterX((j*13)+15);
                    myCircle.setCenterY((i*20)+15);
                    //stack.getChildren().add(myCircle);
                }
                //row.getChildren().add(stack);
            }
        }
        primaryStage.sizeToScene();
        switch(myColor) {
            case 'r':
                primaryStage.setTitle("Checkers - Red");
                break;
            case 'b':
                primaryStage.setTitle("Checkers - Blue");
                break;
            case 'y':
                primaryStage.setTitle("Checkers - Yellow");
                break;
            case 'g':
                primaryStage.setTitle("Checkers - Green");
                break;
            case 'o':
                primaryStage.setTitle("Checkers - Orange");
                break;
            case 'v':
                primaryStage.setTitle("Checkers - Purple");
                break;
            default:
                primaryStage.setTitle("Checkers");
                break;
        }
    }

    // Static method for initialization
    public static void launchClient(String serverAddress, int port) {
        Client.serverAddress = serverAddress;
        Client.port = port;
        launch(); // Launch JavaFX application
    }

    @Override
    public void start(Stage mainStage) { // Gui INIT here
        // Initialize ClientHandler using static fields
        doer = new ClientHandler(serverAddress, port, this);
        new Thread(doer).start();
        new MyCircle(0,0).setClient(this);
        background = new Pane();
        doer.getBoard();
        //doer.say("Hello"+this);
        System.out.println(myColor+" my color");
        primaryStage = mainStage;

        //primaryStage.setTitle("Checkers");
//        Button btn = new Button();
//        btn.setText("Click Me");
//        btn.setOnAction(event -> doer.say("Hello from "+ this));
        //root.getChildren().add(btn);
        primaryStage.setScene(new Scene(background, 350, 350));
        primaryStage.sizeToScene();
        primaryStage.show();

        // Add event filter for key presses
        primaryStage.getScene().addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.SPACE) {
                // Define the action to be performed when space is pressed
                doer.getBoard();
                System.out.println("Space key was pressed");
                // Example action: send a message to the server
                //doer.say("Space key was pressed");
            }
        });

        primaryStage.getScene().addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.E) {
                // Define the action to be performed when space is pressed
                doer.getBoard();
                System.out.println("E key was pressed");
                // Example action: send a message to the server
                //doer.say("Space key was pressed");
            }
        });

        doer.addWinAction(() -> {
            showWinPopup();
            return null;
        });

        // Call methods on doer
        //doer.say("Hello");
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
        // executed when the application shuts down
        doer.stop();
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void sendMove(int x, int y, int x1, int y1) {
        doer.move(x, y, x1, y1);
    }

    public void setPlayerColor(char color) {
        this.myColor = color;
    }
}
