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
        assertThrows(DataAccessException.class, () -> currTesting.killAuth(new AuthData("0", "Jack")));
    }

    @Test
    void killAuthNot() throws DataAccessException {
        DataAccess currTesting = new DataAccess();
        currTesting.killEverything();

        currTesting.createUser(userData);
        assertDoesNotThrow(() -> currTesting.killAuth(currTesting.createAuth(userData)));
    }

}