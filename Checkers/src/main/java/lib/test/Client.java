package lib.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;



public class Client implements Runnable {

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
    public void run() {
        Client client = new Client("localhost", 55555);
        Scanner input = new Scanner(System.in);

        Listener listener = new Listener(client.in);
        Thread listenerThread = new Thread(listener);
        listenerThread.start();

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
