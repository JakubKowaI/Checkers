import java.io.PrintWriter;
import java.util.Scanner;

public class Speaker implements Runnable {
    private Scanner in;
    private PrintWriter out;

    public Speaker(Scanner in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        while (in.hasNextLine()) {
            out.println(in.nextLine());
        }
    }
    
}
