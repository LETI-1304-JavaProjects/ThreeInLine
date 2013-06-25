package course_work;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Интерфейс игрока. Формирует на экране область данных, 
 * связанные с игроком (изображение, имя и уровень здоровья).
 */
public class PlayerGUI extends JPanel {
        private JLabel nameLabel;
        private JProgressBar healthPointsBar;
        
        // Возвращает ссылку на индикатор здоровья.
        public JProgressBar health () {
            return healthPointsBar;
        } 
        
        // Задает имя игрока.
        public void setPlayerName (String name) {
            nameLabel.setText(name);
        } 
        
        // Получить введенное имя.
        public String getname () {
            return nameLabel.getText();
        }
        
      PlayerGUI(int PlayerNum){
	          ImageIcon avatarIcon = new ImageIcon("Player"+String.valueOf(PlayerNum)+".jpg");
            
            JLabel avatar = new JLabel(avatarIcon);
            avatar.setBorder(new LineBorder(new Color(169, 169, 169)));
            avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
            avatar.setPreferredSize(new Dimension(190, 190));
            
	          setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	       	        
            nameLabel = new JLabel("");
            nameLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            nameLabel.setPreferredSize(new Dimension(200, 30));
            nameLabel.setMinimumSize(new Dimension(200, 30));
            nameLabel.setMaximumSize(new Dimension(200, 30));
            nameLabel.setForeground(Color.white);
            
            setBorder(new LineBorder(new Color(169, 169, 169)));
	          setPreferredSize(new Dimension(200, 500));
	          setBackground(new Color(50,50,50));   
            add(nameLabel);
	          add(avatar);
	       	        
            healthPointsBar = new JProgressBar();
            healthPointsBar.setStringPainted(true);
            healthPointsBar.setBorder(new LineBorder(new Color(0, 0, 0)));
            healthPointsBar.setValue(50);
            healthPointsBar.setForeground(new Color(178, 34, 34));
            healthPointsBar.setMinimumSize(new Dimension(190, 30));
            healthPointsBar.setMaximumSize(new Dimension(190, 30));
            healthPointsBar.setPreferredSize(new Dimension(190, 30));
            healthPointsBar.setBackground(new Color(120, 22, 22));
            
            add(healthPointsBar);
            JLabel EmptyLabel = new JLabel("");
            EmptyLabel.setPreferredSize(new Dimension(190, 180));
            add(EmptyLabel);
            setSize(800, 500);
	}
}
