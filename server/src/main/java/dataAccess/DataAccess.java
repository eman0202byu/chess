package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Base64;
import java.util.HashSet;
import java.util.Vector;

import chess.ChessGame;

public class DataAccess {
    private MemoryDataAccess database;
    public final String JOIN_GAME_EXCEPTION_STRING = "NULL_GAME_ID";

    public DataAccess() {
        database = new MemoryDataAccess();
    }

    public UserData getUser(UserData user) throws DataAccessException {
        String username = user.username();
        return database.getUser(username);
    }

    public UserData createUser(UserData user) throws DataAccessException {
        String username = user.username();
        String password = user.password();
        String email = user.email();
        return database.createUser(username, password, email);
    }

    public AuthData createAuth(UserData user) throws DataAccessException {
        String username = user.username();
        String key = genAuth(username);
        return database.createAuth(username, key);
    }

    private String genAuth(String source) {
        return Base64.getEncoder().encodeToString(source.getBytes());
    }

    public UserData getAccount(UserData user) throws DataAccessException {
        String username = user.username();
        String password = user.password();
        return database.getAccount(username, password);
    }

    public AuthData killAuth(AuthData auth) throws DataAccessException {
        String token = auth.authToken();
        return database.killAuth(token);
    }

    public AuthData validate(AuthData auth) throws DataAccessException {
        String token = auth.authToken();
        return database.getAuth(token);
    }

    public HashSet<GameData> listActive() throws DataAccessException {
        HashSet<GameData> output = new HashSet<GameData>();
        Vector<Vector<String>> rawOutput = database.getActiveGames();
        for (int i = 0; i < rawOutput.size(); i++) {
            if (rawOutput.elementAt(i).size() == 4) {
                String strId = rawOutput.elementAt(i).elementAt(0);
                String white = rawOutput.elementAt(i).elementAt(1);
                String black = rawOutput.elementAt(i).elementAt(2);
                String name = rawOutput.elementAt(i).elementAt(3);
                int id = Integer.parseInt(strId);
                output.add(new GameData(id, white, black, name, null));
            }
        }
        return output;
    }

    public GameData createGame(GameData game) throws DataAccessException {
        String name = game.gameName();
        return database.addGame(name);
    }

    public GameData joinGame(ChessGame.TeamColor color, Integer id) throws DataAccessException {
        Integer gameId = database.joinGame(color, id);
        if (gameId == null) {
            throw new DataAccessException(JOIN_GAME_EXCEPTION_STRING);
        } else {
            return new GameData(gameId, null, null, null, null);
        }
    }

}
