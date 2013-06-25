// Если игрок делал ход, заполняет пустые места рандомными клетками.
    private void refillHoles() {
        for (int i = 0; i < l.length; i++) {
            for (int j = 0; j < l[0].length; j++) {
                if (l[i][j]==0) {
                    l[i][j]=(int)(Math.random()*1000%6+1);
                }
            }
        }
    }


// проверка наличия хода на игровом поле
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
    
    
    // Генерирует корректное поле (с ходом и без рядов по 3).
    private void repopulate() {
        try {
            do {
                while (eliminateMatches()) {}
            } 
            while (!gridHasValidMoves());
        }
        catch (ServerException | ClientException e)   {
            
        }
    }
