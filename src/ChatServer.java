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
    private ServerSocket serverText;
    private ServerSocket serverSound;
    private List<ClientText> clients;
    private List<SoundClient> clientsVeu;
    private static final int portText = 2000;
    private static final int portSound = 2001;
    private boolean run = true;

    public ChatServer() throws IOException {
        serverText = new ServerSocket(portText);
        serverSound = new ServerSocket(portSound);

        clients = new ArrayList<ClientText>();
        clientsVeu = new ArrayList<SoundClient>();
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
        Socket socketText,socketSound;
        while (run) {
            socketText = serverText.accept();
            socketSound = serverSound.accept();
            SoundClient sc = new SoundClient(socketSound, clientsVeu);
            sc.start();
            new ClientText(socketText, sc, clients).start();
            System.out.println("["+c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+"] Client with IP " + socketText.getInetAddress().getHostAddress() + ":" + socketText.getPort() + " added.");
        }
    }
}
