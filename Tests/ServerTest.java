package servertest;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class ServerTest {
    private LocalServer s;
    private Thread t1, t2;
    
    public ServerTest () {
        s = new LocalServer();
        t1 = new Thread(new Runnable () {
            public void run () {
                Client c = new Client();
                try {
                    c.connect(s);
                } catch (ClientException | ServerException ex) {
                    System.out.println("Fatal Error");
                    System.exit(1);
                }
            }
        });
        t2 = new Thread(new Runnable () {
            public void run () {
                Client c = new Client();
                try {
                    c.connect(s);
                } catch (ClientException | ServerException ex) {
                    System.out.println("Fatal Error");
                    System.exit(1);
                }
            }
        });
    }
    
    public void start () {
        t1.start();
        t2.start();
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        ServerTest st = new ServerTest();
        st.start();
    }
}
