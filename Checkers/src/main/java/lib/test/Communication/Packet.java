package lib.test.Communication;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Packet implements Serializable {
    private static final long serialVersionUID = 1L;

    public String command;
    public String message;
    public char[][] board = null;
    public int oldX, oldY, newX, newY;
    public char color;

    public Packet(String command, String message) {
        this.command = command;
        this.message = message;
    }

    public Packet(String command, char color) {
        this.command = command;
        this.color = color;
    }
    //
    public Packet(String command, char[][] board) {
        this.command = command;
        this.board = board;
    }

    public Packet(char[][] board) {
        this.board = board;
    }

    public Packet(String command, int oldX, int oldY, int newX, int newY) {
        this.command = command;
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
    }

    public Packet(int startX, int startY, int endX, int endY) {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Packet packet = (Packet) obj;
        return oldX == packet.oldX &&
                oldY == packet.oldY &&
                newX == packet.newX &&
                newY == packet.newY &&
                color == packet.color &&
                Objects.equals(command, packet.command) &&
                Objects.equals(message, packet.message) &&
                Arrays.deepEquals(board, packet.board);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(command, message, oldX, oldY, newX, newY, color);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }
}

