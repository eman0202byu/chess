package server;

import java.util.*;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.*;
import service.*;
import spark.*;
import com.google.gson.Gson;


public class Server {
    private final ChessService service;
    private final String AUTHTOKENHEADER = "authorization";
    private final String MAPOFGAMEKEY = "games";

    public Server() {
        service = new ChessService();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Clear application endpoint
        Spark.delete("/db", this::clearDatabase);

        // Register endpoint
        Spark.post("/user", this::registerUser);

        // Login endpoint
        Spark.post("/session", this::loginUser);

        // Logout endpoint
        Spark.delete("/session", this::logoutUser);

        // List games endpoint
        Spark.get("/game", this::listGames);

        // Create game endpoint
        Spark.post("/game", this::createGame);

        // Join game endpoint
        Spark.put("/game", this::joinGame);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    // Clear application endpoint handler
    private Object clearDatabase(Request request, Response response) {
        var result = service.clearDatabase();
        if (result == null) {
            response.status(200);
            return "";
        } else {
            response.status(500);
            var output = new Gson().toJson(result);
            return output;
        }
    }

    // Register endpoint handler
    private Object registerUser(Request request, Response response) {
        var user = new Gson().fromJson(request.body(), UserData.class);
        try {
            user = service.registerUser(user);
        } catch (DataAccessException e) {
            response.status(501);
            return null; //TODO:: IMPLEMENT
        }
        response.status(200);
        return new Gson().toJson(user);
    }

    // Login endpoint handler
    private Object loginUser(Request request, Response response) {
        var user = new Gson().fromJson(request.body(), UserData.class);
        try {
            var auth = service.loginUser(user);
            response.status(200);
            return new Gson().toJson(auth);
        } catch (DataAccessException e) {
            response.status(501);
            return null; //TODO:: IMPLEMENT
        }
    }

    // Logout endpoint handler
    private Object logoutUser(Request request, Response response) {
        var auth = new Gson().fromJson(request.headers(AUTHTOKENHEADER), AuthData.class);
        var result = service.logoutUser(auth);
        if (result == null) {
            response.status(200);
            return "";
        } else {
            response.status(500);
            var output = new Gson().toJson(result);
            return output;
        }
    }

    // List games endpoint handler
    private Object listGames(Request request, Response response) {
        var auth = new Gson().fromJson(request.headers(AUTHTOKENHEADER), AuthData.class);
        try {
            var list = service.listGames(auth).toArray();
            response.status(200);
            return new Gson().toJson(Map.of(MAPOFGAMEKEY, list));
        } catch (DataAccessException e) {
            response.status(501);
            return null; //TODO:: IMPLEMENT
        }
    }

    // Create game endpoint handler
    private Object createGame(Request request, Response response) {
        var auth = new Gson().fromJson(request.headers(AUTHTOKENHEADER), AuthData.class);
        var gameName = new Gson().fromJson(request.body(), AuthData.class);
        try {
            var game = service.createGames(auth, gameName);
            response.status(200);
            return new Gson().toJson(game);
        } catch (DataAccessException e) {
            response.status(501);
            return null; //TODO:: IMPLEMENT
        }
    }

    // Join game endpoint handler
    private Object joinGame(Request request, Response response) {
        var auth = new Gson().fromJson(request.headers(AUTHTOKENHEADER), AuthData.class);
        ColorAndID lookLeft = new Gson().fromJson(request.body(), ColorAndID.class);
        var result = service.joinGames(auth, lookLeft.playerColor, lookLeft.gameID);
        if (result == null) {
            response.status(200);
            return "";
        } else {
            response.status(500);
            var output = new Gson().toJson(result);
            return output;
        }
    }
}
