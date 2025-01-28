package lib.test.Server;

import lib.test.Communication.Packet;

public class Validator {
    private char[][] boardState;
    char[][] possibleMoves;


    private void calculatePossibleMoves(int X, int Y) {
        possibleMoves = new char[17][25];

        try{
            if(boardState[Y+1][X+1] == 'p'){
                possibleMoves[Y+1][X+1] = 'x';
            } else if (boardState[Y+1][X+1] == ' '){
            }else{
                if(boardState[Y+2][X+2] == 'p'){
                    possibleMoves[Y+2][X+2] = 'x';
                }
            }
        }catch (Exception e){
            //e.printStackTrace();
        }
        try{
            if(boardState[Y+1][X-1] == 'p'){
                possibleMoves[Y+1][X-1] = 'x';
            } else if (boardState[Y+1][X-1] == ' '){
            }else{
                if(boardState[Y+2][X-2] == 'p'){
                    possibleMoves[Y+2][X-2] = 'x';
                }
            }
        }catch (Exception e){
            //e.printStackTrace();
        }
        try{
            if(boardState[Y-1][X+1] == 'p'){
                possibleMoves[Y-1][X+1] = 'x';
            } else if (boardState[Y-1][X+1] == ' '){
            }else{
                if(boardState[Y-2][X+2] == 'p'){
                    possibleMoves[Y-2][X+2] = 'x';
                }
            }
        }catch (Exception e){
            //e.printStackTrace();
        }
        try{
            if(boardState[Y-1][X-1] == 'p'){
                possibleMoves[Y-1][X-1] = 'x';
            } else if (boardState[Y-1][X-1] == ' '){
            }else{
                if(boardState[Y-2][X-2] == 'p'){
                    possibleMoves[Y-2][X-2] = 'x';
                }
            }
        }catch (Exception e){
            //e.printStackTrace();
        }

    }

    private boolean hasMoves(int lastX, int lastY) {
        for(int i = 0; i < 17; i++) {
            for(int j = 0; j < 25; j++) {
                if(possibleMoves[i][j] == 'x'&& (i != lastY || j != lastX)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasPossibleMoves(Packet packet, char[][] state,int lastX, int lastY) {
        this.boardState = state;
        calculatePossibleMoves(packet.oldX, packet.oldY);
        return hasMoves(lastX, lastY);
    }

    private boolean isCloseEnough(int oldX, int oldY, int newX, int newY) {
        return Math.abs(oldX - newX) <= 2 && Math.abs(oldY - newY) <= 2;
    }

    public boolean isMoveValid(Packet packet, char[][] state) {
        this.boardState = state;
        if(!isCloseEnough(packet.oldX, packet.oldY, packet.newX, packet.newY)) {
            return false;
        }
        if(boardState[packet.newY][packet.newX] != 'p') {
            return false;
        }
        calculatePossibleMoves(packet.oldX, packet.oldY);
        return possibleMoves[packet.newY][packet.newX] == 'x';
    }
}