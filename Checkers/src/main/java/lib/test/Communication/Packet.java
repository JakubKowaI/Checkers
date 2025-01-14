package lib.test.Communication;

import java.io.Serializable;

public class Packet implements Serializable {
    private static final long serialVersionUID = 1L;

    public String command;
    public String message;
    public char[][] board = null;

    public Packet(String command, String message) {
        this.command = command;
        this.message = message;
    }

    public Packet(char[][] board) {
        this.board = board;
    }

    public Packet(int startX, int startY, int endX, int endY) {
    }
}
