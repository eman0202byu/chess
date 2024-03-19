package serverFacade;

import chess.ChessGame;
import com.google.gson.Gson;


import com.google.gson.internal.LinkedTreeMap;
import exception.ResponseException;
import model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Vector;
import java.io.*;
import java.net.*;

public class ServerFacade {
    private static Integer port;
    private static String serverUrl;

    public ServerFacade(Integer serverPort, String url) {
        if (serverPort == null) {
            throw new RuntimeException("client::src::main::java::ServerFacade.ServerFacade::constructor::serverPort CAN NOT == NULL");
        }
        if (url == null) {
            throw new RuntimeException("client::src::main::java::ServerFacade.ServerFacade::constructor::url CAN NOT == NULL");
        }
        port = serverPort;
        serverUrl = url;
    }

    public static void clear() throws ResponseException {
        var path = "/db";
        makeRequest("DELETE", path, null, null, null);
    }

    public static AuthData register(String username, String password, String email) throws ResponseException {
        var path = "/user";
        UserData body = new UserData(username, password, email);
        AuthData result = makeRequest("POST", path, null, body, AuthData.class);
        return result;
    }

    public static AuthData login(String username, String password) throws ResponseException {
        var path = "/session";
        UserData body = new UserData(username, password, null);
        AuthData result = makeRequest("POST", path, null, body, AuthData.class);
        return result;
    }

    public static GameData createGame(String auth, String gameName) throws ResponseException {
        var path = "/game";
        GameData body = new GameData(null, null, null, gameName, null);
        GameData result = makeRequest("POST", path, auth, body, GameData.class);
        return result;
    }

    public static Vector<GameData> listGames(String auth) throws ResponseException {
        var path = "/game";
        var result = makeRequest("GET", path, auth, null, Map.class);
        var resultObj = result.get("games");
        ArrayList resultArray = new ArrayList<>((Collection<LinkedTreeMap>) resultObj);
        Vector<GameData> cleanResult = new Vector<>();
        for (Object elem : resultArray) {
            LinkedTreeMap fixedElem = (LinkedTreeMap) elem;
            Double doubleId = (Double) fixedElem.get("gameID");
            Integer intId = (int) Math.floor(doubleId);
            String white = (String) fixedElem.get("whiteUsername");
            String black = (String) fixedElem.get("blackUsername");
            String name = (String) fixedElem.get("gameName");

            GameData push = new GameData(intId, white, black, name, null);
            cleanResult.add(push);
        }
        return cleanResult;
    }


    public static GameData joinGame(String auth, String strId, ChessGame.TeamColor team) throws ResponseException {
        var path = "/game";
        Integer id = Integer.parseInt(strId);
        ColorAndID body = new ColorAndID(team, id);
        GameData result = makeRequest("PUT", path, auth, body, GameData.class);
        return result;
    }

    private static <T> T makeRequest(String method, String path, Object requestHead, Object requestBody, Class<T> responseClass) throws ResponseException {
        try {
            URL url = new URL(serverUrl + ":" + port + path);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            writeRequest(requestHead, requestBody, http);
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeRequest(Object requestHead, Object requestBody, HttpURLConnection http) throws IOException {
        if (requestHead != null || requestBody != null) {
            http.setRequestProperty("Content-Type", "application/json");
        }
        if (requestHead instanceof String) {
            http.setRequestProperty("authorization", (String) requestHead);
        }
        if (requestBody != null) {
            String reqData = new Gson().toJson(requestBody);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private static boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
