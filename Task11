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
	if (x1 >= l.length || x2 >= l.length || y1 >= l[0].length || y2 >= l[0].length) {
            		return false;
        	}
	return hasSequenceForCoords(x1, y1, x2, y2) || hasSequenceForCoords(x2, y2, x1, y1);
    }
