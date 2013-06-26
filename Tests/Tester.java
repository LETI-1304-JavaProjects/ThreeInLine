/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tester;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.LineBorder;

/* Формат сообщения: код данные
 * 
 * Коды сообщений:
 * 0 - запрос номера игрока
 * 1 - ответ с номером игрока
 * 2 - противник сделал ход
 * 3 - копирование поля противника
 * 4 - обновление индикаторов здоровья
 * 5 - задать имя
 * 6 - противник закрыл игровое окно
 * 7 - запрос о начале игры
 * 8 - ответ о начале игры
 * 9 - конец игры
 */


/**
 * Основной класс, наследник клиента. Реализует абстрактный метод getMessage.
 * Формирует окно игрока и выполняет синхронизацию действий через сервер.
 */
public class Tester {
    private Element l [][], currFigure;
    private JPanel fieldJPanel, backgroundPanel;
    private Vector cells, panel;
    private JFrame gameFrame;

    // Создает окно с сообщением s.
    private void errorWindow (String s) {
        JOptionPane.showMessageDialog(gameFrame, s);
    }
    
    public Tester(){
        super();
        panel = new Vector();
        currFigure = null; 
        cells = new Vector();
        l = new Element[11][11];        
        
  gameFrame = new JFrame();
	
	fieldJPanel = new JPanel();
	fieldJPanel.setPreferredSize(new Dimension(500,500));
	fieldJPanel.setBackground(new Color(100,100,100));
        initFieldPanel (1);
        
	backgroundPanel = new JPanel();
	backgroundPanel.setSize(1000,600);
	backgroundPanel.setBackground(new Color(0,0,0));
	backgroundPanel.add(fieldJPanel);
        
        JButton exitButton = new JButton("Выйти из игры");
        exitButton.setBorder(new LineBorder(new Color(169, 169, 169)));
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.setBackground(new Color(95, 158, 160));
        exitButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setMinimumSize(new Dimension(140, 40));
        exitButton.setMaximumSize(new Dimension(140, 40));
        exitButton.setPreferredSize(new Dimension(190, 40));
        exitButton.setForeground(Color.white);
        exitButton.addActionListener(new ActionListener() {
            @Override // обработчик кнопки выхода из битвы
            public void actionPerformed(ActionEvent e) {
                // покидаем игру
                System.exit(0);
            }
        });
        
	backgroundPanel.add(exitButton);
        
	gameFrame.setSize(600,600);
	gameFrame.add(backgroundPanel);
        gameFrame.setResizable(false);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	gameFrame.setVisible(true);
    }

    // Создает игровое поле в центре окна, если options = 0.
    // Создает начальное меню в центре окна, если options = 1.
    private void initFieldPanel (int options) {
            fieldJPanel.removeAll();
            fieldJPanel.setLayout(new GridLayout(9,9));
            for(int i=0; i<l.length; i++)  {                     
                for(int j=0, k; j<l.length; j++) {
                    k = (int)(Math.random()*1000%6+1);    
                    l[i][j]=new Element(new Position(i, j), k); 
                    l[i][j].addMouseListener(new Listener(l[i][j],this));
                    if(i<9 && j<9) {
                        fieldJPanel.add(l[i][j]);
                    }
                } 
            }
            repopulate();

    }
          
    // Обрабатывает ход и передает право хода противнику.
    private void TwoFiguresChanged(Element f1, Element f2) {
            // если ход правильный, меняем местами клетки
            if(isValidMove(f1.getPos().X(),f1.getPos().Y(),f2.getPos().X(),f2.getPos().Y())) {
                int b = f1.getType();
                f1.setType(f2.getType());
                f2.setType(b);
            }
            // иначе ничего не делаем
            else {
                currFigure = null;
                return;
            }
            // происходит переформирование поля
            // в val будут набранные очки
            while (delEliminateMatches()) {
                fallDown();
                refillHoles();
            }
            gameFrame.repaint();
            currFigure = null;
    }
  
    // Если игрок делал ход, заполняет пустые места рандомными клетками.
    private void refillHoles() {
        for (int i = 0; i < l.length; i++) {
            for (int j = 0; j < l[0].length; j++) {
                if (l[i][j].IsDel()) {
                    l[i][j].setType((int)(Math.random()*1000%6+1));
                }
            }
        }
    }
    
    // Вызывается обработчиком событий мыши, если была выбрана клетка.
    // Если выбранная клетка вторая по счету, вызывает функцию обработки хода
    // (TwoFiguresChanged).
    public void OneFigureChanged (Element f) {
        if(f==null) {
            currFigure=null;
            return;
        }
        if(currFigure==null) {
            currFigure = f;
            f.setBorder(BorderFactory.createLineBorder( new Color(0,255,0), 3));
        }
        else {
            currFigure.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
            TwoFiguresChanged (currFigure, f);
        }
    }
    
    // Находит ряды по 3 и удаляет их.
    // Информацию о удаленных клетках и нанесенном повреждении противнику 
    // заносит в переменную buff.
    // Возвращает 0, если не было удалено ни одной клетки.
    // 1 в противном случае.
    // Формат буфера: повреждение x1 y1 type1 x2 y2 type2 .......
    private boolean delEliminateMatches() {
	boolean matchOccured = false;
	for (int i = 0; i < l.length - 2; i++) {
	    for (int j = 0;j < l[0].length - 2; j++) {
		if (l[i][j].equals(l[i + 1][j])
                    && l[i + 1][j].equals(l[i + 2][j]) && !l[i][j].IsDel()) {
                    l[i][j].setDel(true);
                    l[i+1][j].setDel(true);
                    l[i+2][j].setDel(true);
		    matchOccured = true;
		}
		if (l[i][j].equals(l[i][j + 1])
                    && l[i][j + 1].equals(l[i][j + 2]) && !l[i][j].IsDel()) {
                    l[i][j].setDel(true);
                    l[i][j+1].setDel(true);
                    l[i][j+2].setDel(true);
		    matchOccured = true;
		}
	    }
	}
	return matchOccured;
    }
    
    // Находит ряды по 3 и заменяет в них клетки на рандомные.
    // Возвращает 0, если не было произведено ни одной замены.
    // 1 в противном случае.
    private boolean eliminateMatches() {
	boolean matchOccured = false;
	Set<Position> matches = new HashSet<>();
	for (int i = 0; i < l.length - 2; i++) {
	    for (int j = 0; j < l[0].length - 2; j++) {
		if (l[i][j].equals(l[i + 1][j])
                    && l[i + 1][j].equals(l[i + 2][j])) {
		    matches.add(new Position(i, j));
		    matches.add(new Position(i + 1, j));
		    matches.add(new Position(i + 2, j));
		    matchOccured = true;
		}
		if (l[i][j].equals(l[i][j + 1])
                    && l[i][j + 1].equals(l[i][j + 2])) {
		    matches.add(new Position(i, j));
		    matches.add(new Position(i, j + 1));
		    matches.add(new Position(i, j + 2));
		    matchOccured = true;
		}
	    }
	}
	for (Position coords : matches) {
            l[coords.X()][coords.Y()].setType((int)(Math.random()*1000%6+1));
        }
	return matchOccured;
    }
    
    private boolean gridHasValidMoves() {
	for (int i = 0; i < l.length; i++) {
            for (int j = 0; j < l[0].length; j++) {
                if (isValidMove(i, j, i + 1, j) || isValidMove(i, j, i, j + 1)) {
                    return true;
                }
            }
        }
	return false;
    }
    
    private boolean hasSequenceForCoords(Element cell, int x, int y) {
	int horzSequence = 1; 
	if (x >= 1) {
            if (cell.equals(l[x - 1][y])) {
                horzSequence++;
                if (x >= 2 && cell.equals(l[x - 2][y])) {
                    horzSequence++;
                }
            }
        }
	if (x < 8) {
            if (cell.equals(l[x + 1][y])) {
                horzSequence++;
                if (x < 7 && cell.equals(l[x + 2][y])) {
                    horzSequence++;
                }
            }
        }
	if (horzSequence >= 3) {
            return true;
        }
        
	int vertSequence = 1;
	if (y >= 1) {
            if (cell.equals(l[x][y - 1])) {
                vertSequence++;
                if (y >= 2 && cell.equals(l[x][y - 2])) {
                    vertSequence++;
                }
            }
        }
	if (y < 8) {
            if (cell.equals(l[x][y + 1])) {
                vertSequence++;
                if (y < 7 && cell.equals(l[x][y + 2])) {
                    vertSequence++;
                }
            }
        }
	if (vertSequence >= 3) {
            return true;
        }
        
	return false;
    }
    
    private boolean isValidMove(int x1, int y1, int x2, int y2) {
	if (Math.abs(x1 - x2) + Math.abs(y1 - y2) > 1) {
            return false;
        }
	if (x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0) {
            return false;
        }
	if (x1 >= l.length || x2 >= l.length || y1 >= l[0].length || y2 >= l[0].length) {
            return false;
        }
	return hasSequenceForCoords(l[x1][y1], x2, y2) || hasSequenceForCoords(l[x2][y2], x1, y1);
    }
    
    // Генерирует корректное поле (с ходом и без рядов по 3).
    private void repopulate() {
            do {
                while (eliminateMatches()) {}
            } 
            while (!gridHasValidMoves());
    }
    
    // Перемещает клетки вниз после удаления ряда.
    private void fallDown() {
	for (int j = 0; j < l[0].length; j++) {
	    for (int i = l.length - 1; i >= 0; i--) {
		if (l[i][j].IsDel()) {
		    for (int i2 = i; i2 >= 0; i2--) {
			if (!l[i2][j].IsDel()) {
			    l[i][j].setType(l[i2][j].getType());
			    l[i2][j].setDel(true);
                            i = l.length - 1;
			    break;
			}
		    }
		}
	    }
	}
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(new Runnable () {
            public void run () {
                new Tester();
            }
        });
    }
}
