package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.*;
import dataAccess.DataAccess;

import java.util.Collection;
import java.util.HashSet;

public class ChessService {
    private DataAccess dataAccess;

    public enum StatusCodes {
        UNAUTHORISED,
        BADAUTHTOKEN,
        BADCONTENT,
        DATAACCESSFAILURE
    }

    public ChessService() {
        dataAccess = new DataAccess();
    }


    public ServiceReport clearDatabase() {
        var status = new ServiceReport(StatusCodes.DATAACCESSFAILURE, "NOT_IMPLEMENTED");
        return status; //TODO:: IMPLEMENT
    }

    public UserData registerUser(UserData user) throws DataAccessException {
        var status = new ServiceReport(StatusCodes.DATAACCESSFAILURE, "NOT_IMPLEMENTED");
        return null; //TODO:: IMPLEMENT
    }

    public AuthData loginUser(UserData user) throws DataAccessException {
        var status = new ServiceReport(StatusCodes.DATAACCESSFAILURE, "NOT_IMPLEMENTED");
        return null; //TODO:: IMPLEMENT
    }

    public ServiceReport logoutUser(AuthData auth) {
        var status = new ServiceReport(StatusCodes.DATAACCESSFAILURE, "NOT_IMPLEMENTED");
        return status; //TODO:: IMPLEMENT
    }

    public HashSet<GameData> listGames(AuthData auth) throws DataAccessException {
        var status = new ServiceReport(StatusCodes.DATAACCESSFAILURE, "NOT_IMPLEMENTED");
        return null; //TODO:: IMPLEMENT
    }

    public GameData createGames(AuthData auth, AuthData gameName) throws DataAccessException {
        var status = new ServiceReport(StatusCodes.DATAACCESSFAILURE, "NOT_IMPLEMENTED");
        return null; //TODO:: IMPLEMENT
    }

    public ServiceReport joinGames(AuthData auth, ChessGame.TeamColor color, Integer id) {
        var status = new ServiceReport(StatusCodes.DATAACCESSFAILURE, "NOT_IMPLEMENTED");
        return status; //TODO:: IMPLEMENT
    }
}
