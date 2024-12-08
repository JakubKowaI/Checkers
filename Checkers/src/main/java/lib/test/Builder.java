package lib.test;

import java.util.Scanner;

public class Builder {
    public static void main(String[] args) {
        System.out.println("Wybierz aplikacje do zbudowania:\n1. Server\n2. Client");
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        switch (choice) {
            case 1:
                new Server().run();
                break;
            case 2:
                new Client().run();
                break;
            default:
                System.out.println("Niepoprawny wybór");
                break;
        }
        input.close();
    }
    
}
