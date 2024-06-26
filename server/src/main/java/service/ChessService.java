package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.*;
import dataAccess.DataAccess;

import java.util.HashSet;
import java.util.Objects;

public class ChessService {
    private DataAccess dataAccess;

    public ServiceReport makeMove(AuthData authData, Integer gameID, ChessMove move) {
        String result;
        try {
            result = dataAccess.getGame(gameID.toString(), authData.authToken());
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), dataAccess.DB_ALREADY_EXISTS_EXCEPTION)) {
                return new ServiceReport(StatusCodes.ALREADYTAKEN, e.getMessage());
            } else if (Objects.equals(e.getMessage(), dataAccess.DB_NULL_RESULT_EXCEPTION)) {
                return new ServiceReport(StatusCodes.BADREQUEST, e.getMessage());
            } else {
                return new ServiceReport(StatusCodes.DATAACCESSFAILURE, e.getMessage());
            }
        }
        var currentGame = new Gson().fromJson(result, ChessGame.class);
        String username;
        try {
            username = dataAccess.getUser(authData.authToken());
        } catch (DataAccessException e) {
            return new ServiceReport(StatusCodes.BADREQUEST, e.getMessage());
        }
        try {
            dataAccess.gameUsernameExist(username, gameID);
        } catch (DataAccessException e) {
            return new ServiceReport(StatusCodes.BADREQUEST, e.getMessage());
        }
        try {
            ChessPiece chessPiece = currentGame.getBoard().getBoard()[move.getStartPosition().getArrayRow()][move.getStartPosition().getArrayColumn()];
            ChessGame.TeamColor current = chessPiece.getTeamColor();
            if (current != currentGame.getTeamTurn()) {
                throw new InvalidMoveException();
            } else if ("black".equals(username)) {
                throw new InvalidMoveException();
            }
            if (currentGame.isInCheckmate(ChessGame.TeamColor.BLACK) || currentGame.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                throw new InvalidMoveException();
            } else if (currentGame.isInStalemate(ChessGame.TeamColor.BLACK) || currentGame.isInStalemate(ChessGame.TeamColor.WHITE)) {
                throw new InvalidMoveException();
            }
            currentGame.makeMove(move);
            try {
                dataAccess.replaceGame(currentGame, gameID.toString());
            } catch (DataAccessException e) {
                return new ServiceReport(StatusCodes.BADREQUEST, e.getMessage());
            }
        } catch (InvalidMoveException e) {
            return new ServiceReport(StatusCodes.BADREQUEST, e.getMessage());
        }
        return new ServiceReport(StatusCodes.PASS, null);
    }

    public ServiceReport leaveGame(AuthData authData, Integer gameID) {
        String result;
        try {
            result = dataAccess.getUser(authData.authToken());
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), dataAccess.DB_ALREADY_EXISTS_EXCEPTION)) {
                return new ServiceReport(StatusCodes.ALREADYTAKEN, e.getMessage());
            } else if (Objects.equals(e.getMessage(), dataAccess.DB_NULL_RESULT_EXCEPTION)) {
                return new ServiceReport(StatusCodes.BADREQUEST, e.getMessage());
            } else {
                return new ServiceReport(StatusCodes.DATAACCESSFAILURE, e.getMessage());
            }
        }
        try {
            dataAccess.removeFromGame(gameID.toString(), result);
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), dataAccess.DB_ALREADY_EXISTS_EXCEPTION)) {
                return new ServiceReport(StatusCodes.ALREADYTAKEN, e.getMessage());
            } else if (Objects.equals(e.getMessage(), dataAccess.DB_NULL_RESULT_EXCEPTION)) {
                return new ServiceReport(StatusCodes.BADREQUEST, e.getMessage());
            } else {
                return new ServiceReport(StatusCodes.DATAACCESSFAILURE, e.getMessage());
            }
        }
        return new ServiceReport(StatusCodes.PASS, null);
    }

    public ServiceReport resignGame(AuthData authData, Integer gameID) {
        String username;
        try {
            username = dataAccess.getUser(authData.authToken());
        } catch (DataAccessException e) {
            return new ServiceReport(StatusCodes.BADREQUEST, e.getMessage());
        }
        try {
            dataAccess.gameUsernameExist(username, gameID);
        } catch (DataAccessException e) {
            return new ServiceReport(StatusCodes.BADREQUEST, e.getMessage());
        }
        try {
            dataAccess.killGame(gameID.toString());
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), dataAccess.DB_ALREADY_EXISTS_EXCEPTION)) {
                return new ServiceReport(StatusCodes.ALREADYTAKEN, e.getMessage());
            } else if (Objects.equals(e.getMessage(), dataAccess.DB_NULL_RESULT_EXCEPTION)) {
                return new ServiceReport(StatusCodes.BADREQUEST, e.getMessage());
            } else {
                return new ServiceReport(StatusCodes.DATAACCESSFAILURE, e.getMessage());
            }
        }
        return new ServiceReport(StatusCodes.PASS, null);
    }

    public ServiceReport reservedSpot(AuthData authData, ChessGame.TeamColor playerColor, Integer gameID) {
        String username;
        try {
            username = dataAccess.getUser(authData.authToken());
        } catch (DataAccessException e) {
            return new ServiceReport(StatusCodes.BADREQUEST, e.getMessage());
        }
        try {
            dataAccess.gameUsernameColor(username, playerColor, gameID.toString());
        } catch (DataAccessException e) {
            return new ServiceReport(StatusCodes.BADREQUEST, e.getMessage());
        }
        return new ServiceReport(StatusCodes.PASS, null);
    }

    public enum StatusCodes {
        BADREQUEST,
        UNAUTHORIZED,
        ALREADYTAKEN,
        PASS,
        DATAACCESSFAILURE,
        EXTREME_ERROR,
    }

    public final String BAD_REQUEST_OUTSIDE_OF_SPEC_EXCEPTION = "BADREQUEST_BUT_BADREQUEST_IS_OUTSIDE_OF_SPEC";

    public ChessService() throws DataAccessException {
        try {
            dataAccess = new DataAccess();
        } catch (DataAccessException e) {
            ServiceReport report = new ServiceReport(StatusCodes.DATAACCESSFAILURE, e.getMessage());
            String rawReport = new Gson().toJson(report);
            throw new DataAccessException(rawReport);
        }
    }


    public ServiceReport clearDatabase() {
        try {
            dataAccess.killEverything();
            return new ServiceReport(StatusCodes.PASS, null);
        } catch (DataAccessException e) {
            return new ServiceReport(StatusCodes.DATAACCESSFAILURE, e.getMessage());
        }
    }

    public AuthData registerUser(UserData user) throws DataAccessException {
        String name = user.username();
        String pass = user.password();
        String mail = user.email();
        if (name == null || pass == null || mail == null) {
            ServiceReport report = new ServiceReport(StatusCodes.BADREQUEST, null);
            String rawReport = new Gson().toJson(report);
            throw new DataAccessException(rawReport);
        }
        try {
            var userObj = dataAccess.createUser(user);
            var authObj = dataAccess.createAuth(user);
            if (userObj == null) {
                ServiceReport report = new ServiceReport(StatusCodes.EXTREME_ERROR, "ERROR::ChessService::Line:50::userObj==null::true");
                String rawReport = new Gson().toJson(report);
                throw new DataAccessException(rawReport);
            }
            userObj.changeEmail(null);
            return authObj;
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), dataAccess.DB_ALREADY_EXISTS_EXCEPTION)) {
                ServiceReport report = new ServiceReport(StatusCodes.ALREADYTAKEN, e.getMessage());
                String rawReport = new Gson().toJson(report);
                throw new DataAccessException(rawReport);
            }
            ServiceReport report = new ServiceReport(StatusCodes.DATAACCESSFAILURE, e.getMessage());
            String rawReport = new Gson().toJson(report);
            throw new DataAccessException(rawReport);
        }
    }

    public AuthData loginUser(UserData user) throws DataAccessException {
        String name = user.username();
        String pass = user.password();
        if (name == null || pass == null) {
            ServiceReport report = new ServiceReport(StatusCodes.EXTREME_ERROR, BAD_REQUEST_OUTSIDE_OF_SPEC_EXCEPTION);
            String rawReport = new Gson().toJson(report);
            throw new DataAccessException(rawReport);
        }
        try {
            dataAccess.getAccount(user);
        } catch (DataAccessException e) {
            ServiceReport report = new ServiceReport(StatusCodes.UNAUTHORIZED, e.getMessage());
            String rawReport = new Gson().toJson(report);
            throw new DataAccessException(rawReport);
        }
        try {
            var authObj = dataAccess.createAuth(user);
            if (authObj == null) {
                ServiceReport report = new ServiceReport(StatusCodes.EXTREME_ERROR, "ERROR::ChessService::Line:73::authObj==null::true");
                String rawReport = new Gson().toJson(report);
                throw new DataAccessException(rawReport);
            }
            return authObj;
        } catch (DataAccessException e) {
            ServiceReport report = new ServiceReport(StatusCodes.DATAACCESSFAILURE, e.getMessage());
            String rawReport = new Gson().toJson(report);
            throw new DataAccessException(rawReport);
        }
    }

    public ServiceReport logoutUser(AuthData auth) {
        String token = auth.authToken();
        if (token == null) {
            return new ServiceReport(StatusCodes.EXTREME_ERROR, BAD_REQUEST_OUTSIDE_OF_SPEC_EXCEPTION);
        }
        try {
            var authObj = dataAccess.killAuth(auth);
            if (authObj == null) {
                ServiceReport report = new ServiceReport(StatusCodes.EXTREME_ERROR, "ERROR::ChessService::Line:95::authObj==null::true");
                String rawReport = new Gson().toJson(report);
                throw new DataAccessException(rawReport);
            }
            return new ServiceReport(StatusCodes.PASS, null);
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), dataAccess.DB_NULL_RESULT_EXCEPTION)) {
                return new ServiceReport(StatusCodes.UNAUTHORIZED, e.getMessage());
            } else {
                return new ServiceReport(StatusCodes.DATAACCESSFAILURE, e.getMessage());
            }
        }
    }

    public HashSet<GameData> listGames(AuthData auth) throws DataAccessException {
        String token = auth.authToken();
        if (token == null) {
            ServiceReport report = new ServiceReport(StatusCodes.EXTREME_ERROR, BAD_REQUEST_OUTSIDE_OF_SPEC_EXCEPTION);
            String rawReport = new Gson().toJson(report);
            throw new DataAccessException(rawReport);
        }

        ValidationCheck(auth);

        HashSet<GameData> dataAccessResults;
        try {
            dataAccessResults = dataAccess.listActive();
        } catch (DataAccessException e) {
            ServiceReport report = new ServiceReport(StatusCodes.DATAACCESSFAILURE, e.getMessage());
            String rawReport = new Gson().toJson(report);
            throw new DataAccessException(rawReport);
        }
        return dataAccessResults;
    }

    public GameData createGames(AuthData auth, GameData gameName) throws DataAccessException {
        String token = auth.authToken();
        String name = gameName.gameName();
        if (token == null || name == null) {
            ServiceReport report = new ServiceReport(StatusCodes.BADREQUEST, null);
            String rawReport = new Gson().toJson(report);
            throw new DataAccessException(rawReport);
        }

        ValidationCheck(auth);

        try {
            var gameObj = dataAccess.createGame(gameName);
            if (gameObj == null) {
                ServiceReport report = new ServiceReport(StatusCodes.EXTREME_ERROR, "ERROR::ChessService::Line:95::authObj==null::true");
                String rawReport = new Gson().toJson(report);
                throw new DataAccessException(rawReport);
            }
            gameObj = gameObj.changeBlackUsername(null);
            gameObj = gameObj.changeWhiteUsername(null);
            gameObj = gameObj.changeGameName(null);
            gameObj = gameObj.changeGame(null);
            return gameObj;
        } catch (DataAccessException e) {
            ServiceReport report = new ServiceReport(StatusCodes.DATAACCESSFAILURE, e.getMessage());
            String rawReport = new Gson().toJson(report);
            throw new DataAccessException(rawReport);
        }
    }

    public ServiceReport joinGames(AuthData auth, ChessGame.TeamColor color, Integer id) {
        String token = auth.authToken();
        if (token == null || id == null) {
            return new ServiceReport(StatusCodes.BADREQUEST, null);
        }
        try {
            ValidationCheck(auth);
        } catch (DataAccessException e) {
            var report = new Gson().fromJson(e.getMessage(), ServiceReport.class);
            if (Objects.equals(report.ErrorLogging(), dataAccess.DB_NULL_RESULT_EXCEPTION)) {
                return new ServiceReport(StatusCodes.UNAUTHORIZED, report.ErrorLogging());
            } else {
                return new ServiceReport(StatusCodes.DATAACCESSFAILURE, report.ErrorLogging());
            }
        }
        try {
            GameData gameObj = dataAccess.joinGame(color, id, auth);
            if (gameObj == null) {
                return new ServiceReport(StatusCodes.EXTREME_ERROR, "ERROR::ChessService::Line:173::authObj==null::true");
            }
            return new ServiceReport(StatusCodes.PASS, null);
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), dataAccess.DB_ALREADY_EXISTS_EXCEPTION)) {
                return new ServiceReport(StatusCodes.ALREADYTAKEN, e.getMessage());
            } else if (Objects.equals(e.getMessage(), dataAccess.DB_NULL_RESULT_EXCEPTION)) {
                return new ServiceReport(StatusCodes.BADREQUEST, e.getMessage());
            } else {
                return new ServiceReport(StatusCodes.DATAACCESSFAILURE, e.getMessage());
            }
        }
    }

    public void ValidationCheck(AuthData key) throws DataAccessException {
        try {
            dataAccess.validate(key);
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), dataAccess.DB_NULL_RESULT_EXCEPTION)) {
                ServiceReport report = new ServiceReport(StatusCodes.UNAUTHORIZED, e.getMessage());
                String rawReport = new Gson().toJson(report);
                throw new DataAccessException(rawReport);
            } else {
                ServiceReport report = new ServiceReport(StatusCodes.DATAACCESSFAILURE, e.getMessage());
                String rawReport = new Gson().toJson(report);
                throw new DataAccessException(rawReport);
            }
        }
    }
}
