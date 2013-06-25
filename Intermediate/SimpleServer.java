package servertest;

public interface SimpleServer {
    
    // Отправляет сообщение message клиенту.
    // Гененирует ServerException, если отправка невозможна.
    public void send (String message, int id) throws ServerException;
    
    // Отправляет сообщение message всем клиентам.
    // Гененирует ServerException, если отправка невозможна.
    public void sendAll (String message) throws ServerException;
    
    // Подключение клиента к серверу.
    // Гененирует ServerException, если сервер зарегистрировал максимальное число клиентов.
    public void connect (SimpleClient client) throws ServerException;
    
    // Отключеие клиента от сервера, используя id клиента.
    // Генерирует ClientException, если клиент с таким id не зарегистрирован.
    public void disconnect (int id) throws ServerException;
    
    // Отключеие клиента от сервера, используя ссылку на клиента.
    // Генерирует ClientException, если клиент с таким id не зарегистрирован.
    public void disconnect (SimpleClient client) throws ServerException;
}
