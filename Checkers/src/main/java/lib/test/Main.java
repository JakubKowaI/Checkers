package lib.test;

import lib.test.Player.Client;
import lib.test.Server.BoardBuilder;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Wybierz aplikacje do zbudowania:\n1. Server\n2. Client");
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
            default:
                System.out.println("Niepoprawny wybór");
                break;
        }
        input.close();
    }
}