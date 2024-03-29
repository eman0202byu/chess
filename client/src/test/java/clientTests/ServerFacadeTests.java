package clientTests;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import serverFacade.ServerFacade;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {
    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port, "http://localhost");
    }

    @BeforeEach
    public void clear() throws ResponseException {
        ServerFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    public void sampleTestNot() {
        assertNotEquals(true, false);
    }

    @Test
    void register() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        assertTrue(authData.authToken().length() > 10);
    }

    void registerNot() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        assertFalse(authData.authToken().length() > 99999);
    }

    @Test
    void login() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        var authData = facade.login("player1", "password");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void loginNot() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        var authData = facade.login("player1", "password");
        assertFalse(authData.authToken().length() > 99999);
    }

    @Test
    void createGame() throws Exception {
        String gameName = "Avalon";
        AuthData auth = facade.register("player1", "password", "p1@email.com");
        GameData gameData = facade.createGame(auth.authToken(), gameName);
        assertTrue(gameData.gameID() != null);
    }

    @Test
    void createGameNot() throws Exception {
        String gameName = "Avalon";
        AuthData auth = facade.register("player1", "password", "p1@email.com");
        assertDoesNotThrow(() -> facade.createGame(auth.authToken(), gameName));
    }

    @Test
    void listGames() throws Exception {
        AuthData auth = facade.register("player1", "password", "p1@email.com");
        GameData gameData1 = facade.createGame(auth.authToken(), "one");
        GameData gameData2 = facade.createGame(auth.authToken(), "two");
        GameData gameData3 = facade.createGame(auth.authToken(), "three");
        GameData gameData4 = facade.createGame(auth.authToken(), "four");
        GameData gameDataOne = facade.createGame(auth.authToken(), "one");
        Vector<GameData> games = facade.listGames(auth.authToken());
        Vector<GameData> validation = new Vector<>();
        validation.add(new GameData(2, null, null, "two", null));
        validation.add(new GameData(5, null, null, "one", null));
        validation.add(new GameData(4, null, null, "four", null));
        validation.add(new GameData(1, null, null, "one", null));
        validation.add(new GameData(3, null, null, "three", null));
        assertEquals(validation.size(), games.size());
    }

    @Test
    void listGamesNot() throws Exception {
        AuthData auth = facade.register("player1", "password", "p1@email.com");
        GameData gameData1 = facade.createGame(auth.authToken(), "one");
        GameData gameData2 = facade.createGame(auth.authToken(), "two");
        GameData gameData3 = facade.createGame(auth.authToken(), "three");
        GameData gameData4 = facade.createGame(auth.authToken(), "four");
        GameData gameDataOne = facade.createGame(auth.authToken(), "one");
        assertDoesNotThrow(() -> facade.listGames(auth.authToken()));
    }

    @Test
    void joinGame() throws Exception {
        String gameName = "Avalon";
        AuthData auth = facade.register("player1", "password", "p1@email.com");
        GameData gameData = facade.createGame(auth.authToken(), gameName);
        facade.joinGame(auth.authToken(), "1", ChessGame.TeamColor.WHITE);
        Vector<GameData> games = facade.listGames(auth.authToken());
        Vector<GameData> validation = new Vector<>();
        validation.add(new GameData(1, "player1", null, gameName, null));
        assertEquals(validation, games);
    }

    @Test
    void joinGameNot() throws Exception {
        String gameName = "Avalon";
        AuthData auth = facade.register("player1", "password", "p1@email.com");
        GameData gameData = facade.createGame(auth.authToken(), gameName);
        assertDoesNotThrow(() -> facade.joinGame(auth.authToken(), "1", ChessGame.TeamColor.WHITE));
    }

    @Test
    void logout() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        var authData = facade.login("player1", "password");
        assertThrows(ResponseException.class, () -> facade.logout("NULL"));
    }

    @Test
    void logoutNot() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        var authData = facade.login("player1", "password");
        assertDoesNotThrow(() -> facade.logout(authData.authToken()));
    }

}
