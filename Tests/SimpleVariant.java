/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplevariant;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author WINDOWS 7
 */
public class SimpleVariant {
    public static void main(String[] args) {
     SwingUtilities.invokeLater(new Runnable () {
        public void run () {
            LocalServer s = new LocalServer();
            Launcher l1 = new Launcher(), l2 = new Launcher(); 
             try {
                 l1.connect(s);
                 l2.connect(s);
                 l1.start();
                 l2.start();
             } 
             catch (ClientException | ServerException ex) {
                 System.out.print(ex);
             }
        }
     });
    }
}
