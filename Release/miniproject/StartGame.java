package miniproject;

import miniproject.client_server.exceptions.ClientException;
import miniproject.client_server.exceptions.ServerException;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Стартовое меню игры.
 */
public class StartGame extends JPanel {
  
    private JButton StartButton;
    private JButton ExitButton;
    private JButton Connect;
    private JTextField PlayerName;
    private Launcher obj;
    private JLabel connectLabel;
    private boolean isConnected;
    
    // Получить введенное имя.
    public String getname () {
        return PlayerName.getText();
    }
    
    // Проверка подключения.
    public boolean IsConnected () {
        return isConnected;
    }
    
    protected StartGame(Launcher perrent){
        super();
        isConnected = false;
        obj = perrent;
        connectLabel = new JLabel("           Соединение не установлено...");
        connectLabel.setForeground(Color.white);
	connectLabel.setBackground(new Color(50,50,50)); 
        connectLabel.setBackground(new Color(100,100,100));
        connectLabel.setOpaque(true);
        
        setLayout(new GridLayout(9,1,5,5));

        JLabel emptyLabel1 = new JLabel();
        JLabel emptyLabel2 = new JLabel();
        emptyLabel1.setSize(520,60);
        emptyLabel2.setSize(520,60);
        
        add(emptyLabel1);
        add(new JLabel("Имя игрока:"));
        add(PlayerName = new JTextField("player"+Math.round(Math.random()*10)));
        add(Connect = new JButton("Подключиться"));
        add(StartButton = new JButton("Новая игра"));
        add(ExitButton = new JButton("Выход"));
        add(connectLabel);
        add(emptyLabel2);
        add(emptyLabel2);

        StartButton.setEnabled(true);
        ExitButton.setEnabled(true);

        setBorder(new LineBorder(new Color(255, 213, 213)));
	setBackground(new Color(187,187,187)); 
        setVisible(true);

        StartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                obj.start();
            }
	});
		
        // обработчик кнопки выхода
        ExitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(obj.isAlone()) {
                    Runtime.getRuntime().exit(0);
                }
                try {
                    obj.send("6 ", obj.Id());
                    obj.setVisible(false);
                } 
                catch (ClientException ex) {
                    Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
                } 
                catch (ServerException ex) {
                    Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
	});
        
        // обработчик кнопки подключения
        Connect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(isConnected) return;
                    if(obj.isConnected()) {
                        connectLabel.setText("             Соединение установлено...");
                        isConnected = true;
                    }
                    else {
                        connectLabel.setText("          Соединение не установлено...");
                        isConnected = false;
                    }
            }
	});
    }
}
