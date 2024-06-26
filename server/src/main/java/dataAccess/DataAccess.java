package dataAccess;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Base64;
import java.util.HashSet;
import java.util.Objects;
import java.util.Vector;

import chess.ChessGame;

public class DataAccess {
    private MySqlDataAccess database;
    public final String JOIN_GAME_EXCEPTION;
    public final String JAVA_IS_BROKEN_EXCEPTION;
    public final String FAILURE_TO_DELETE_AUTH_TABLE_EXCEPTION;
    public final String FAILURE_TO_DELETE_USER_TABLE_EXCEPTION;
    public final String FAILURE_TO_DELETE_GAME_TABLE_EXCEPTION;

    public final String DB_NULL_RESULT_EXCEPTION;
    public final String DB_ALREADY_EXISTS_EXCEPTION;

    public final String DB_UNABLE_TO_REMOVE_EXCEPTION;
    public final Integer MAXIMUM_AUTHTOKENS_PER_USER = 10;

    public DataAccess() throws DataAccessException {
        JOIN_GAME_EXCEPTION = "NULL_GAME_ID";
        JAVA_IS_BROKEN_EXCEPTION = "JAVA_LOGIC_ERROR";
        FAILURE_TO_DELETE_AUTH_TABLE_EXCEPTION = "AUTH_TABLE_KILL_ERROR";
        FAILURE_TO_DELETE_USER_TABLE_EXCEPTION = "USER_TABLE_KILL_ERROR";
        FAILURE_TO_DELETE_GAME_TABLE_EXCEPTION = "GAME_TABLE_KILL_ERROR";

        database = new MySqlDataAccess();
        DB_NULL_RESULT_EXCEPTION = database.NULL_RESULT_EXCEPTION;
        DB_ALREADY_EXISTS_EXCEPTION = database.ALREADY_EXISTS_EXCEPTION;
        DB_UNABLE_TO_REMOVE_EXCEPTION = database.UNABLE_TO_REMOVE_EXCEPTION;
    }

    public UserData createUser(UserData user) throws DataAccessException {
        String username = user.username();
        String password = user.password();
        String email = user.email();
        return database.createUser(username, password, email);
    }

    public AuthData createAuth(UserData user) throws DataAccessException {
        if (user.username() == null) {
            throw new RuntimeException("SERVICE_WAS_REMOVED");
        }
        String username = user.username();
        String key = genAuth(username);
        AuthData result = new AuthData(null, null);
        for (int i = 0; i < MAXIMUM_AUTHTOKENS_PER_USER; i++) {
            try {
                result = database.createAuth(username, key);
                break;
            } catch (DataAccessException e) {
                if (Objects.equals(e.getMessage(), database.ALREADY_EXISTS_EXCEPTION)) {
                    key = genAuth(key);
                } else {
                    throw new DataAccessException(JAVA_IS_BROKEN_EXCEPTION);
                }
            }
        }
        return result;
    }

    private String genAuth(String source) {
        String dirtyOutput = Base64.getEncoder().encodeToString(source.getBytes());
        return dirtyOutput.replaceAll("\\\\", "X");
    }

    public UserData getAccount(UserData user) throws DataAccessException {
        String username = user.username();
        String password = user.password();
        return database.getAccount(username, password);
    }

    public AuthData killAuth(AuthData auth) throws DataAccessException {
        String key = auth.authToken();
        return database.killAuth(key);
    }

    public boolean killEverything() throws DataAccessException {
        try {
            database.killTable("user");
        } catch (DataAccessException e) {
            throw new DataAccessException(FAILURE_TO_DELETE_USER_TABLE_EXCEPTION);
        }
        try {
            database.killTable("games");
        } catch (DataAccessException e) {
            throw new DataAccessException(FAILURE_TO_DELETE_GAME_TABLE_EXCEPTION);
        }
        try {
            database.killTable("auth");
        } catch (DataAccessException e) {
            throw new DataAccessException(FAILURE_TO_DELETE_AUTH_TABLE_EXCEPTION);
        }
        return true;
    }

    public AuthData validate(AuthData auth) throws DataAccessException {
        String token = auth.authToken();
        return database.getAuth(token);
    }

    public HashSet<GameData> listActive() throws DataAccessException {
        HashSet<GameData> output = new HashSet<>();
        Vector<Vector<String>> rawOutput = database.getActiveGames();
        for (int i = 0; i < rawOutput.size(); i++) {
            String strId = rawOutput.elementAt(i).elementAt(0);
            String white = rawOutput.elementAt(i).elementAt(1);
            String black = rawOutput.elementAt(i).elementAt(2);
            String name = rawOutput.elementAt(i).elementAt(3);
            int id = Integer.parseInt(strId);
            output.add(new GameData(id, white, black, name, null));
        }
        return output;
    }

    public GameData createGame(GameData game) throws DataAccessException {
        if (game.gameName() == null) {
            throw new RuntimeException("SERVICE_WAS_REMOVED");
        }
        String name = game.gameName();
        return database.addGame(name);
    }

    public String getGame(String id, String token) throws DataAccessException {
        return database.getGame(id, token);
    }

    public String getUser(String token) throws DataAccessException {
        return database.getUsername(token);
    }

    public void removeFromGame(String id, String username) throws DataAccessException {
        database.removeFromGame(id, username);
    }

    public void killGame(String id) throws DataAccessException {
        database.killGame(id);
    }

    public GameData joinGame(ChessGame.TeamColor color, Integer id, AuthData auth) throws DataAccessException {
        String strId = id.toString();
        String token = auth.authToken();
        String newStrId = database.joinGame(color, strId, token);
        if (newStrId == null) {
            throw new DataAccessException(JOIN_GAME_EXCEPTION);
        } else {
            Integer gameId = Integer.parseInt(newStrId);
            return new GameData(gameId, null, null, null, null);
        }
    }

    public void gameUsernameExist(String username, Integer id) throws DataAccessException {
        database.gameUsernameExist(username, id);
    }

    public void gameUsernameColor(String username, ChessGame.TeamColor playerColor, String gameID) throws DataAccessException {
        database.gameUsernameColor(username, playerColor, gameID);
    }

    public void replaceGame(ChessGame currentGame, String id) throws DataAccessException {
        database.replaceGame(new Gson().toJson(currentGame), id);
    }
}
