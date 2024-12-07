import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    public Client(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

            // Odczytuj powitalne wiadomości od serwera
            while (in.hasNextLine()) {
                System.out.println(in.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void listenToServer() {
        while (in.hasNextLine()) {
            System.out.println(in.nextLine());
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 55555);
        Scanner scanner = new Scanner(System.in);

        // Obsługa komunikacji z serwerem
        Thread listener = new Thread(client::listenToServer);
        listener.start();

        while (scanner.hasNextLine()) {
            String message = scanner.nextLine();
            client.sendMessage(message);
        }
    }
}
