package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;
import java.util.Objects;
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
        Vector<String> strUserData;
        try {
            strUserData = execUpdate(result, statement, arguments);
        } catch (SQLException e) {
            var check = e.getErrorCode();
            if (check == 1062) {
                throw new DataAccessException(ALREADY_EXISTS_EXCEPTION);
            } else {
                String out = "FATAL_ERROR::MYSqlDAO::execUpdate :: " + e.getMessage();
                throw new DataAccessException(out);
            }
        } catch (DataAccessException e) {
            String out = FAILURE_TO_GET_CONNECTION_TO_DB_EXCEPTION + " :: " + e.getMessage();
            throw new DataAccessException(out);
        }
        result = result.changeUsername(strUserData.elementAt(1));
        result = result.changePassword(strUserData.elementAt(2));
        result = result.changeEmail(strUserData.elementAt(3));
        return result;
    }

    public UserData getAccount(String username, String password) throws DataAccessException {
        UserData result = new UserData(null, null, null);
        String statement = "SELECT * FROM user WHERE username COLLATE utf8mb4_bin = ?";
        Vector<String> arguments = new Vector<String>();
        arguments.add(username);
        arguments.add(password);
        Vector<String> strUserData;
        try {
            execQuery(result, statement, arguments);
        } catch (SQLException e) {
            var check = e.getErrorCode();
            if (check == 0) {
                throw new DataAccessException(NULL_RESULT_EXCEPTION);
            } else {
                String out = "FATAL_ERROR::MYSqlDAO::execUpdate :: " + e.getMessage();
                throw new DataAccessException(out);
            }
        } catch (DataAccessException e) {
            String out = FAILURE_TO_GET_CONNECTION_TO_DB_EXCEPTION + " :: " + e.getMessage();
            throw new DataAccessException(out);
        }
        statement = "SELECT * FROM user WHERE username COLLATE utf8mb4_bin = ? AND password COLLATE utf8mb4_bin = ?";
        try {
            strUserData = execQuery(result, statement, arguments);
        } catch (SQLException e) {
            var check = e.getErrorCode();
            if (check == 0) {
                throw new DataAccessException(ALREADY_EXISTS_EXCEPTION);
            } else {
                String out = "FATAL_ERROR::MYSqlDAO::execUpdate :: " + e.getMessage();
                throw new DataAccessException(out);
            }
        } catch (DataAccessException e) {
            String out = FAILURE_TO_GET_CONNECTION_TO_DB_EXCEPTION + " :: " + e.getMessage();
            throw new DataAccessException(out);
        }
        if (strUserData == null) {
            throw new DataAccessException(NULL_RESULT_EXCEPTION);
        }

        return result;
    }

    public AuthData createAuth(String username, String key) throws DataAccessException {
        AuthData result = new AuthData(null, null);
        String statement = "SELECT * FROM auth WHERE token COLLATE utf8mb4_bin = ?";
        Vector<String> arguments = new Vector<String>();
        arguments.add(key);
        Vector<String> strAuthData = null;
        try {
            execQuery(result, statement, arguments);
            throw new DataAccessException(ALREADY_EXISTS_EXCEPTION);
        } catch (SQLException e) {
            var check = e.getErrorCode();
            if (check == 0) {
                statement = "INSERT INTO auth (username, token) VALUES (?,?)";
                arguments.clear();
                arguments.add(username);
                arguments.add(key);
                try {
                    strAuthData = execUpdate(result, statement, arguments);
                } catch (SQLException ex) {
                    String out = "FATAL_ERROR::MYSqlDAO::execUpdate :: " + e.getMessage();
                    throw new DataAccessException(out);
                }

            } else {
                String out = "FATAL_ERROR::MYSqlDAO::execQuery :: " + e.getMessage();
                throw new DataAccessException(out);
            }
        }
        result = result.changeAuthToken(strAuthData.elementAt(2));
        result = result.changeUsername(strAuthData.elementAt(1));
        return result;
    }

    public AuthData killAuth(String key) throws DataAccessException {
        AuthData result = new AuthData(null, null);
        String statement = "SELECT * FROM auth WHERE token COLLATE utf8mb4_bin = ?";
        Vector<String> arguments = new Vector<String>();
        arguments.add(key);
        Vector<String> strAuthData = null;
        try {
            strAuthData = execQuery(result, statement, arguments);
        } catch (SQLException e) {
            var check = e.getErrorCode();
            if (check == 0) {
                throw new DataAccessException(NULL_RESULT_EXCEPTION);
            } else {
                String out = "FATAL_ERROR::MYSqlDAO::execQuery :: " + e.getMessage();
                throw new DataAccessException(out);
            }
        }
        statement = "DELETE FROM auth WHERE token COLLATE utf8mb4_bin = ";
        arguments.clear();
        String arg = "'" + key + "'";
        arguments.add(arg);
        try {
            execUpdate(null, statement, arguments);
        } catch (SQLException e) {
            String out = "FATAL_ERROR::MYSqlDAO::execQuery :: " + e.getMessage();
            throw new DataAccessException(out);
        }
        return result;
    }

    public void killTable(String table) throws DataAccessException {
        Vector<String> arguments = new Vector<String>();
        arguments.add(table);
        String statement = "TRUNCATE TABLE ";
        try {
            execUpdate(null, statement, arguments);
        } catch (SQLException e) {
            String out = "FATAL_ERROR::MYSqlDAO::execUpdate :: " + e.getMessage();
            throw new DataAccessException(out);
        }
    }

    public AuthData getAuth(String token) throws DataAccessException {
        AuthData result = new AuthData(null, null);
        String statement = "SELECT * FROM auth WHERE token COLLATE utf8mb4_bin = ?";
        Vector<String> arguments = new Vector<String>();
        arguments.add(token);
        Vector<String> strAuthData = null;
        try {
            strAuthData = execQuery(result, statement, arguments);
        } catch (SQLException e) {
            var check = e.getErrorCode();
            if (check == 0) {
                throw new DataAccessException(NULL_RESULT_EXCEPTION);
            } else {
//                String out = "FATAL_ERROR::MYSqlDAO::execQuery :: " + e.getMessage();
//                throw new DataAccessException(out);
            }
        }
        result = result.changeAuthToken(strAuthData.elementAt(0));
        result = result.changeUsername(strAuthData.elementAt(1));
        return result;
    }

    public Vector<Vector<String>> getActiveGames() throws DataAccessException {
        String statement = "SELECT id,white,black,gameName FROM games";
        Vector<Vector<String>> result = new Vector<>();
        try {
            result = execActiveGames(statement);
        } catch (SQLException e) {
            String out = "FATAL_ERROR::MYSqlDAO::execQuery :: " + e.getMessage();
            throw new DataAccessException(out);
        }
        return result;
    }

    public GameData addGame(String name) throws DataAccessException {
        GameData result = new GameData(null, null, null, null, null);
        String statement = "INSERT INTO games (gameName,game) VALUES (?,?)";
        Vector<String> arguments = new Vector<String>();
        arguments.add(name);
        var gameInit = new ChessGame();
        String game = new Gson().toJson(gameInit);
        arguments.add(game);
        Vector<String> strGameData;
        Integer id = null;
        try {
            strGameData = execUpdate(result, statement, arguments);
            String strId = strGameData.elementAt(0);
            id = Integer.parseInt(strId);
        } catch (SQLException e) {
            String out = "FATAL_ERROR::MYSqlDAO::execUpdate :: " + e.getMessage();
            throw new DataAccessException(out);
        }
        result = result.changeGameName(name);
        result = result.changeGameID(id);
        return result;
    }

    public String joinGame(ChessGame.TeamColor color, String id, String token) throws DataAccessException {
        String statement;
        if (color == ChessGame.TeamColor.WHITE) {
            statement = "UPDATE games SET " + "white" + " = ? WHERE id = ?";
        } else {
            statement = "UPDATE games SET " + "black" + " = ? WHERE id = ?";
        }
        String name = getAuth(token).username();
        Integer intID = Integer.parseInt(id);
        try {
            id = execJoin(statement, intID, name);
        } catch (SQLException e) {
            String out = "FATAL_ERROR::MYSqlDAO::execUpdate :: " + e.getMessage();
            throw new DataAccessException(out);
        }
        return id;
    }

    private String execJoin(String statement, Integer id, String name) throws DataAccessException, SQLException {
        PreparedStatement preparedStatement = DatabaseManager.getConnection().prepareStatement(statement, RETURN_GENERATED_KEYS);

        preparedStatement.setInt(2, id);
        preparedStatement.setString(1, name);

        Integer outId = preparedStatement.executeUpdate();
        return outId.toString();
    }

    private Vector<Vector<String>> execActiveGames(String statement) throws DataAccessException, SQLException {
        PreparedStatement preparedStatement = DatabaseManager.getConnection().prepareStatement(statement);
        ResultSet set = preparedStatement.executeQuery();
        Vector<Vector<String>> result = new Vector<>();
        while (set.next()) {
            Vector<String> row = new Vector<>();
            row.add(String.valueOf(set.getInt(1)));
            row.add(set.getString(2));
            row.add(set.getString(3));
            row.add(set.getString(4));
            result.add(row);
        }
        return result;
    }

    private Vector<String> execUpdate(Object passThrough, String statement, Vector<String> arguments) throws DataAccessException, SQLException {
        if (passThrough == null) {
            String finalStatement = statement + arguments.elementAt(0);
            PreparedStatement preparedStatement = DatabaseManager.getConnection().prepareStatement(finalStatement);
            preparedStatement.executeUpdate();
        } else if (passThrough instanceof AuthData) {
            PreparedStatement preparedStatement = DatabaseManager.getConnection().prepareStatement(statement, RETURN_GENERATED_KEYS);
            for (int i = 1; i <= preparedStatement.getParameterMetaData().getParameterCount(); i++) {
                preparedStatement.setString(i, arguments.elementAt(i - 1));
            }
            preparedStatement.executeUpdate();
            var set = preparedStatement.getGeneratedKeys();
            set.next();
            Integer id = set.getInt(1);
            String validateStatement = "SELECT * FROM auth WHERE id = '" + id + "'";
            PreparedStatement valPreparedStatement = DatabaseManager.getConnection().prepareStatement(validateStatement, RETURN_GENERATED_KEYS);
            set = valPreparedStatement.executeQuery();
            set.next();
            Vector<String> output = new Vector<>();
            output.add(id.toString());
            output.add(set.getString(2));
            output.add(set.getString(3));

            return output;

        } else if (passThrough instanceof GameData) {
            PreparedStatement preparedStatement = DatabaseManager.getConnection().prepareStatement(statement, RETURN_GENERATED_KEYS);
            for (int i = 1; i <= preparedStatement.getParameterMetaData().getParameterCount(); i++) {
                preparedStatement.setString(i, arguments.elementAt(i - 1));
            }
            Integer id = preparedStatement.executeUpdate();
            Vector<String> output = new Vector<>();
            output.add(id.toString());

            return output;
        } else if (passThrough instanceof UserData) {
            PreparedStatement preparedStatement = DatabaseManager.getConnection().prepareStatement(statement, RETURN_GENERATED_KEYS);
            for (int i = 1; i <= preparedStatement.getParameterMetaData().getParameterCount(); i++) {
                preparedStatement.setString(i, arguments.elementAt(i - 1));
            }
            preparedStatement.executeUpdate();
            var set = preparedStatement.getGeneratedKeys();
            set.next();
            Integer id = set.getInt(1);
            String validateStatement = "SELECT * FROM user WHERE id = '" + id + "'";
            PreparedStatement valPreparedStatement = DatabaseManager.getConnection().prepareStatement(validateStatement, RETURN_GENERATED_KEYS);
            set = valPreparedStatement.executeQuery();
            set.next();
            Vector<String> output = new Vector<>();
            output.add(id.toString());
            output.add(set.getString(2));
            output.add(set.getString(3));
            output.add(set.getString(4));

            return output;
        } else {
            throw new DataAccessException(INVALID_TYPING_IN_CODE);
        }
        return null;
    }

    private Vector<String> execQuery(Object passThrough, String statement, Vector<String> arguments) throws DataAccessException, SQLException {
        if (passThrough == null) {
            String finalStatement = statement + arguments.elementAt(0);
            PreparedStatement preparedStatement = DatabaseManager.getConnection().prepareStatement(finalStatement);
            preparedStatement.executeQuery();
        } else if (passThrough instanceof AuthData) {
            PreparedStatement preparedStatement = DatabaseManager.getConnection().prepareStatement(statement, RETURN_GENERATED_KEYS);
            for (int i = 1; i <= preparedStatement.getParameterMetaData().getParameterCount(); i++) {
                preparedStatement.setString(i, arguments.elementAt(i - 1));
            }
            var set = preparedStatement.executeQuery();
            set.next();
            Vector<String> output = new Vector<>();
            Integer id = set.getInt(1);
            output.add(id.toString());
            output.add(set.getString(2));
            output.add(set.getString(3));

            return output;
        } else if (passThrough instanceof GameData) {
            PreparedStatement preparedStatement = DatabaseManager.getConnection().prepareStatement(statement, RETURN_GENERATED_KEYS);
            for (int i = 1; i <= preparedStatement.getParameterMetaData().getParameterCount(); i++) {
                preparedStatement.setString(i, arguments.elementAt(i - 1));
            }
            var set = preparedStatement.executeQuery();
            set.next();
            Vector<String> output = new Vector<>();
            Integer id = set.getInt(1);
            output.add(id.toString());
            output.add(set.getString(2));
            return output;
        } else if (passThrough instanceof UserData) {
            PreparedStatement preparedStatement = DatabaseManager.getConnection().prepareStatement(statement, RETURN_GENERATED_KEYS);
            for (int i = 1; i <= preparedStatement.getParameterMetaData().getParameterCount(); i++) {
                preparedStatement.setString(i, arguments.elementAt(i - 1));
            }
            var set = preparedStatement.executeQuery();
            set.next();
            Vector<String> output = new Vector<>();
            Integer id = set.getInt(1);
            output.add(id.toString());
            output.add(set.getString(2));
            output.add(set.getString(3));
            output.add(set.getString(4));

            return output;
        } else {
            throw new DataAccessException(INVALID_TYPING_IN_CODE);
        }
        return null;
    }
}
