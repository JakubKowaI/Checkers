package lib.test.Player;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Client extends Application {

    private static String serverAddress;
    private static int port;
    Pane background;
    private ClientHandler doer;
    VBox root = new VBox();
    private Stage primaryStage;

    public void refreshBoard(char[][] board) {
            root.getChildren().clear();
        for(int i=0;i<17;i++) {
            HBox row = new HBox();
            row.setAlignment(javafx.geometry.Pos.CENTER);
            row.setSpacing(5);
            root.getChildren().add(row);
            if (i != 0) {
                root.setMargin(row, new Insets(-1.5, 0, 0, 0));
            }
            for (int j = 0; j < 25; j++) {
                switch (board[i][j]) {
                    case 'p':
                        Circle circle = new Circle(10);
                        circle.setFill(javafx.scene.paint.Color.WHITE);
                        circle.setStroke(javafx.scene.paint.Color.BLACK);
                        row.getChildren().add(circle);
                        break;
                    case 'r':
                        Circle circle2 = new Circle(10);
                        circle2.setFill(javafx.scene.paint.Color.RED);
                        circle2.setStroke(javafx.scene.paint.Color.BLACK);
                        row.getChildren().add(circle2);
                        break;
                    case 'b':
                        Circle circle3 = new Circle(10);
                        circle3.setFill(javafx.scene.paint.Color.BLUE);
                        circle3.setStroke(javafx.scene.paint.Color.BLACK);
                        row.getChildren().add(circle3);
                        break;
                    case 'y':
                        Circle circle4 = new Circle(10);
                        circle4.setFill(javafx.scene.paint.Color.YELLOW);
                        circle4.setStroke(javafx.scene.paint.Color.BLACK);
                        row.getChildren().add(circle4);
                        break;
                    case 'g':
                        Circle circle5 = new Circle(10);
                        circle5.setFill(javafx.scene.paint.Color.GREEN);
                        circle5.setStroke(javafx.scene.paint.Color.BLACK);
                        row.getChildren().add(circle5);
                        break;
                    case 'o':
                        Circle circle6 = new Circle(10);
                        circle6.setFill(javafx.scene.paint.Color.ORANGE);
                        circle6.setStroke(javafx.scene.paint.Color.BLACK);
                        row.getChildren().add(circle6);
                        break;
                    case 'v':
                        Circle circle7 = new Circle(10);
                        circle7.setFill(Color.VIOLET);
                        circle7.setStroke(javafx.scene.paint.Color.BLACK);
                        row.getChildren().add(circle7);
                        break;
                }
            }
        }
        primaryStage.sizeToScene();
    }

    // Static method for initialization
    public static void launchClient(String serverAddress, int port) {
        Client.serverAddress = serverAddress;
        Client.port = port;
        launch(); // Launch JavaFX application
    }

    @Override
    public void start(Stage mainStage) {
        // Initialize ClientHandler using static fields
        doer = new ClientHandler(serverAddress, port, this);
        new Thread(doer).start();
        doer.getBoard();
        //doer.say("Hello"+this);

        primaryStage = mainStage;
        primaryStage.setTitle("Checkers");
        background = new Pane();
//        Button btn = new Button();
//        btn.setText("Click Me");
//        btn.setOnAction(event -> doer.say("Hello from "+ this));
        //root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 500, 300));
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

        // Call methods on doer
        //doer.say("Hello");
    }

    @Override
    public void stop() {
        // executed when the application shuts down
        doer.stop();
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }
}
