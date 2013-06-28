package miniproject.client_server;

import miniproject.client_server.exceptions.ClientException;
import miniproject.client_server.exceptions.ServerException;

/**
 * Объекты класса могут осуществлять обмен сообщениями
 * друг с другом, используя класс LocalServer.
 * Один объект класса может быть связан лишь с одним сервером.
 */
abstract public class SimpleClient extends SimpleClient {

    private LocalServer server;
    private int id;
    private static int initId = 0;

    public SimpleClient() {
        server = null;
        id = initId++;
    }
    
    // Получение уникального идентификатора объекта.
    public int Id () {
        return id;
    }

    // Проверка подключения к серверу.
    public boolean isConnected () {
        return server != null;
    }
    
    // Подключение к серверу.
    // Генерирует ClientException, если клиент уже подключен.
    // Генерирует ServerException, если в подключении отказано.
    public void connect (LocalServer server) throws ClientException, ServerException {
        if(this.server != null) 
            throw new ClientException();
        this.server = server;
        try {
            server.connect(this);
            return;
        }
        catch (ServerException e) {
            this.server = null;
            throw e;
        }
    }
    
    // Отключение.
    // Генерирует ClientException, если клиент не подключен.
    // Генерирует ServerException, если сервер не может произвести отключение.
    public void disconnect () throws ClientException, ServerException {
        if(server == null) 
            throw new ClientException();
        try {
            server.disconnect(id);
            server = null;
            return;
        } 
        catch (ServerException ex) {
            throw ex;
        }
    }
    
    // Посылка сообщения клиенту с идентификатором id.
    // Генерирует ClientException, если клиент не подключен.
    // Гененирует ServerException, если отправка невозможна.
    public void send (String message, int id) throws ClientException, ServerException {
        if(server == null) 
            throw new ClientException();
        try {
            server.send(message, id);
            return;
        }
        catch(ServerException e) {
            throw e;
        }
    }
    
    // Посылка сообщения всем клиентам.
    // Генерирует ClientException, если клиент не подключен.
    // Гененирует ServerException, если отправка невозможна.
    public void sendAll (String message) throws ClientException, ServerException {
        if(server == null) 
            throw new ClientException();
        try {
            server.sendAll(message);
        }
        catch(ServerException e) {
            throw e;
        }
    }
    
    // Выполняется при получении сообщения.
    // Определяется пользователем.
    abstract public void getMessage (String message);
}
