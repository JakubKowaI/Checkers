package lib.test.Server;

import java.net.Socket;

public class Bot extends PlayerHandler {
    private BotStrategy strategy;

    public Bot(Board board) {
        super(null, board);
    }

    public void setStrategy(BotStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(1000);
                if (board.isPlayerTurn(playerNumber)) {
                    strategy.makeMove(board, this);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
