package dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;
import service.ChessService;
import service.ServiceReport;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class DaoTests {
    final private String USER = "Jack";
    final private String PASS = "password";
    final private String MAIL = "a@cat.com";
    final private String BAD_PASS = "Password";
    final private UserData userData = new UserData(USER, PASS, MAIL);
    final private UserData userDataBad = new UserData(USER, BAD_PASS, MAIL);

    final private String DIFF_USER = "John";
    final private String DIFF_PASS = "asdfghjkl";
    final private String DIFF_MAIL = "b@cat.com";
    final private UserData userDataDiff = new UserData(DIFF_USER, DIFF_PASS, DIFF_MAIL);

    @Test
    void killEverything() throws DataAccessException {
        DataAccess currTesting = new DataAccess();

        assertEquals(currTesting.killEverything(), true);
    }

    @Test
    void killEverythingNot() throws DataAccessException {
        DataAccess currTesting = new DataAccess();

        assertNotEquals(currTesting.killEverything(), false);
    }

    @Test
    void createUser() throws DataAccessException {
        DataAccess currTesting = new DataAccess();
        currTesting.killEverything();

        currTesting.createUser(userData);
        assertThrows(DataAccessException.class, () -> currTesting.createUser(userData));
    }

    @Test
    void createUserNot() throws DataAccessException {
        DataAccess currTesting = new DataAccess();
        currTesting.killEverything();

        assertDoesNotThrow(() -> currTesting.createUser(userData));
    }

    @Test
    void getAccount() throws DataAccessException {
        DataAccess currTesting = new DataAccess();
        currTesting.killEverything();

        currTesting.createUser(userData);
        assertThrows(DataAccessException.class, () -> currTesting.getAccount(userDataBad));
        assertThrows(DataAccessException.class, () -> currTesting.getAccount(userDataDiff));
    }

    @Test
    void getAccountNot() throws DataAccessException {
        DataAccess currTesting = new DataAccess();
        currTesting.killEverything();

        currTesting.createUser(userData);
        assertDoesNotThrow(() -> currTesting.getAccount(userData));
    }

    @Test
    void createAuth() throws DataAccessException {
        DataAccess currTesting = new DataAccess();
        currTesting.killEverything();
        assertThrows(RuntimeException.class, () -> currTesting.createAuth(new UserData(null, null, null)));
    }

    @Test
    void createAuthNot() throws DataAccessException {
        DataAccess currTesting = new DataAccess();
        currTesting.killEverything();

        currTesting.createUser(userData);
        assertDoesNotThrow(() -> currTesting.createAuth(userData));
    }

    @Test
    void killAuth() throws DataAccessException {
        DataAccess currTesting = new DataAccess();
        currTesting.killEverything();

        currTesting.createUser(userData);
        currTesting.createAuth(userData);
        assertThrows(DataAccessException.class, () -> currTesting.killAuth(new AuthData("0", "Jack")));
    }

    @Test
    void killAuthNot() throws DataAccessException {
        DataAccess currTesting = new DataAccess();
        currTesting.killEverything();

        currTesting.createUser(userData);
        var auth = currTesting.createAuth(userData);
        assertDoesNotThrow(() -> currTesting.killAuth(auth));
    }

    @Test
    void getAuth() throws DataAccessException {
        DataAccess currTesting = new DataAccess();
        currTesting.killEverything();

        currTesting.createUser(userData);
        assertThrows(DataAccessException.class, () -> currTesting.validate(new AuthData(null, null)));
    }

    @Test
    void getAuthNot() throws DataAccessException {
        DataAccess currTesting = new DataAccess();
        currTesting.killEverything();

        currTesting.createUser(userData);
        var auth = currTesting.createAuth(userData);
        assertDoesNotThrow(() -> currTesting.validate(auth));
    }

    @Test
    void addGame() throws DataAccessException {
        DataAccess currTesting = new DataAccess();
        currTesting.killEverything();

        currTesting.createUser(userData);
        assertThrows(RuntimeException.class, () -> currTesting.createGame(new GameData(null, null, null, null, null)));
    }

    @Test
    void addGameNot() throws DataAccessException {
        DataAccess currTesting = new DataAccess();
        currTesting.killEverything();

        assertDoesNotThrow(() -> currTesting.createGame(new GameData(null, null, null, "Game", null)));
    }

    @Test
    void getActiveGames() throws DataAccessException {
        DataAccess currTesting = new DataAccess();
        currTesting.killEverything();

        currTesting.createGame(new GameData(null, null, null, "Game", null));
        currTesting.createGame(new GameData(null, null, null, "Game", null));
        currTesting.createGame(new GameData(null, null, null, "Game1", null));
        currTesting.createGame(new GameData(null, null, null, "Game2", null));
        currTesting.createGame(new GameData(null, null, null, "Game3", null));

        assertEquals(5, currTesting.listActive().size());
    }

    @Test
    void getActiveGamesNot() throws DataAccessException {
        DataAccess currTesting = new DataAccess();
        currTesting.killEverything();

        currTesting.createGame(new GameData(null, null, null, "Game", null));
        currTesting.createGame(new GameData(null, null, null, "Game", null));
        currTesting.createGame(new GameData(null, "null", null, "Game1", null));
        currTesting.createGame(new GameData(null, null, "null", "Game2", null));
        currTesting.createGame(new GameData(null, "null", "null", "Game3", null));

        assertDoesNotThrow(currTesting::listActive);
    }

    @Test
    void joinGame() throws DataAccessException {
        DataAccess currTesting = new DataAccess();
        currTesting.killEverything();

        currTesting.createUser(userData);
        currTesting.createUser(userDataDiff);
        var user1Auth = currTesting.createAuth(userData);
        var user2Auth = currTesting.createAuth(userDataDiff);
        currTesting.createGame(new GameData(null, null, null, "Game", null));
        currTesting.createGame(new GameData(null, null, null, "Game", null));
        currTesting.createGame(new GameData(null, null, null, "Game1", null));
        currTesting.createGame(new GameData(null, null, null, "Game2", null));
        currTesting.createGame(new GameData(null, null, null, "Game3", null));
        currTesting.joinGame(ChessGame.TeamColor.WHITE, 3, user1Auth);
        currTesting.joinGame(ChessGame.TeamColor.BLACK, 3, user2Auth);
        currTesting.joinGame(ChessGame.TeamColor.BLACK, 4, user1Auth);
        currTesting.joinGame(ChessGame.TeamColor.WHITE, 5, user2Auth);
        HashSet<GameData> out = new HashSet<>();
        out.add(new GameData(1, null, null, "Game", null));
        out.add(new GameData(2, null, null, "Game", null));
        out.add(new GameData(3, USER, DIFF_USER, "Game1", null));
        out.add(new GameData(4, null, USER, "Game2", null));
        out.add(new GameData(5, DIFF_USER, null, "Game3", null));

        assertEquals(out, currTesting.listActive());
    }

    @Test
    void joinGameNot() throws DataAccessException {
        DataAccess currTesting = new DataAccess();
        currTesting.killEverything();

        currTesting.createUser(userData);
        currTesting.createUser(userDataDiff);
        var user1Auth = currTesting.createAuth(userData);
        var user2Auth = currTesting.createAuth(userDataDiff);
        currTesting.createGame(new GameData(null, null, null, "Game", null));
        currTesting.createGame(new GameData(null, null, null, "Game", null));
        currTesting.createGame(new GameData(null, null, null, "Game1", null));
        currTesting.createGame(new GameData(null, null, null, "Game2", null));
        currTesting.createGame(new GameData(null, null, null, "Game3", null));
        currTesting.joinGame(ChessGame.TeamColor.WHITE, 3, user1Auth);
        currTesting.joinGame(ChessGame.TeamColor.BLACK, 3, user2Auth);
        currTesting.joinGame(ChessGame.TeamColor.BLACK, 4, user1Auth);
        currTesting.joinGame(ChessGame.TeamColor.WHITE, 5, user2Auth);

        assertNotEquals(6, currTesting.listActive().size());
        assertNotEquals(4, currTesting.listActive().size());
        assertNotEquals(0, currTesting.listActive().size());
    }

}