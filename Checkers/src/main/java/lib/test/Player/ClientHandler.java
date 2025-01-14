package lib.test.Player;

import javafx.application.Platform;
import lib.test.Communication.Packet;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream outa;
    private boolean running = true;
    private Client client;

    public ClientHandler(String address, int port, Client client) {
        try {
            socket = new Socket(address, port);
            in = new ObjectInputStream(socket.getInputStream());
            outa = new ObjectOutputStream(socket.getOutputStream());
            this.client = client;
        } catch (Exception e) {
            e.printStackTrace();
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

    public void getBoard() {
        try {
            if (socket != null && !socket.isClosed()) {
                outa.writeObject(new Packet("GET_BOARD", ""));
                outa.flush(); // Ensure the packet is sent immediately
            } else {
                System.out.println("Socket is closed, cannot get board.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processObject(Object object) {
        if (object instanceof Packet) {
            Packet packet = (Packet) object;
            Platform.runLater(() -> {
                if (packet.command == null) {
                    client.refreshBoard(packet.board);
                } else {
                    if (packet.command.equals("SAY")) {
                        client.displayMessage(packet.message);
                    } else if (packet.command.equals("GET_BOARD")) {

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
        } catch (Exception e) {
            System.out.println("Connection closed or error occurred: " + e.getMessage());
        } finally {
            stop();
        }
    }
}