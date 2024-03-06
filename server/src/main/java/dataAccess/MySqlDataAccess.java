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
            """, //`game` TEXT, -> JSON Serialization of ChessGame
            """
            CREATE TABLE IF NOT EXISTS `auth` (
              `id` INT AUTO_INCREMENT PRIMARY KEY,
              `username` VARCHAR(256) NOT NULL,
              `token` VARCHAR(256) NOT NULL
            );
            """
    };

    public MySqlDataAccess() throws DataAccessException {
        NULL_RESULT_EXCEPTION = "NULL_RESULT";
        ALREADY_EXISTS_EXCEPTION = "ALREADY_EXISTS";
        UNABLE_TO_REMOVE_EXCEPTION = "FAILURE_TO_REMOVE";
        FAILURE_TO_MAKE_DB_EXCEPTION = "MySQL_FAILURE_TO_INITIALIZE";
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
        return null;
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
}
