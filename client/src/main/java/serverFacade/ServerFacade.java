package serverFacade;

import com.google.gson.Gson;


import exception.ResponseException;
import model.*;

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
        makeRequest("DELETE", path, null, null);
    }

    public static AuthData register(String username, String password, String email) throws ResponseException {
        var path = "/user";
        UserData body = new UserData(username, password, email);
        AuthData result = makeRequest("POST", path, body, AuthData.class);
        return result;
    }

    private static <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + ":" + port + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
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
