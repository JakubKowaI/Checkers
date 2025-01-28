package lib.test;

import lib.test.DB.MainTable;
import lib.test.DB.UserService;
import lib.test.Player.Client;
import lib.test.Replay.ReplayClient;
import lib.test.Server.BoardBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        //UserService service = new UserService();
        //service.saveUser(new MainTable("Test", 2));
        System.out.println("Wybierz aplikacje do zbudowania:\n1. Server\n2. Client\n3. Replay");
        Scanner input = new Scanner(System.in);
        int choice = 0;
        while(true) {
            try {
                choice = input.nextInt();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        switch (choice) {
            case 1:
                new BoardBuilder("localhost", 55555);
                break;
            case 2:
                new Client().launchClient("localhost", 55555);
                break;
            case 3:
                System.out.println("Replay");
                new ReplayClient().launchClient();
                break;
            default:
                System.out.println("Niepoprawny wybór");
                break;
        }
        input.close();
    }
}