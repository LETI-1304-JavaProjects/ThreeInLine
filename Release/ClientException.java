/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servertest;

/**
 * Класс-исключение.
 * Служит для сообщения об ошибках в методах класса SimpleClient.
 */
public class ClientException extends Exception {
    private String message;
    
    public ClientException () {
        message = "ERROR";
    }
    
    public ClientException (String s) {
        message = s;
    }
    
    // Возвращает строку с причиной ошибки.
    public String what () {
        return message;
    }
}
