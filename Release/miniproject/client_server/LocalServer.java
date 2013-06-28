package miniproject.client_server;

import miniproject.client_server.exceptions.ServerException;

/**
 * Локальный сервер, позволяющий осуществлять обмен
 * сообщениями между двумя клиентами.
 */
public class LocalServer {
    
    private SimpleClient client1, client2;
    
    public LocalServer () {
        client1 = null;
        client2 = null;
    }
    
    // Отправляет сообщение message второму клиенту.
    // Гененирует ServerException, если отправка невозможна.
    public void send (String message, int id) throws ServerException {
        if(client1 == null && client2 == null) 
            throw new ServerException();
        
        if(client1 != null && client1.Id() != id) {
            client1.getMessage(message);
            return;
        }
        if(client2 != null && client2.Id() != id) {
            client2.getMessage(message);
            return;
        }
        throw new ServerException();
    }
    
    // Отправляет сообщение message всем клиентам.
    // Гененирует ServerException, если отправка невозможна.
    public void sendAll (String message) throws ServerException {
        if(client1 == null && client2 == null) 
            throw new ServerException();
        
        if(client1 != null) {
            client1.getMessage(message);
        }
        if(client2 != null) {
            client2.getMessage(message);
        }
    }
    
    // Подключение клиента к серверу.
    // Гененирует ServerException, если сервер зарегистрировал максимальное число клиентов.
    public void connect (SimpleClient client) throws ServerException {
        if(client1 == null) {
            client1 = client;
        }
        else if(client2 == null) {
            client2 = client;
        }
        else throw new ServerException();
    }
    
    // Отключеие клиента от сервера, используя id клиента.
    // Генерирует ClientException, если клиент с таким id не зарегистрирован.
    public void disconnect (int id) throws ServerException {
        if(client1 == null && client2 == null) 
            throw new ServerException();
        if(client1.Id() == id) {
            client1 = null;
        }
        else if (client2.Id() == id) {
            client2 = null;
        }
        else throw new ServerException();
    }
    
    // Отключеие клиента от сервера, используя ссылку на клиента.
    // Генерирует ClientException, если клиент с таким id не зарегистрирован.
    public void disconnect (SimpleClient client) throws ServerException {
        try {
            disconnect(client.Id());
            return;
        }
        catch(ServerException e) {
            throw e;
        }
    }
}
