import java.util.Scanner;

public class Listener implements Runnable {
    private Scanner in;

    public Listener(Scanner in) {
        this.in = in;
    }

    @Override
    public void run() {
        while (true) {
            if(in.hasNextLine()) System.out.println(in.nextLine());
        }
    }
    
}
