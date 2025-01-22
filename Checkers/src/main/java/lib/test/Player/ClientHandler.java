package lib.test.Player;

import javafx.application.Platform;
import lib.test.Communication.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream outa;
    private boolean running = true;
    private Client client;
    private final List<Callable<Void>> onWinActions = new ArrayList<>();

    public ClientHandler(String address, int port, Client client) {
        try {
            socket = new Socket(address, port);
            in = new ObjectInputStream(socket.getInputStream());
            outa = new ObjectOutputStream(socket.getOutputStream());
            this.client = client;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addWinAction(Callable<Void> action) {
        onWinActions.add(action);
    }

    public void executeOnWinActions() {
        for (Callable<Void> action : onWinActions) {
            try {
                action.call(); // Execute the callable
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void say(String message) {
        try {
            if (socket != null && !socket.isClosed()) {
                outa.writeObject(new Packet("SAY", message));
                outa.flush(); // Ensure the packet is sent immediately
            } else {
                if(socket == null) {
                    System.out.println("Socket is null, cannot send message.");
                } else
                System.out.println("Socket is closed, cannot send message.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void move(int x, int y, int x2, int y2) {
        try {
            outa.reset();
            outa.writeObject(new Packet("MOVE", x, y, x2, y2));
            outa.flush(); // Ensure the packet is sent immediately
            outa.reset();
            //getBoard();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Socket is closed, cannot move.");
        }
    }
    public void giveTurn() {
        try {
            outa.reset();
            outa.writeObject(new Packet("GIVE_TURN", ""));
            outa.flush(); // Ensure the packet is sent immediately
            outa.reset();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Socket is closed, cannot give turn.");
        }
    }

    public void getBoard() {
        try {
            System.out.println("Getting board");
            outa.writeObject(new Packet("GET_BOARD", ""));
            outa.flush(); // Ensure the packet is sent immediately
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Socket is closed, cannot get board.");
        }
    }

    public void printBoard(char[][] board){
        for(int i = 0; i < 17; i++){
            for(int j = 0; j < 25; j++){
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    private void processObject(Object object) {
        if (object instanceof Packet) {
            Packet packet = (Packet) object;
            Platform.runLater(() -> {
                if (packet.board != null) {
                    System.out.println("Otrzymano zaktualizowaną planszę:");
                    //printBoard(packet.board);
                    client.refreshBoard(packet.board);
                } else {
                    if (packet.command.equals("SAY")) {
                        client.displayMessage(packet.message);
                    } else if (packet.command.equals("ASSIGN_COLOR")) {
                        client.setPlayerColor(packet.color);
                    } else if (packet.command.equals("INVALID_MOVE")) {
                        client.displayMessage(packet.message);
                    } else if (packet.command.equals("NOT_YOUR_TURN")) {
                        client.displayMessage(packet.message);
                    } else if (packet.command.equals("YOU_WON")) {
                        client.displayMessage(packet.message);
                        executeOnWinActions();
                    } else if (packet.command.equals("TURN")) {
                        client.displayMessage(packet.message);
                    }
                }
            });
        }
    }


    public void stop() {
        running = false;
        try {
            if (socket != null) socket.close();
            if (in != null) in.close();
            if (outa != null) outa.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (running) {
                Object receivedObject = in.readObject();
                if (receivedObject != null) {
                    processObject(receivedObject);
                }
            }
        } catch (SocketException e) {
            System.out.println("Connection closed or error occurred: " + e.getMessage());
            closeConnection();
        } catch (IOException e) {
            System.out.println("IO Connection closed or error occurred: " + e.getMessage());
            closeConnection();
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
            closeConnection();
        } finally {
            stop();
        }


    }

    private void closeConnection() {
        try {
            running = false;
            if (in != null) in.close();
            if (outa != null) outa.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}