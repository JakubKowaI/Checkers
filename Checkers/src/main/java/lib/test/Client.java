package lib.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;



public class Client extends Application {

    private Socket socket;
    private Scanner in;
    private PrintWriter outa;

    public Client(){

    }

    public Client(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            in = new Scanner(socket.getInputStream());
            outa = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Metoda wysylajaca wiadomosc do serwera
    public void tellServer(String message) {
        outa.println("SAY " + message);
    }


    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 55555);
        Scanner input = new Scanner(System.in);

        Listener listener = new Listener(client.in);
        Thread listenerThread = new Thread(listener);
        listenerThread.start();

        launch();

        while(input.hasNextLine()){
            //Sczytywanie wiadomosci od gracza
            String message = input.nextLine();
            //Wysylanie wiadomosci do serwera
            client.tellServer(message);
        }
        try{
        listenerThread.interrupt();
        client.socket.close();
        input.close();
        System.exit(0);
        }catch(IOException e){
            e.printStackTrace();
        }
    }   
}
