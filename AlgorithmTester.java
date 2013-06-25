package algorithmtester;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class AlgorithmTester {
    private int l [][];
    
    AlgorithmTester () {
        l = new int[7][7];
    }
    // Если игрок делал ход, заполняет пустые места рандомными клетками.
    private void refillHoles() {
        for (int i = 0; i < l.length-2; i++) {
            for (int j = 0; j < l[0].length; j++) {
                if (l[i][j]==0) {
                    l[i][j]=(int)(Math.random()*1000%6+1);
                }
            }
        }
    }

    // проверка наличия хода на игровом поле
    private boolean gridHasValidMoves() {
        for (int i = 0; i < l.length-2; i++) {
            for (int j = 0; j < l[0].length; j++) {
                if (isValidMove(i, j, i + 1, j) || isValidMove(i, j, i, j + 1)) {
                    return true;
                }     
            }
        }
        return false;
    }
    
    // проверка наличия рядов по три или более
    private boolean hasSequenceForCoords(int xo, int yo, int x, int y) {
  int horzSequence = 1; 
	if (x >= 1) {
            if (l[xo][yo] == l[x - 1][y]) {
                horzSequence++;
                    if (x >= 2 && l[xo][yo] == l[x - 2][y]) {
                        horzSequence++;
                    }
            }
    	}
	if (x < 8) {
            if (l[xo][yo] == l[x + 1][y]) {
                horzSequence++;
                    if (x < 7 && l[xo][yo] == l[x + 2][y]) {
                        horzSequence++;
                    }
            }
        }
	if (horzSequence >= 3) {
            return true;
        }
        
	int vertSequence = 1;
	if (y >= 1) {
            if (l[xo][yo] == l[x][y - 1]) {
                vertSequence++;
                if (y >= 2 && l[xo][yo] == l[x][y - 2]) {
                    vertSequence++;
                }
            }
        }
	if (y < 8) {
            if (l[xo][yo] == l[x][y + 1]) {
                vertSequence++;
                if (y < 7 && l[xo][yo] == l[x][y + 2]) {
                    vertSequence++;
                }
            }
        }
	if (vertSequence >= 3) {
            		return true;
        }
	return false;
    }
    
    private boolean eliminateMatches() {
	boolean matchOccured = false;
	Set<Position> matches = new HashSet<>();
	for (int i = 0; i < l.length - 2; i++) {
	    for (int j = 0; j < l[0].length - 2; j++) {
		if (l[i][j] == l[i + 1][j]&& l[i + 1][j]==l[i + 2][j]) {
		    matches.add(new Position(i, j));
		    matches.add(new Position(i + 1, j));
		    matches.add(new Position(i + 2, j));
		    matchOccured = true;
		}
		if (l[i][j]==l[i][j + 1] && l[i][j + 1]==l[i][j + 2]) {
		    matches.add(new Position(i, j));
		    matches.add(new Position(i, j + 1));
		    matches.add(new Position(i, j + 2));
		    matchOccured = true;
		}
	    }
	}
	for (Position coords : matches) {
            l[coords.X()][coords.Y()] = (int)(Math.random()*1000%6+1);
        }
	return matchOccured;
    }
    
    // Генерирует корректное поле (с ходом и без рядов по 3).
    private void repopulate() {
        do {
            while (eliminateMatches()) {}
        } 
        while (!gridHasValidMoves());
    }
    
    // Находит ряды по 3 и удаляет их.
    private boolean delEliminateMatches() {
	boolean matchOccured = false;
	for (int i = 0; i < l.length - 2; i++) {
	    for (int j = 0;j < l[0].length - 2; j++) {
		if (l[i][j]==l[i + 1][j] && l[i + 1][j]==l[i + 2][j] && l[i][j]!=0) {
                   		 l[i][j] = 0;
                   		 l[i+1][j] = 0;
                   		 l[i+2][j] = 0;
		    	 matchOccured = true;
		}
		if (l[i][j]==l[i][j + 1] && l[i][j + 1]==l[i][j + 2]&&l[i][j]!=0) {
                   		l[i][j] = 0;
                    		l[i][j+1] = 0;
                    		l[i][j+2] = 0;
		    	matchOccured = true;
		}
	    }
	}
	return matchOccured;
    }
    
    // проверка хода на корректность
    private boolean isValidMove(int x1, int y1, int x2, int y2) {
	if (Math.abs(x1 - x2) + Math.abs(y1 - y2) > 1) {
            		return false;
        	}
	if (x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0) {
            		return false;
        	}
	if (x1 >= l.length-2 || x2 >= l.length-2 || y1 >= l[0].length-2 || y2 >= l[0].length-2) {
            		return false;
        	}
	return hasSequenceForCoords(x1, y1, x2, y2) || hasSequenceForCoords(x2, y2, x1, y1);
    }
    
    public void input () {
        Scanner s = new Scanner(System.in);
        for(int i = 0; i<l.length-2; i++) {
            for(int j = 0; j<l[0].length-2; j++) {
                l[i][j] = s.nextInt();
            }
        }
    }
    
    public void output () {
        for(int i = 0; i<l.length-2; i++) {
            for(int j = 0; j<l[0].length-2; j++) {
                System.out.print(l[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        AlgorithmTester alg = new AlgorithmTester();
        alg.repopulate();
        alg.output();
    }
}
