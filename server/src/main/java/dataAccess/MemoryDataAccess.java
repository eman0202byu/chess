package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.Vector;
import java.util.HashMap;

public class MemoryDataAccess {
    //auth::AuthToken is Key
    private HashMap<String, Vector<String>> auth;
    //games::GameID is Key
    private HashMap<String, Vector<String>> games;
    //users::Username is Key
    private HashMap<String, Vector<String>> users;
    private Integer highestGameId = 0;
    public final String NULL_RESULT_EXCEPTION = "NULL_RESULT";
    public final String ALREADY_EXISTS_EXCEPTION = "ALREADY_EXISTS";

    public final String UNABLE_TO_REMOVE_EXCEPTION = "FAILURE_TO_REMOVE";

    public UserData getUser(String username) throws DataAccessException {
        UserData result = new UserData(null, null, null);
        var strUserData = users.get(username);
        if (strUserData == null) {
            throw new DataAccessException(NULL_RESULT_EXCEPTION);
        } else {
            result = result.changeUsername(strUserData.elementAt(0));
            result = result.changePassword(strUserData.elementAt(1));
            result = result.changeEmail(strUserData.elementAt(2));
        }
        return result;
    }

    public UserData createUser(String username, String password, String email) throws DataAccessException {
        UserData result = new UserData(null, null, null);
        var strUserData = users.get(username);
        if (strUserData != null) {
            throw new DataAccessException(ALREADY_EXISTS_EXCEPTION);
        } else {
            Vector<String> newUser = new Vector<String>();
            newUser.add(username);
            newUser.add(password);
            newUser.add(email);
            users.put(username, newUser);
        }
        strUserData = users.get(username);
        result = result.changeUsername(strUserData.elementAt(0));
        result = result.changePassword(strUserData.elementAt(1));
        result = result.changeEmail(strUserData.elementAt(2));
        return result;
    }

    public AuthData createAuth(String username, String key) throws DataAccessException {
        AuthData result = new AuthData(null, null);
        var strAuthData = auth.get(key);
        if (strAuthData != null) {
            throw new DataAccessException(ALREADY_EXISTS_EXCEPTION);
        } else {
            Vector<String> newAuth = new Vector<String>();
            newAuth.add(key);
            newAuth.add(username);
            users.put(key, newAuth);
        }
        strAuthData = auth.get(key);
        result = result.changeAuthToken(strAuthData.elementAt(0));
        result = result.changeUsername(strAuthData.elementAt(1));
        return result;
    }

    public UserData getAccount(String username, String password) throws DataAccessException {
        UserData result = new UserData(null, null, null);
        var strUserData = users.get(username);
        if (strUserData == null) {
            throw new DataAccessException(NULL_RESULT_EXCEPTION);
        } else {
            if (strUserData.elementAt(1) != password) {
                throw new DataAccessException(NULL_RESULT_EXCEPTION);
            } else {
                result = result.changeUsername(strUserData.elementAt(0));
                result = result.changePassword(strUserData.elementAt(1));
                result = result.changeEmail(strUserData.elementAt(2));
            }
        }
        return result;
    }

    public AuthData killAuth(String key) throws DataAccessException {
        AuthData result = new AuthData(null, null);
        var strAuthData = auth.get(key);
        if (strAuthData == null) {
            throw new DataAccessException(NULL_RESULT_EXCEPTION);
        } else {
            if (auth.remove(key, strAuthData)) {
                return result;
            } else {
                throw new DataAccessException(UNABLE_TO_REMOVE_EXCEPTION);
            }
        }
    }

    public UserData killUser(String key) throws DataAccessException {
        UserData result = new UserData(null, null, null);
        var strUserData = users.get(key);
        if (strUserData == null) {
            throw new DataAccessException(NULL_RESULT_EXCEPTION);
        } else {
            if (users.remove(key, strUserData)) {
                return result;
            } else {
                throw new DataAccessException(UNABLE_TO_REMOVE_EXCEPTION);
            }
        }
    }

    public GameData killGame(String key) throws DataAccessException {
        GameData result = new GameData(null, null, null, null, null);
        var strGameData = games.get(key);
        if (strGameData == null) {
            throw new DataAccessException(NULL_RESULT_EXCEPTION);
        } else {
            if (games.remove(key, strGameData)) {
                return result;
            } else {
                throw new DataAccessException(UNABLE_TO_REMOVE_EXCEPTION);
            }
        }
    }

    public void killAllAuth() throws DataAccessException {
        auth.clear();
        if (!auth.isEmpty()) {
            throw new DataAccessException(UNABLE_TO_REMOVE_EXCEPTION);
        }
    }

    public void killAllUser() throws DataAccessException {
        users.clear();
        if (!users.isEmpty()) {
            throw new DataAccessException(UNABLE_TO_REMOVE_EXCEPTION);
        }
    }

    public void killAllGame() throws DataAccessException {
        games.clear();
        if (!games.isEmpty()) {
            throw new DataAccessException(UNABLE_TO_REMOVE_EXCEPTION);
        }
    }

    public AuthData getAuth(String token) throws DataAccessException {
        AuthData result = new AuthData(null, null);
        var strAuthData = auth.get(token);
        if (strAuthData == null) {
            throw new DataAccessException(NULL_RESULT_EXCEPTION);
        } else {
            result = result.changeAuthToken(strAuthData.elementAt(0));
            result = result.changeUsername(strAuthData.elementAt(1));
            return result;
        }
    }

    public Vector<Vector<String>> getActiveGames() throws DataAccessException {
        var result = games.values();
        if (result == null) {
            throw new DataAccessException(NULL_RESULT_EXCEPTION);
        }
        return new Vector<Vector<String>>(result);
    }

    public GameData addGame(String name) {
        String id = highestGameId.toString();
        highestGameId++;
        var gameInit = new ChessGame();
        var output = new GameData(highestGameId, null, null, name, null);
        String game = new Gson().toJson(gameInit);
        Vector<String> push = new Vector<String>();
        push.add(id);
        push.add(null);
        push.add(null);
        push.add(name);
        push.add(game);
        games.put(id, push);
        return output;
    }

    public String joinGame(ChessGame.TeamColor color, String id, String token) throws DataAccessException {
        var game = games.get(id);
        if (game == null) {
            throw new DataAccessException(NULL_RESULT_EXCEPTION);
        }
        var username = auth.get(token).elementAt(1);
        if (color == ChessGame.TeamColor.WHITE) {
            if (game.elementAt(1) != null) {
                throw new DataAccessException(ALREADY_EXISTS_EXCEPTION);
            } else {
                games.get(id).setElementAt(username, 1);
            }
        } else if (color == ChessGame.TeamColor.BLACK) {
            if (game.elementAt(2) != null) {
                throw new DataAccessException(ALREADY_EXISTS_EXCEPTION);
            } else {
                games.get(id).setElementAt(username, 2);
            }
        }
        return id;
    }
}
