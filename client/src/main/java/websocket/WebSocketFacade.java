package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
import chess.ChessMove;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinPlayer(int gameID, ChessGame.TeamColor playerColor, String authToken) throws ResponseException {
        try {
            UserGameCommand command = new UserGameCommand(authToken, UserGameCommand.CommandType.JOIN_PLAYER);
            command.gameID = gameID;
            command.playerColor = playerColor;
            sendCommand(command);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void joinObserver(int gameID, String authToken) throws ResponseException {
        try {
            UserGameCommand command = new UserGameCommand(authToken, UserGameCommand.CommandType.JOIN_OBSERVER);
            command.gameID = gameID;
            sendCommand(command);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void makeMove(int gameID, ChessMove move, String authToken) throws ResponseException {
        try {
            UserGameCommand command = new UserGameCommand(authToken, UserGameCommand.CommandType.MAKE_MOVE);
            command.gameID = gameID;
            command.move = move;
            sendCommand(command);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leaveGame(int gameID, String authToken) throws ResponseException {
        try {
            UserGameCommand command = new UserGameCommand(authToken, UserGameCommand.CommandType.LEAVE);
            command.gameID = gameID;
            sendCommand(command);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void resignGame(int gameID, String authToken) throws ResponseException {
        try {
            UserGameCommand command = new UserGameCommand(authToken, UserGameCommand.CommandType.RESIGN);
            command.gameID = gameID;
            sendCommand(command);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private void sendCommand(UserGameCommand command) throws IOException {
        String jsonCommand = new Gson().toJson(command);
        this.session.getBasicRemote().sendText(jsonCommand);
    }
}

