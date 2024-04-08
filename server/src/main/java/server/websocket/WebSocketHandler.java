package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import service.ChessService;
import service.ChessService.StatusCodes;
import service.ServiceReport;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ChessService chessService;
    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler() throws DataAccessException {
        chessService = new ChessService();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinGameAsPlayer(command.getAuthString(), session, command);
            case JOIN_OBSERVER -> joinGameAsObserver(command.getAuthString(), session, command);
            case MAKE_MOVE -> makeMove(command.getAuthString(), session, command);
            case LEAVE -> leaveGame(command.getAuthString(), session, command);
            case RESIGN -> resignGame(command.getAuthString(), session, command);
        }
    }

    private void joinGameAsPlayer(String authToken, Session session, UserGameCommand command) throws IOException {
        AuthData authData = new AuthData(authToken, null);
        boolean isAuthenticated;
        try {
            chessService.ValidationCheck(authData);
            isAuthenticated = true;
        } catch (DataAccessException e) {
            isAuthenticated = false;
        }

        if (!isAuthenticated) {
            ServerMessage errorMessage = new ServerMessage.ErrorMessage("Authentication failed");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        GameData gameState = new GameData(null, null, null, null, new ChessGame());
        ServiceReport result = chessService.joinGames(authData, command.playerColor, command.gameID);
        if (result.Status() == StatusCodes.ALREADYTAKEN) {
            result = chessService.reservedSpot(authData, command.playerColor, command.gameID);
            if (result.Status() == StatusCodes.PASS) {
                ServerMessage loadGameMessage = new ServerMessage.LoadGameMessage(gameState);
                var result1 = new Gson().toJson(loadGameMessage);
                session.getRemote().sendString(result1);
                String notification = "Player has joined";
                ServerMessage notificationMessage = new ServerMessage.NotificationMessage(notification);
                var result2 = new Gson().toJson(notificationMessage);
                connections.add(authData.authToken(), session, command.gameID);
                connections.broadcast(authData.authToken(), command.gameID, result2);
            } else {
                ServerMessage errorMessage = new ServerMessage.ErrorMessage("Failed to join the game");
                session.getRemote().sendString(new Gson().toJson(errorMessage));
            }
        } else {
            ServerMessage errorMessage = new ServerMessage.ErrorMessage("Failed to join the game");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
        }
    }

    private void joinGameAsObserver(String authToken, Session session, UserGameCommand command) throws IOException {
        AuthData authData = new AuthData(authToken, null);
        boolean isAuthenticated;
        try {
            chessService.ValidationCheck(authData);
            isAuthenticated = true;
        } catch (DataAccessException e) {
            isAuthenticated = false;
        }

        if (!isAuthenticated) {
            ServerMessage errorMessage = new ServerMessage.ErrorMessage("Authentication failed");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        GameData gameState = new GameData(null, null, null, null, new ChessGame());
        ServiceReport result = chessService.joinGames(authData, null, command.gameID);
        if (result.Status() == StatusCodes.PASS) {
            ServerMessage loadGameMessage = new ServerMessage.LoadGameMessage(gameState);
            var result1 = new Gson().toJson(loadGameMessage);
            session.getRemote().sendString(result1);
            String notification = "Observer has joined";
            ServerMessage notificationMessage = new ServerMessage.NotificationMessage(notification);
            var result2 = new Gson().toJson(notificationMessage);
            connections.add(authData.authToken(), session, command.gameID);
            connections.broadcast(authData.authToken(), command.gameID, result2);
        } else {
            ServerMessage errorMessage = new ServerMessage.ErrorMessage("Failed to join the game");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
        }
    }

    private void makeMove(String authToken, Session session, UserGameCommand command) throws IOException {
        AuthData authData = new AuthData(authToken, null);
        boolean isAuthenticated;
        try {
            chessService.ValidationCheck(authData);
            isAuthenticated = true;
        } catch (DataAccessException e) {
            isAuthenticated = false;
        }

        if (!isAuthenticated) {
            ServerMessage errorMessage = new ServerMessage.ErrorMessage("Authentication failed");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        GameData gameState = new GameData(null, null, null, null, new ChessGame());
        ServiceReport result = chessService.makeMove(authData, command.gameID, command.move);
        if (result.Status() == ChessService.StatusCodes.PASS) {
            ServerMessage loadGameMessage = new ServerMessage.LoadGameMessage(gameState);
            var result1 = new Gson().toJson(loadGameMessage);
            session.getRemote().sendString(result1);
            ServerMessage notificationMessage = new ServerMessage.LoadGameMessage(gameState);
            var result2 = new Gson().toJson(notificationMessage);
            connections.broadcast(authData.authToken(), command.gameID, result2);
            ServerMessage notificationMessages = new ServerMessage.NotificationMessage("Move Made");
            var result3 = new Gson().toJson(notificationMessages);
            connections.broadcast(authData.authToken(), command.gameID, result3);
        } else {
            ServerMessage errorMessage = new ServerMessage.ErrorMessage("Failed to make the move");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
        }
    }

    private void leaveGame(String authToken, Session session, UserGameCommand command) throws IOException {
        AuthData authData = new AuthData(authToken, null);
        boolean isAuthenticated;
        try {
            chessService.ValidationCheck(authData);
            isAuthenticated = true;
        } catch (DataAccessException e) {
            isAuthenticated = false;
        }

        if (!isAuthenticated) {
            ServerMessage errorMessage = new ServerMessage.ErrorMessage("Authentication failed");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        ServiceReport result = chessService.leaveGame(authData, command.gameID);
        if (result.Status() == ChessService.StatusCodes.PASS) {
            ServerMessage successMessage = new ServerMessage.NotificationMessage("You have left the game");
            session.getRemote().sendString(new Gson().toJson(successMessage));
            ServerMessage notificationMessages = new ServerMessage.NotificationMessage("Player has left");
            var result3 = new Gson().toJson(notificationMessages);
            connections.broadcast(authData.authToken(), command.gameID, result3);
            session.close();
        } else {
            ServerMessage errorMessage = new ServerMessage.ErrorMessage("Failed to leave the game");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
        }
    }

    private void resignGame(String authToken, Session session, UserGameCommand command) throws IOException {
        AuthData authData = new AuthData(authToken, null);
        boolean isAuthenticated;
        try {
            chessService.ValidationCheck(authData);
            isAuthenticated = true;
        } catch (DataAccessException e) {
            isAuthenticated = false;
        }

        if (!isAuthenticated) {
            ServerMessage errorMessage = new ServerMessage.ErrorMessage("Authentication failed");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        ServiceReport result = chessService.resignGame(authData, command.gameID);
        if (result.Status() == ChessService.StatusCodes.PASS) {
            // Send a success message indicating the user resigned from the game
            ServerMessage successMessage = new ServerMessage.NotificationMessage("You have resigned from the game");
            session.getRemote().sendString(new Gson().toJson(successMessage));
            ServerMessage notification = new ServerMessage.NotificationMessage("A user has resigned");
            connections.broadcast(authToken, command.gameID, new Gson().toJson(notification));
        } else {
            // Send an error message upon failure to resign from the game
            ServerMessage errorMessage = new ServerMessage.ErrorMessage("Failed to resign from the game");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
        }
    }
}