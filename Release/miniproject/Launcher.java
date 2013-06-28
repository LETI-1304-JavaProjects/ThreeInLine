package miniproject;

import miniproject.client_server.SimpleClient;
import miniproject.client_server.exceptions.ClientException;
import miniproject.client_server.exceptions.ServerException;
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
public class Launcher extends SimpleClient {
    private Element l [][], currFigure;
    private JPanel fieldJPanel, backgroundPanel;
    private Vector cells, panel;
    private StartGame startPanel;
    private JFrame gameFrame;
    private PlayerGUI player, player1, player2;
    private Integer playerNum;
    private int val;
    private boolean started, opponentStarted, IsEnable, IamAlone;
    private boolean winner;
    
    // Проверка доступности текущего хода.
    public boolean isEnable () {
        return IsEnable;
    }
    
    // Создает окно с сообщением s.
    private void errorWindow (String s) {
        JOptionPane.showMessageDialog(gameFrame, s);
    }
    
    // Начинает игру, если оба игрока готовы.
    public void start () {
        // если нет связи с сервером
        if(!startPanel.IsConnected()) {
            errorWindow("Сначала нужно подключиться, а потом убивать.");
            return; 
        }
        // если противника вообще нет
        if(IamAlone) {
            errorWindow("Все враги повержены.");
            return; 
        }
        try{
            // запрос на номер противника (кто первый вошел в игру)
            send("0 ", super.Id());
            
            // если -1, значит он еще не в игре. мы первые
            if(playerNum == -1) {
                player = player1;
                playerNum = 1;
                IsEnable = true;
            }
            // иначе мы вторые и первый ход у противника
            else {
                player = player2;
                IsEnable = false;
                playerNum = 2;
            }
            
            // создаем поле и добавляем имя игрока
            initFieldPanel(0);
            player.setPlayerName(startPanel.getname());
            send("5 " + startPanel.getname(), super.Id());
            started = true;
        }
        catch (ServerException | ClientException e)   {
            errorWindow("Колдунство почему-то не сроботало, попробуй еще раз.");
        }
    }
    
    // Обработка полученных сообщений.
    @Override
    public void getMessage (String str) {
        try {
            StringTokenizer tokens = new StringTokenizer(str," ");
            String s;
            if(tokens.hasMoreTokens()) {
                s = tokens.nextToken();
                switch (Integer.parseInt(s)) {
                    // запрос на номер игрока
                    case 0:
                        // отправляем назад наш номер
                        send("1 " + String.valueOf(playerNum), super.Id());
                        break;
                    // обработка ответа с номером игрока
                    case 1:
                        s = tokens.nextToken();
                        playerNum = Integer.parseInt(s);
                        return;
                    // противник сделал ход
                    case 2: {
                        cells.clear();
                        // помещаем в вектор cells данные о состоянии поля противника
                        while (tokens.hasMoreTokens()) {
                            s = tokens.nextToken();
                            cells.add(Integer.parseInt(s));
                        }
                        // приводим свое поле в такое же состояние
                        for (int i = 0; i<cells.size();) {
                            l[(int)cells.elementAt(i++)][(int)cells.elementAt(i++)].setType(
                                        (int)cells.elementAt(i++)
                                    );
                        }
                        // обновляем экран
                        gameFrame.repaint();
                        // следующий ход наш
                        IsEnable = true;
                        return;
                    }
                    // копируем данные о поле противника в panel
                    case 3:
                        panel.clear();
                        while (tokens.hasMoreTokens()) {
                            s = tokens.nextToken();
                            panel.add(Integer.parseInt(s));
                        }
                        return;
                    case 5:
                        if(playerNum == 1) {
                            player2.setPlayerName(str.substring(1));
                        }
                        else {
                            player1.setPlayerName(str.substring(1));
                        }
                        gameFrame.repaint();
                        return;
                    // обновление индикаторов здоровья
                    case 4:
                        s = tokens.nextToken();
                        int h = Integer.parseInt(s);
                        s = tokens.nextToken();
                        int num = Integer.parseInt(s);
                        if(num == 1) {
                            player1.health().setValue(player1.health().getValue()+h);
                        }
                        else {
                            player2.health().setValue(player2.health().getValue()+h);
                        }
                        return;
                    // противник покинул игру
                    case 6:
                        if(!winner) errorWindow("Противник скрылся, испугавшись жестокой расправы.");
                        IamAlone = true;
                        return;
                    // получили запрос о старте игры
                    case 7:
                        send("8 " + String.valueOf(started), super.Id());
                        return;
                    // получили ответ о старте игры
                    case 8:
                        s = tokens.nextToken();
                        opponentStarted = Boolean.parseBoolean(s);
                        return;  
                    case 9:
                        errorWindow("Ты в пыли у ног твоего врага. ");
                        fieldJPanel.removeAll();
                        winner = true;
                        gameFrame.repaint();
                        return;   
                    default:
                        return;
                }
            }
        }
        catch (ServerException | ClientException e)   {
            errorWindow("Колдунство почему-то не сроботало, попробуй еще раз.");
        }
    }
    
    public boolean isAlone () {
        return IamAlone;
    }
    
    public Launcher(){
        super();
        panel = new Vector();
        player = null;
        started = false;
        playerNum = -1;
        currFigure = null; 
        IsEnable = false;
        IamAlone = winner = false;
        cells = new Vector();
        l = new Element[11][11];        
        
  gameFrame = new JFrame();
        startPanel = new StartGame(this);
        startPanel.setPreferredSize(new Dimension(250, 450));
        
	player1 = new PlayerGUI(1);
	player2 = new PlayerGUI(2);
	
	fieldJPanel = new JPanel();
	fieldJPanel.setPreferredSize(new Dimension(500,500));
	fieldJPanel.setBackground(new Color(100,100,100));
        initFieldPanel (1);
        
	backgroundPanel = new JPanel();
	backgroundPanel.setSize(1000,600);
	backgroundPanel.setBackground(new Color(0,0,0));
	backgroundPanel.add(player1);
	backgroundPanel.add(fieldJPanel);
	backgroundPanel.add(player2);
        
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
                if(IamAlone) {
                    Runtime.getRuntime().exit(0);
                }
                try{
                    gameFrame.setVisible(false);
                    send("6 ", Id());
                }
                catch (ServerException | ClientException ex)   {
                    errorWindow("Колдунство почему-то не сроботало, попробуй еще раз.");
                }
            }
        });
        
	backgroundPanel.add(exitButton);
        
        gameFrame.addWindowListener(new WindowListener() {
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
                // покидаем игру
                if(IamAlone) {
                    Runtime.getRuntime().exit(0);
                }
                try{
                    gameFrame.setVisible(false);
                    send("6 ", Id());
                }
                catch (ServerException | ClientException ex)   {
                    errorWindow("Колдунство почему-то не сроботало, попробуй еще раз.");
                }
            }	
        });
	gameFrame.setSize(1000,600);
	gameFrame.add(backgroundPanel);
        gameFrame.setResizable(false);
	gameFrame.setVisible(true);
    }
    
    public void setVisible (boolean b) {
	gameFrame.setVisible(b);
    }
    
    // Создает игровое поле в центре окна, если options = 0.
    // Создает начальное меню в центре окна, если options = 1.
    private void initFieldPanel (int options) {
        if(options == 0) {
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
            if(IsEnable) {
                repopulate();
            }
            else {
                for (int i = 0; i<panel.size();) {
                    l[(int)panel.elementAt(i++)][(int)panel.elementAt(i++)].setType((int)panel.elementAt(i++));
                }
                gameFrame.repaint();
            }
        }
        else {
            fieldJPanel.removeAll();
            fieldJPanel.setLayout(new FlowLayout());
            fieldJPanel.add(startPanel);
            gameFrame.repaint();
        }
    }
          
    // Обрабатывает ход и передает право хода противнику.
    private void TwoFiguresChanged(Element f1, Element f2) {
        try {
            if(IamAlone) {
                currFigure = null;
                errorWindow("Мудрый воин не станет биться сам с собой.");
                return;
            }
            // спрашиваем, начал ли противник игру
            send("7 ", super.Id());
            if(!opponentStarted) {
                currFigure = null;
                errorWindow("Не спеши, противник еще не надел свои доспехи.");
                return;
            }
            // если начал, то:
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
            val = 0;
            while (delEliminateMatches()) {
                fallDown();
                refillHoles();
            }
            gameFrame.repaint();
            currFigure = null;
            IsEnable = false;
            // меняем поле противника в соответствие с ходом
            String s = createBuffer();
            send("2 " + s, super.Id());
        }
        catch (ServerException | ClientException e)   {
            errorWindow("Колдунство не сроботало, попробуй еще раз.");
        }
    }
    
    // Возвращает строку с данными о состоянии поля.
    private String createBuffer () {
        String s = "";
        for(int i=0; i<l.length; i++)  {                     
            for(int j=0; j<l.length; j++) { 
                if(i<9 && j<9) {
                    s  += String.valueOf(i)+" "+String.valueOf(j)+" "+
                          String.valueOf(l[i][j].getType())+" ";
                }
            } 
        }
        return s;
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
    
    // По типу вычисляет наносимое повреждение.
    private int valCalc (int type) {
        switch (type) {
            case 1:
                return -6;
            case 2:
                return 0;
            case 3:
                return -4;
            case 4:
                return 3;
            case 5:
                return -3;
            case 6:
                return -2;
            default:
                return 0;
        }
    }
    
    private void killhill (int val) {
        try {
            if(val > 0) {
                player.health().setValue(player.health().getValue()+val);
                send("4 " + String.valueOf(val) + " " + String.valueOf(playerNum), super.Id());
            }
            else {
                if(playerNum == 1) {
                    player2.health().setValue(player2.health().getValue()+val);
                    send("4 " + String.valueOf(val) + " 2", super.Id());

                }
                else {
                    player1.health().setValue(player1.health().getValue()+val);
                    send("4 " + String.valueOf(val) + " 1", super.Id());
                }
            }
            if (playerNum == 1 && player2.health().getValue() <= 0) {
                errorWindow("Возрадуйся, ибо враг повержен, пал к твоим ногам.");
                fieldJPanel.removeAll();
                send("9 ", super.Id());
                winner = true;
                gameFrame.repaint();
                return;
            }
            if (playerNum == 2 && player1.health().getValue() <= 0) {
                errorWindow("Возрадуйся, ибо враг повержен, пал к твоим ногам.");
                fieldJPanel.removeAll();
                send("9 ", super.Id());
                winner = true;
                gameFrame.repaint();
                return;
            }
        } 
        catch (ClientException | ServerException ex) {
            errorWindow("Колдунство не сроботало, попробуй еще раз.");
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
	    for (int j = 0;!winner && j < l[0].length - 2; j++) {
		if (l[i][j].equals(l[i + 1][j])
                    && l[i + 1][j].equals(l[i + 2][j]) && !l[i][j].IsDel()) {
                    l[i][j].setDel(true);
                    l[i+1][j].setDel(true);
                    l[i+2][j].setDel(true);
		    matchOccured = true;
                    val = valCalc(l[i][j].getType());
                    killhill(val);
		}
		if (l[i][j].equals(l[i][j + 1])
                    && l[i][j + 1].equals(l[i][j + 2]) && !l[i][j].IsDel()) {
                    l[i][j].setDel(true);
                    l[i][j+1].setDel(true);
                    l[i][j+2].setDel(true);
		    matchOccured = true;
                    val = valCalc(l[i][j].getType());
                    killhill(val);
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
        try {
            do {
                while (eliminateMatches()) {}
            } 
            while (!gridHasValidMoves());
            
            String s = createBuffer();
            send("3 " + s,super.Id());
        }
        catch (ServerException | ClientException e)   {
            errorWindow("Колдунство не сроботало, попробуй еще раз.");
        }
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
}
