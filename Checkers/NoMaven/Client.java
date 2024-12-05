import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;



public class Client {

    private Socket socket;
    private Scanner in;
    private PrintWriter outa;

    public Client(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            in = new Scanner(socket.getInputStream());
            outa = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tellServer(String message) {
        outa.println("SAY " + message);
    }
    public static void main(String[] args) {
        Client client = new Client("localhost", 55555);
        Scanner input = new Scanner(System.in);
        while(input.hasNextLine()){
            String message = input.nextLine();
            client.tellServer(message);
        }
        try{
        client.socket.close();
        input.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }   
}
