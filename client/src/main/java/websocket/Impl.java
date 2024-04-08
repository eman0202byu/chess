package websocket;

import webSocketMessages.serverMessages.ServerMessage;

public class Impl implements NotificationHandler {
    @Override
    public void notify(ServerMessage notification) {
        System.out.println(notification.getServerMessageType());
        if (notification.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            ServerMessage.NotificationMessage result = (ServerMessage.NotificationMessage) notification;
            System.out.println(result.getMessage());
        }
    }
}
