package server;

import java.util.*;

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
        if (result.Status() == ChessService.StatusCodes.PASS) {
            response.status(200);
            return "";
        } else {
            response.status(500);
            return new Gson().toJson(ErrorReportGen(result));
        }
    }

    // Register endpoint handler
    private Object registerUser(Request request, Response response) {
        var user = new Gson().fromJson(request.body(), UserData.class);
        try {
            var auth = service.registerUser(user);
            response.status(200);
            return new Gson().toJson(auth);
        } catch (DataAccessException e) {
            ServiceReport report = new Gson().fromJson(e.getMessage(), ServiceReport.class);
            if (report.Status() == ChessService.StatusCodes.BADREQUEST) {
                response.status(400);
                return new Gson().toJson(ErrorReportGen(report));
            } else if (report.Status() == ChessService.StatusCodes.UNAUTHORIZED) {
                response.status(401);
                return new Gson().toJson(ErrorReportGen(report));
            } else if (report.Status() == ChessService.StatusCodes.ALREADYTAKEN) {
                response.status(403);
                return new Gson().toJson(ErrorReportGen(report));
            } else {
                response.status(500);
                return new Gson().toJson(ErrorReportGen(report));
            }
        }
    }

    // Login endpoint handler
    private Object loginUser(Request request, Response response) {
        var user = new Gson().fromJson(request.body(), UserData.class);
        var auth = new AuthData(null, null);
        try {
            auth = service.loginUser(user);
            response.status(200);
            return new Gson().toJson(auth);
        } catch (DataAccessException e) {
            ServiceReport report = new Gson().fromJson(e.getMessage(), ServiceReport.class);
            if (report.Status() == ChessService.StatusCodes.BADREQUEST) {
                response.status(400);
                return new Gson().toJson(ErrorReportGen(report));
            } else if (report.Status() == ChessService.StatusCodes.UNAUTHORIZED) {
                response.status(401);
                return new Gson().toJson(ErrorReportGen(report));
            } else if (report.Status() == ChessService.StatusCodes.ALREADYTAKEN) {
                response.status(403);
                return new Gson().toJson(ErrorReportGen(report));
            } else {
                response.status(500);
                return new Gson().toJson(ErrorReportGen(report));
            }
        }
    }

    // Logout endpoint handler
    private Object logoutUser(Request request, Response response) {
        var token = request.headers(AUTHTOKENHEADER);
        var auth = new AuthData(token, null);
        ServiceReport result = service.logoutUser(auth);
        if (result.Status() == ChessService.StatusCodes.PASS) {
            response.status(200);
            return "";
        } else if (result.Status() == ChessService.StatusCodes.BADREQUEST) {
            response.status(400);
            return new Gson().toJson(ErrorReportGen(result));
        } else if (result.Status() == ChessService.StatusCodes.UNAUTHORIZED) {
            response.status(401);
            return new Gson().toJson(ErrorReportGen(result));
        } else if (result.Status() == ChessService.StatusCodes.ALREADYTAKEN) {
            response.status(403);
            return new Gson().toJson(ErrorReportGen(result));
        } else {
            response.status(500);
            return new Gson().toJson(ErrorReportGen(result));
        }
    }

    // List games endpoint handler
    private Object listGames(Request request, Response response) {
        var token = request.headers(AUTHTOKENHEADER);
        var auth = new AuthData(token, null);
        try {
            var out = service.listGames(auth);
            var outArray = out.toArray();
            response.status(200);
            return new Gson().toJson(Map.of(MAPOFGAMEKEY, outArray));
        } catch (DataAccessException e) {
            ServiceReport report = new Gson().fromJson(e.getMessage(), ServiceReport.class);
            if (report.Status() == ChessService.StatusCodes.BADREQUEST) {
                response.status(400);
                return new Gson().toJson(ErrorReportGen(report));
            } else if (report.Status() == ChessService.StatusCodes.UNAUTHORIZED) {
                response.status(401);
                return new Gson().toJson(ErrorReportGen(report));
            } else if (report.Status() == ChessService.StatusCodes.ALREADYTAKEN) {
                response.status(403);
                return new Gson().toJson(ErrorReportGen(report));
            } else {
                response.status(500);
                return new Gson().toJson(ErrorReportGen(report));
            }
        }
    }

    // Create game endpoint handler
    private Object createGame(Request request, Response response) {
        var token = request.headers(AUTHTOKENHEADER);
        var gameName = new Gson().fromJson(request.body(), GameData.class);
        var auth = new AuthData(token, null);
        try {
            var out = service.createGames(auth, gameName);
            response.status(200);
            return new Gson().toJson(out);
        } catch (DataAccessException e) {
            ServiceReport report = new Gson().fromJson(e.getMessage(), ServiceReport.class);
            if (report.Status() == ChessService.StatusCodes.BADREQUEST) {
                response.status(400);
                return new Gson().toJson(ErrorReportGen(report));
            } else if (report.Status() == ChessService.StatusCodes.UNAUTHORIZED) {
                response.status(401);
                return new Gson().toJson(ErrorReportGen(report));
            } else if (report.Status() == ChessService.StatusCodes.ALREADYTAKEN) {
                response.status(403);
                return new Gson().toJson(ErrorReportGen(report));
            } else {
                response.status(500);
                return new Gson().toJson(ErrorReportGen(report));
            }
        }
    }

    // Join game endpoint handler
    private Object joinGame(Request request, Response response) {
        var token = request.headers(AUTHTOKENHEADER);
        ColorAndID lookLeft = new Gson().fromJson(request.body(), ColorAndID.class);
        var auth = new AuthData(token, null);
        ServiceReport result = service.joinGames(auth, lookLeft.playerColor, lookLeft.gameID);
        if (result.Status() == ChessService.StatusCodes.PASS) {
            response.status(200);
            return "";
        } else if (result.Status() == ChessService.StatusCodes.BADREQUEST) {
            response.status(400);
            return new Gson().toJson(ErrorReportGen(result));
        } else if (result.Status() == ChessService.StatusCodes.UNAUTHORIZED) {
            response.status(401);
            return new Gson().toJson(ErrorReportGen(result));
        } else if (result.Status() == ChessService.StatusCodes.ALREADYTAKEN) {
            response.status(403);
            return new Gson().toJson(ErrorReportGen(result));
        } else {
            response.status(500);
            return new Gson().toJson(ErrorReportGen(result));
        }
    }

    private ErrorReport ErrorReportGen(ServiceReport result) {
        String startString = "Error: " + result.Status().toString();
        String endString = " :: " + result.ErrorLogging();
        String out = startString + endString;
        return new ErrorReport(out);
    }
}
