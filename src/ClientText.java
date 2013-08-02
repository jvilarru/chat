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
public class ClientText implements Runnable {

    private DataInputStream in;
    private DataOutputStream out;
    private Socket socketText;
    private SoundClient germa;
    private List<ClientText> llista;
    private final static String end = "bye";

    public ClientText(Socket socketText,SoundClient germa, List<ClientText> llista) throws IOException {
        this.socketText = socketText;
        this.germa = germa;
        this.llista = llista;
        this.in = new DataInputStream(socketText.getInputStream());
        this.out = new DataOutputStream(socketText.getOutputStream());
    }

    @Override
    public void run() {
        try {
            llegeix();
        } catch (IOException ex) {
            Logger.getLogger(ClientText.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void bcst(String line) throws IOException {
        for (ClientText c : llista) {
            if (!c.equals(this)) {
                c.send(line);
            }
        }
    }

    public void send(String line) throws IOException {
        out.writeUTF(line);
        out.flush();
    }

    private void llegeix() throws IOException {
        boolean stop = false;
        String line;
        while (!stop) {
            line = in.readUTF();
            System.out.println(socketText.getPort() + "--> " + line);
            stop = line.equalsIgnoreCase(end);
            if (!stop) {
                bcst(line);
            } else {
                System.out.println("Connection closed at client " + socketText.getPort());
                llista.remove(this);
                in.close();
                socketText.close();
                germa.para();
            }
        }
    }

    public void start() {
        llista.add(this);
        new Thread(this).start();
    }
}
