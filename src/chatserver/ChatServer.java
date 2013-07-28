/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JosepOriol
 */
public class ChatServer {

    /**
     * @param args the command line arguments
     */
    private ServerSocket server;
    private List<Socket> clients;
    private static final int port = 2000;
    private boolean run = true;

    public ChatServer() throws IOException {
        server = new ServerSocket(port);
        clients = new ArrayList<>();
    }

    public static void main(String[] args) {
        try {
            new ChatServer().start();
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void start() throws IOException {
        Calendar c = Calendar.getInstance();
        Socket s;
        while (run) {
            s = server.accept();
            new Thread(new Writer(s, clients)).start();
            System.out.println("["+c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+"] Client with IP " + s.getInetAddress().getHostAddress() + ":" + s.getPort() + " added.");
        }
    }
}
