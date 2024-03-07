package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;
import java.util.Vector;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlDataAccess {
    public final String NULL_RESULT_EXCEPTION;
    public final String ALREADY_EXISTS_EXCEPTION;
    public final String UNABLE_TO_REMOVE_EXCEPTION;
    public final String FAILURE_TO_MAKE_DB_EXCEPTION;

    public final String FAILURE_TO_GET_CONNECTION_TO_DB_EXCEPTION;

    public final String INVALID_TYPING_IN_CODE;
    private final String[] ALL_TABLE_ARGS = {
            """
              CREATE TABLE IF NOT EXISTS `user` (
              `id` INT AUTO_INCREMENT PRIMARY KEY,
              `username` VARCHAR(256) UNIQUE NOT NULL,
              `password` VARCHAR(256) NOT NULL,
              `email` VARCHAR(256) NOT NULL
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS `games` (
              `id` INT AUTO_INCREMENT PRIMARY KEY,
              `white` VARCHAR(256),
              `black` VARCHAR(256),
              `gameName` VARCHAR(256) NOT NULL,
              `game` TEXT
            );
            """, //`game` TEXT -> JSON Serialization of ChessGame
            """
            CREATE TABLE IF NOT EXISTS `auth` (
              `id` INT AUTO_INCREMENT PRIMARY KEY,
              `username` VARCHAR(256) NOT NULL,
              `token` VARCHAR(256) UNIQUE NOT NULL
            );
            """
    };

    public MySqlDataAccess() throws DataAccessException {
        NULL_RESULT_EXCEPTION = "NULL_RESULT";
        ALREADY_EXISTS_EXCEPTION = "ALREADY_EXISTS";
        UNABLE_TO_REMOVE_EXCEPTION = "FAILURE_TO_REMOVE";
        FAILURE_TO_MAKE_DB_EXCEPTION = "MySQL_FAILURE_TO_INITIALIZE";
        FAILURE_TO_GET_CONNECTION_TO_DB_EXCEPTION = "MySQL_FAILURE_TO_CONNECT";
        INVALID_TYPING_IN_CODE = "TYPE_FAILURE";
        configDb();
    }

    private void configDb() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : ALL_TABLE_ARGS) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            String out = FAILURE_TO_MAKE_DB_EXCEPTION + " :: " + ex.getMessage();
            throw new DataAccessException(out);
        }
    }

    public UserData createUser(String username, String password, String email) throws DataAccessException {
        UserData result = new UserData(null, null, null);
        String statement = "INSERT INTO user (username, password, email) VALUES (?,?,?);";
        Vector<String> arguments = new Vector<String>();
        arguments.add(username);
        arguments.add(password);
        arguments.add(email);
        execUpdate(result, statement, arguments);
        return result;
    }


    public UserData getAccount(String username, String password) throws DataAccessException {
        return null;
    }

    public AuthData createAuth(String username, String key) throws DataAccessException {
        return null;
    }

    public AuthData killAuth(String key) throws DataAccessException {
        return null;

    }

    public void killAllAuth() throws DataAccessException {

    }

    public void killAllUser() throws DataAccessException {
        //truncate user
    }

    public void killAllGame() throws DataAccessException {

    }

    public AuthData getAuth(String token) throws DataAccessException {
        return null;
    }

    public Vector<Vector<String>> getActiveGames() throws DataAccessException {
        return null;
    }

    public GameData addGame(String name) {
        return null;
    }

    public String joinGame(ChessGame.TeamColor color, String id, String token) throws DataAccessException {
        return null;
    }

    private Object execUpdate(Object passThrough, String statement, Vector<String> arguments) throws DataAccessException {
        if (passThrough instanceof AuthData result) {

        } else if (passThrough instanceof GameData result) {

        } else if (passThrough instanceof UserData result) {
            String username = arguments.elementAt(0);
            ;
            String password = arguments.elementAt(1);
            String email = arguments.elementAt(2);

            try (
                    PreparedStatement preparedStatement = DatabaseManager.getConnection().prepareStatement(statement, RETURN_GENERATED_KEYS)
            ) {
                preparedStatement.setString(1, username);
                result = result.changeUsername(username);
                preparedStatement.setString(2, password);
                result = result.changePassword(password);
                preparedStatement.setString(3, email);
                result = result.changeEmail(email);
                preparedStatement.executeUpdate();
                var set = preparedStatement.getGeneratedKeys();
                set.next();
                var id = set.getInt(1);
                //NULL_RESULT_EXCEPTION

            } catch (SQLException e) {
                var check = e.getErrorCode();
                if (check == 1062 || check == 1586) {
                    throw new DataAccessException(ALREADY_EXISTS_EXCEPTION);
                } else {
                    String out = "FATAL_ERROR::MYSqlDAO::execUpdate :: " + e.getMessage();
                    throw new DataAccessException(out);
                }
            } catch (DataAccessException e) {
                String out = FAILURE_TO_GET_CONNECTION_TO_DB_EXCEPTION + " :: " + e.getMessage();
                throw new DataAccessException(out);
            }
        } else {
            throw new DataAccessException(INVALID_TYPING_IN_CODE);
        }
        return null;
    }
}
