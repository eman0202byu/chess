package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
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
            // Add cases for other user game commands like making moves, leaving games, etc.
        }
    }

    private void joinGameAsPlayer(String authToken, Session session, UserGameCommand command) throws IOException {
        // Authenticate the user using the provided auth token
        AuthData authData = new AuthData(authToken, null);
        boolean isAuthenticated;
        try {
            chessService.ValidationCheck(authData);
            isAuthenticated = true;
        } catch (DataAccessException e) {
            isAuthenticated = false;
        }

        if (!isAuthenticated) {
            // Send an error message indicating authentication failure
            ServerMessage errorMessage = new ServerMessage.ErrorMessage("Authentication failed");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            connections.broadcast("", "ERROR: Failure to authenticate user");
            return;
        }

        GameData gameState = new GameData(null, null, null, null, new ChessGame());
        ServiceReport result = chessService.joinGames(authData, command.playerColor, command.gameID);
        if (result.Status() == StatusCodes.ALREADYTAKEN) {
            // Send the game state upon successful join
            ServerMessage loadGameMessage = new ServerMessage.LoadGameMessage(gameState);
            session.getRemote().sendString(new Gson().toJson(loadGameMessage));
            connections.broadcast("", gameState.toString());
        } else {
            // Send an error message upon failure to join
            ServerMessage errorMessage = new ServerMessage.ErrorMessage("Failed to join the game");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            connections.broadcast("", "ERROR: Failure to joinGame");
        }
    }

    private void joinGameAsObserver(String authToken, Session session, UserGameCommand command) throws IOException {
        // Similar to joinGameAsPlayer, but for joining as an observer
        // Implement this method
    }

    // Implement methods for other user game commands like making moves, leaving games, etc.
}