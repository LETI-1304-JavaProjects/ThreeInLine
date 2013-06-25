package servertest;

/**
 * Класс-исключение.
 * Служит для сообщения об ошибках в методах класса LocalServer.
 */
public class ServerException extends Exception {
    private String message;
    
    public ServerException () {
        message = "ERROR";
    }
    
    public ServerException (String s) {
        message = s;
    }
    
    // Возвращает строку с причиной ошибки.
    public String what () {
        return message;
    }
}
