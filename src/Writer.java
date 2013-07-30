import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JosepOriol
 */
public class Writer implements Runnable {

    private DataInputStream in;
    private Socket socket;
    private List<Socket> llista;
    private final String end;

    public Writer(Socket socket, List<Socket> llista) throws IOException {
        this.socket = socket;
        this.llista = llista;
        llista.add(socket);
        this.in = new DataInputStream(socket.getInputStream());
        end = "bye";
    }

    @Override
    public void run() {
        String line;
        boolean stop = false;
        try {
            while (!stop) {
                line = in.readUTF();
                System.out.println(socket.getPort() + "--> " + line);
                stop = line.equalsIgnoreCase(end);
                if (!stop) {
                    bcst(line);
                } else {
                    System.out.println("Connection closed at client " + socket.getPort());
                    llista.remove(socket);
                    in.close();
                    socket.close();
                }
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
