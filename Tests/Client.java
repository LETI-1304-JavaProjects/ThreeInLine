/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servertest;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author WINDOWS 7
 */
public class Client extends SimpleClient {
    private JTextField textinput;
    private JTextField textoutput;
    private JButton send;
    private boolean isAlone;
    private int twoClId;
    private JFrame f;
    public Client () {
        isAlone = false;
        twoClId = -1;
        JLabel input = new JLabel("Input:    "), output = new JLabel("Output:");
        textinput = new JTextField();
        textinput.setPreferredSize(new Dimension(450, 50));
        textoutput = new JTextField();
        textoutput.setPreferredSize(new Dimension(450, 50));
        JButton send = new JButton("send message");
        textoutput.setEditable(false);
        send.setPreferredSize(new Dimension(150, 50));
        f = new JFrame();
        f.setLayout(new FlowLayout());
        f.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) { }
            @Override
            public void windowIconified(WindowEvent e) { }
            @Override
            public void windowDeiconified(WindowEvent e) { }
            @Override
            public void windowDeactivated(WindowEvent e) { }
            @Override
            public void windowClosed(WindowEvent e) { }
            @Override
            public void windowActivated(WindowEvent e) { }
            @Override
            public void windowClosing(WindowEvent event) {
                if(isAlone) {
                    System.exit(0);
                }
                else {
                    try {
                        sendAll("~");
                        f.setVisible(false);
                    } catch ( ClientException | ServerException ex) {
                        System.out.println(((ServerException)ex).what());
                    }
                }
                }  
        });
        f.add(input);
        f.add(textinput);
        f.add(output);
        f.add(textoutput);
        f.add(send);
        
        send.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendAll(textinput.getText());
                    textoutput.setText("");
                } catch (ClientException | ServerException ex) {
                    System.out.println(((ServerException)ex).what());
                }
            }
        });
        
        f.setSize(530, 200);
        f.setVisible(true);
    }
    
    @Override
    public void getMessage(String message) {
        if (message.charAt(0) == '~') {
            isAlone = true;
        }
        else {
            textinput.setText("");
            textoutput.setText(message);
        }
    }
}
