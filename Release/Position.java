package algorithmtester;

/**
 * Позиция клетки на игровом поле.
 */
class Position {
    private int x, y;
    
    Position (int X, int Y) {
        x = X; y = Y;
    }
    
    public int X () {return x;}
    public int Y () {return y;}
    public void setPosition (int X, int Y) {x = X; y = Y;}
}
