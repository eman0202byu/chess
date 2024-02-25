package dataAccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Vector;

public class MemoryDataAccess {
    public UserData getUser(String username) {
        return null; //TODO:: IMPLEMENT
    }

    public UserData createUser(String username, String password, String email) {
        return null; //TODO:: IMPLEMENT
    }

    public AuthData createAuth(String username, String key) {
        return null; //TODO:: IMPLEMENT
    }

    public UserData getAccount(String username, String password) {
        return null; //TODO:: IMPLEMENT
    }

    public AuthData killAuth(String token) {
        return null; //TODO:: IMPLEMENT
    }

    public AuthData getAuth(String token) {
        return null; //TODO:: IMPLEMENT
    }

    public Vector<Vector<String>> getActiveGames() {
        return null; //TODO:: IMPLEMENT
    }

    public GameData addGame(String name) {
        return null; //TODO:: IMPLEMENT
    }

    public Integer joinGame(ChessGame.TeamColor color, Integer id) {
        return null; //TODO:: IMPLEMENT
    }
}
