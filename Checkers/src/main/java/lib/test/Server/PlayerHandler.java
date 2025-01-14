package lib.test.Server;

import lib.test.Communication.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerHandler extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private static int playerCount = 0;
    private int playerNumber;
    private Board board;

    public PlayerHandler(Socket accept, Board board) {
        this.socket = accept;
        playerNumber = playerCount++;
        this.board = board;
    }

    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String inputLine;
        while (true) {
            Packet packet;
            try {
                packet = (Packet) in.readObject();
            } catch (IOException e) {
                //throw new RuntimeException(e);
                continue;
            } catch (ClassNotFoundException e) {
                //throw new RuntimeException(e);
                continue;
            }
            //if(false)packet = null;
            if(packet.command.equals("QUIT")) {
                break;
            }else if(packet.command.equals("SAY")) {
                board.broadcast(packet);
            } else if (packet.command.equals("GET_BOARD")) {
                try {
                    out.writeObject(new Packet(board.getBoard()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(Packet packet) {
        try {
            out.writeObject(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
