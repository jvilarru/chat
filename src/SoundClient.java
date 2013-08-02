
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author JosepOriol
 */
public class SoundClient implements Runnable {

    private InputStream in;
    private OutputStream out;
    private Socket socket;
    private List<SoundClient> llista;
    private boolean running;
    private byte buffer[];

    public SoundClient(Socket socketVeu, List<SoundClient> llista) throws IOException {
        this.llista = llista;
        this.socket = socketVeu;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        running = false;
        buffer = new byte[4096];
    }

    public void start() {
        llista.add(this);
        new Thread(this).start();
    }

    public void para() {
        running = false;
    }

    @Override
    public void run() {
        int count;
        running = true;
        try {
            while (running) {
                count = in.read(buffer);
                bcst(buffer, count);
            }
            in.close();
            out.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(SoundClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void bcst(byte[] buffer,int len) throws IOException {
        for (SoundClient c : llista) {
            if (!c.equals(this)) {
                c.send(buffer,len);
            }
        }
    }

    public void send(byte[] buffer,int len) throws IOException {
        out.write(buffer,0,len);
        out.flush();
    }
}
