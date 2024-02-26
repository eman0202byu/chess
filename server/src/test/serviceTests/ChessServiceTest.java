package serviceTests;

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

class ChessServiceTest {
    private final String DNE_USERNAME = null;
    private final String DNE_AUTHTOKEN = "\\\\_I_ESCAPED_THE_\\\\_OUT_TO_TEST_THIS";
    private final String DNE_PASS = null;
    private final String DNE_EMAIL = null;
    private final String DNE_GAMENAME = null;
    private final Integer DNE_GAMEID = -999;
    private final String GOOD_USER = "exampUser";
    private final String GOOD_PASS = "Correct_Horse_Battery_Staple";
    private final String GOOD_EMAIL = "jack@gmail.com";
    private final Integer GOOD_GAMEID = 1;
    private final String GOOD_GAMENAME = "Just the Bois 日本語があります！";

    @Test
    void clearDatabase() {
        ChessService currTesting = new ChessService();
        DataAccess exceptionsGrab = new DataAccess();

        assertEquals(currTesting.clearDatabase(), new ServiceReport(ChessService.StatusCodes.PASS, null));
        assertNotEquals(currTesting.clearDatabase(), new ServiceReport(ChessService.StatusCodes.DATAACCESSFAILURE, exceptionsGrab.FAILURE_TO_DELETE_AUTH_TABLE_EXCEPTION));
        assertNotEquals(currTesting.clearDatabase(), new ServiceReport(ChessService.StatusCodes.DATAACCESSFAILURE, exceptionsGrab.FAILURE_TO_DELETE_GAME_TABLE_EXCEPTION));
        assertNotEquals(currTesting.clearDatabase(), new ServiceReport(ChessService.StatusCodes.DATAACCESSFAILURE, exceptionsGrab.FAILURE_TO_DELETE_USER_TABLE_EXCEPTION));
        assertNotEquals(currTesting.clearDatabase(), new ServiceReport(ChessService.StatusCodes.DATAACCESSFAILURE, exceptionsGrab.DB_UNABLE_TO_REMOVE_EXCEPTION));
    }

    @Test
    void registerUser() throws DataAccessException {
        UserData badUser = new UserData(DNE_USERNAME, DNE_PASS, DNE_EMAIL);
        UserData goodUser = new UserData(GOOD_USER, GOOD_PASS, GOOD_EMAIL);
        ChessService currTesting = new ChessService();

        assertThrows(DataAccessException.class, () -> currTesting.registerUser(badUser));
        assertDoesNotThrow(() -> currTesting.registerUser(goodUser));

    }

    @Test
    void loginUser() throws DataAccessException {
        UserData badUser = new UserData(DNE_USERNAME, DNE_PASS, DNE_EMAIL);
        UserData goodUser = new UserData(GOOD_USER, GOOD_PASS, GOOD_EMAIL);
        ChessService currTesting = new ChessService();
        currTesting.registerUser(goodUser);

        assertThrows(DataAccessException.class, () -> currTesting.loginUser(badUser));
        assertDoesNotThrow(() -> currTesting.loginUser(goodUser));
    }

    @Test
    void logoutUser() throws DataAccessException {
        AuthData badAuth = new AuthData(DNE_AUTHTOKEN, DNE_USERNAME);
        UserData goodUser = new UserData(GOOD_USER, GOOD_PASS, GOOD_EMAIL);
        ChessService currTesting = new ChessService();
        var goodAuth = currTesting.registerUser(goodUser);
        DataAccess exceptionsGrab = new DataAccess();
        currTesting.loginUser(goodUser);

        assertNotEquals(currTesting.logoutUser(badAuth), new ServiceReport(ChessService.StatusCodes.PASS, null));
        assertEquals(currTesting.logoutUser(badAuth), new ServiceReport(ChessService.StatusCodes.UNAUTHORIZED, exceptionsGrab.DB_NULL_RESULT_EXCEPTION));
        assertNotEquals(currTesting.logoutUser(goodAuth), new ServiceReport(ChessService.StatusCodes.UNAUTHORIZED, exceptionsGrab.DB_NULL_RESULT_EXCEPTION));
        currTesting.loginUser(goodUser);
        assertEquals(currTesting.logoutUser(goodAuth), new ServiceReport(ChessService.StatusCodes.PASS, null));

    }

    @Test
    void listGames() throws DataAccessException {
        AuthData badAuth = new AuthData(DNE_AUTHTOKEN, DNE_USERNAME);
        GameData goodGame = new GameData(GOOD_GAMEID, GOOD_USER, GOOD_USER, GOOD_GAMENAME, new ChessGame());
        UserData goodUser = new UserData(GOOD_USER, GOOD_PASS, GOOD_EMAIL);
        ChessService currTesting = new ChessService();
        currTesting.registerUser(goodUser);
        var goodAuth = currTesting.loginUser(goodUser);
        currTesting.createGames(goodAuth, goodGame);

        assertThrows(DataAccessException.class, () -> currTesting.listGames(badAuth));
        assertDoesNotThrow(() -> currTesting.listGames(goodAuth));
    }

    @Test
    void createGames() throws DataAccessException {
        AuthData badAuth = new AuthData(DNE_AUTHTOKEN, DNE_USERNAME);
        GameData goodGame = new GameData(GOOD_GAMEID, GOOD_USER, GOOD_USER, GOOD_GAMENAME, new ChessGame());
        UserData goodUser = new UserData(GOOD_USER, GOOD_PASS, GOOD_EMAIL);
        ChessService currTesting = new ChessService();
        currTesting.registerUser(goodUser);
        var goodAuth = currTesting.loginUser(goodUser);

        assertThrows(DataAccessException.class, () -> currTesting.createGames(badAuth, new GameData(null, null, null, DNE_GAMENAME, null)));
        assertDoesNotThrow(() -> currTesting.createGames(goodAuth, goodGame));
    }

    @Test
    void joinGames() throws DataAccessException {
        GameData goodGame = new GameData(GOOD_GAMEID, GOOD_USER, GOOD_USER, GOOD_GAMENAME, new ChessGame());
        UserData goodUser = new UserData(GOOD_USER, GOOD_PASS, GOOD_EMAIL);
        ChessService currTesting = new ChessService();
        DataAccess exceptionsGrab = new DataAccess();
        currTesting.registerUser(goodUser);
        var goodAuth = currTesting.loginUser(goodUser);
        var existingGame = currTesting.createGames(goodAuth, goodGame);

        assertNotEquals(currTesting.joinGames(goodAuth, null, DNE_GAMEID), new ServiceReport(ChessService.StatusCodes.PASS, null));
        assertEquals(currTesting.joinGames(goodAuth, null, DNE_GAMEID), new ServiceReport(ChessService.StatusCodes.BADREQUEST, exceptionsGrab.DB_NULL_RESULT_EXCEPTION));
        assertNotEquals(currTesting.joinGames(goodAuth, null, existingGame.gameID()), new ServiceReport(ChessService.StatusCodes.BADREQUEST, exceptionsGrab.DB_NULL_RESULT_EXCEPTION));
        assertEquals(currTesting.joinGames(goodAuth, null, existingGame.gameID()), new ServiceReport(ChessService.StatusCodes.PASS, null));
    }
}