package lib.test.Server;

import lib.test.Communication.Packet;

import java.net.ServerSocket;

public class Board {
    ServerSocket listener;
    PlayerHandler[] playerHandler;
    char board[][] = new char[17][25];
    int playerCount;


    public void fillFromTop(int y,int x){
        if(y==13)return;
        if(board[y][x]==' '){
            board[y][x]='p';
        }
        fillFromTop(y+1,x-1);
        fillFromTop(y+1,x+1);
    }

    public void fillFromBottom(int y,int x){
        if(y==3)return;
        if(board[y][x]==' '){
            board[y][x]='p';
        }
        fillFromBottom(y-1,x-1);
        fillFromBottom(y-1,x+1);
    }

    public void fillBoard() {
        fillFromTop(0,12);
        fillFromBottom(16,12);
        switch (playerCount) {
            case 2:
                fillTriangleTop(0,12,0+4,'b');
                fillTriangleBottom(16,12,16-4,'r');
                break;
            case 3:
                fillTriangleTop(0,12,0+4,'r');
                fillTriangleTop(9,3,9+4,'g');
                fillTriangleTop(9,21,9+4,'b');
                break;
            case 4:
                fillTriangleTop(9,3,9+4,'g');
                fillTriangleTop(9,21,9+4,'b');
                fillTriangleBottom(7,3,7-4,'r');
                fillTriangleBottom(7,21,7-4,'y');
                break;
            case 6:
                fillTriangleTop(0,12,0+4,'r');
                fillTriangleBottom(16,12,16-4,'g');
                fillTriangleTop(9,3,9+4,'b');
                fillTriangleTop(9,21,9+4,'y');
                fillTriangleBottom(7,3,7-4,'o');
                fillTriangleBottom(7,21,7-4,'v');
                break;
            default:
                break;
        }
    }

    private void fillTriangleBottom(int y, int x, int end,char fill) {
        if(y==end)return;
        if(board[y][x]=='p'){
            board[y][x]=fill;
        }
        fillTriangleBottom(y-1,x-1,end,fill);
        fillTriangleBottom(y-1,x+1,end,fill);
    }

    private void fillTriangleTop(int y,int x,int end,char fill){
        if(y==end)return;
        if(board[y][x]=='p'){
            board[y][x]=fill;
        }
        fillTriangleTop(y+1,x-1,end,fill);
        fillTriangleTop(y+1,x+1,end,fill);
    }

    public void resetBoard() {
        for(int i=0;i<17;i++){
            for(int j=0;j<25;j++){
                board[i][j]=' ';
            }
        }
    }

    public Board(int port, int playerCount) {
        resetBoard();
        this.playerCount = playerCount;
        fillBoard();

        try {
            playerHandler = new PlayerHandler[playerCount];
            listener = new ServerSocket(port);
            int i=0;
            while (i<playerCount) {
                PlayerHandler player = new PlayerHandler(listener.accept(), this);
                player.start();
                playerHandler[i] = player;
                System.out.println("Player "+(i+1)+" connected");
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public char[][] getBoard() {
        return board;
    }

    public void broadcast(Packet packet) {
        for (PlayerHandler player : playerHandler) {
            player.send(packet);
        }
    }
}
