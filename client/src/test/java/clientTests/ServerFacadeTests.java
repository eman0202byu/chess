package clientTests;

import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import serverFacade.ServerFacade;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void register() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void login() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        var authData = facade.login("player1", "password");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void createGame() throws Exception {
        String gameName = "Avalon";
        AuthData auth = facade.register("player1", "password", "p1@email.com");
        GameData gameData = facade.createGame(auth.authToken(), gameName);
        assertTrue(gameData.gameID() != null);
    }

}
