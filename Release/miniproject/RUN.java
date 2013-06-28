/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package miniproject;

import miniproject.client_server.LocalServer;
import miniproject.client_server.exceptions.ClientException;
import miniproject.client_server.exceptions.ServerException;
import javax.swing.SwingUtilities;

/**
 *
 * @author WINDOWS 7
 */
public class RUN {
    public static void main(String[] args) {
     SwingUtilities.invokeLater(new Runnable () {
        public void run () {
            LocalServer s = new LocalServer();
            Launcher l1 = new Launcher(), l2 = new Launcher(); 
             try {
                 l1.connect(s);
                 l2.connect(s);
             } 
             catch (ClientException | ServerException ex) {
                 System.out.print(ex);
             }
        }
     });
    }
}
