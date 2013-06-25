/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guitest;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author student
 */
public class Start {
    Start () {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GUITest g;
        g = new GUITest(1);
        g.setPlayerName("Player 1");
        f.add(g);
        f.setVisible(true);
        f.setSize(new Dimension(210, 500));
    }
        public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(new Runnable () {

            @Override
            public void run() {
                new Start();
            }
            
        });
    }
}
