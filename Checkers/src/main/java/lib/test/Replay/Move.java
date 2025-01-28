package lib.test.Replay;

public class Move {
    private int oldX;
    private int oldY;
    private int newX;
    private int newY;
    private char playerColor;

    public Move(int oldX, int oldY, int newX, int newY, char playerColor) {
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
        this.playerColor = playerColor;
    }

    public int getOldX() {
        return oldX;
    }

    public int getOldY() {
        return oldY;
    }

    public int getNewX() {
        return newX;
    }

    public int getNewY() {
        return newY;
    }

    public char getPlayerColor() {
        return playerColor;
    }
}
