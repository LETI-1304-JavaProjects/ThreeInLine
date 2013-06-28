package miniproject;
import java.awt.Color;
import javax.swing.*;

/**
 * Основной игровой элемент - клетка поля.
 */
public class Element extends JLabel {
    private int type;
    private Position pos;
    private boolean isDel;
    
    public Element (Position p, int type) {
        super(new ImageIcon(String.valueOf(type)+".png"));
        pos = p;
        this.type = type;
        setOpaque(true);
        setBackground(new Color(70,68,81));
        setSize(20, 20);
        setBorder(BorderFactory.createEtchedBorder(Color.gray, Color.lightGray));
    }
         
    // Удаление клетки с поля.
    public void setDel(boolean i) {
        isDel = i;
    }
    
    // Была ли клетка удалена.
    public boolean IsDel () {
        return isDel;
    }
    
    // Получаем позицию на игровом поле.
    public Position getPos () {
        return pos;
    }
    
    // Получаем тип элемента.
    public int getType () {
        return type;
    }
    
    // Устанавка типа элемента.
    public void setType (int type) {
        isDel = false;
        this.type = type;
        ((Listener)getMouseListeners()[0]).setIcons();
    }
    
    // Метод сравнения.
    public boolean equals(Element obj) {
        if (obj == null)
            return false;
        if (type != obj.type)
            return false;
        if(pos.X() == obj.getPos().X() && pos.Y() == obj.getPos().Y())
            return false;
        return true;
    }
}
