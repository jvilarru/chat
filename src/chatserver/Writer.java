/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JosepOriol
 */
public class Writer implements Runnable {

    private BufferedReader in;
    private BufferedWriter out;
    private Socket socket;
    private List<Socket> llista;

    public Writer(Socket socket, List<Socket> llista) throws IOException {
        this.socket = socket;
        this.llista = llista;
        llista.add(socket);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void run() {
        String input;
        try {
            while ((input = in.readLine()) != null) {   
                bcst(input);
            }
        } catch (IOException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void bcst(String line) throws IOException {
        for (Socket s : llista) {
            if (!socket.equals(s)) {
                send(line, s);
            }
        }
    }

    private void send(String line, Socket s) throws IOException {
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        out.writeUTF(line);
        out.flush();
    }
}
