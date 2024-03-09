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
    final private UserData userData = new UserData(USER, PASS, MAIL);

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

        assertDoesNotThrow(() -> currTesting.createUser(userData));
    }

    @Test
    void createUserNot() throws DataAccessException {
        DataAccess currTesting = new DataAccess();
        currTesting.killEverything();

        currTesting.createUser(userData);
        assertThrows(DataAccessException.class, () -> currTesting.createUser(userData));
    }

}