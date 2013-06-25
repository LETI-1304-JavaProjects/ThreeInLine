package tester;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

/**
 * Обработчик событий мыши.
 * Работает с одной клеткой.
 * Изменяет внешний вид клетки на игровом поле.
 * Если был сделан ход, вызывает функцию обработки хода в классе Launcher.
 */
public class Listener implements MouseListener {
    private ImageIcon icon1, icon2;   
    private Element figure;      
    private Tester obj;   
    
    public Listener (Element f, Tester perrent) {
        figure = f;
        icon1 = (ImageIcon) f.getIcon();
        icon2 = new ImageIcon(String.valueOf(f.getType())+"1.png");
        obj = perrent;
    }
    
    // Устанавливает для фигуры соответствующие ее типу изображения.
    // (обычное и более яркое, оторбажаемое при наведении мыши).
    public void setIcons () {
        icon1 = new ImageIcon(String.valueOf(figure.getType())+".png");
        figure.setIcon(icon1);
        icon2 = new ImageIcon(String.valueOf(figure.getType())+"1.png");
    }
    
    public void mouseClicked(MouseEvent e){ 
    
    }

    public void mousePressed(MouseEvent e){ 
    
    }
    
    // Сообщает основному игровому классу Launcher о том, 
    // что была выбрана одна из клеток.
    // Выполняется лишь тогда, когда игрок имеет право на ход.
    public void mouseReleased(MouseEvent e){
        obj.OneFigureChanged(figure);
    }
   
    // Устанавливает яркое изображение.
    // Вызывается при наведении мыши.
    // Выполняется лишь тогда, когда игрок имеет право на ход.
    public void mouseEntered(MouseEvent e){
        figure.setIcon(icon2);
    }
    
    // Устанавливает более темное изображение.
    // Вызывается при выходе мыши из области объекта.
    // Выполняется лишь тогда, когда игрок имеет право на ход.
    public void mouseExited(MouseEvent e) {
        figure.setIcon(icon1);
    }
}
