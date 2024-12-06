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

    //Metoda wysylajaca wiadomosc do serwera
    public void tellServer(String message) {
        outa.println("SAY " + message);
    }
    public static void main(String[] args) {
        Client client = new Client("localhost", 55555);
        Scanner input = new Scanner(System.in);

        while(input.hasNextLine()){
            //Sczytywanie wiadomosci od gracza
            String message = input.nextLine();
            //Wysylanie wiadomosci do serwera
            client.tellServer(message);
            //Wypisywanie odpowiedzi serwera (tylko chyba się bufferuje i na raz nie wypisuje wiecej niż jednej wiadomości)
            if(client.in.hasNextLine()){
                System.out.println(client.in.nextLine());
            }
        }
        try{
        client.socket.close();
        input.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }   
}
