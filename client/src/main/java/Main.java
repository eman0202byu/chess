import chess.*;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import serverFacade.ServerFacade;
import ui.EscapeSequences;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

import static ui.EscapeSequences.*;

public class Main {

    static private ServerFacade facade = new ServerFacade(999999, "404");

    static final String LOGGED_OUT = SET_TEXT_COLOR_BLACK + "[LOGGED_OUT] >>> ";
    static final String LOGGED_OUT_REGISTER = SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>" + SET_TEXT_COLOR_BLACK + " - " + SET_TEXT_COLOR_MAGENTA + "to create an account \n" + SET_TEXT_COLOR_BLACK;
    static final String LOGGED_OUT_LOGIN = SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>" + SET_TEXT_COLOR_BLACK + " - " + SET_TEXT_COLOR_MAGENTA + "to play chess \n" + SET_TEXT_COLOR_BLACK;
    static final String LOGGED_OUT_QUIT = SET_TEXT_COLOR_BLUE + "quit" + SET_TEXT_COLOR_BLACK + " - " + SET_TEXT_COLOR_MAGENTA + "playing chess \n" + SET_TEXT_COLOR_BLACK;
    static final String LOGGED_OUT_HELP = SET_TEXT_COLOR_BLUE + "help" + SET_TEXT_COLOR_BLACK + " - " + SET_TEXT_COLOR_MAGENTA + "with possible commands \n" + SET_TEXT_COLOR_BLACK;

    static final String LOGGED_OUT_VALID_COMMANDS = LOGGED_OUT_REGISTER + LOGGED_OUT_LOGIN + LOGGED_OUT_QUIT + LOGGED_OUT_HELP;

    static final String LOGGED_IN = SET_TEXT_COLOR_BLACK + "[LOGGED_IN] >>> ";
    static final String LOGGED_IN_CREATE = SET_TEXT_COLOR_BLUE + "create <NAME>" + SET_TEXT_COLOR_BLACK + " - " + SET_TEXT_COLOR_MAGENTA + "a game \n" + SET_TEXT_COLOR_BLACK;
    static final String LOGGED_IN_LIST = SET_TEXT_COLOR_BLUE + "list" + SET_TEXT_COLOR_BLACK + " - " + SET_TEXT_COLOR_MAGENTA + "games \n" + SET_TEXT_COLOR_BLACK;
    static final String LOGGED_IN_JOIN = SET_TEXT_COLOR_BLUE + "join <ID> [WHITE|BLACK|<empty>]" + SET_TEXT_COLOR_BLACK + " - " + SET_TEXT_COLOR_MAGENTA + "a game \n" + SET_TEXT_COLOR_BLACK;
    static final String LOGGED_IN_OBSERVE = SET_TEXT_COLOR_BLUE + "observe <ID>" + SET_TEXT_COLOR_BLACK + " - " + SET_TEXT_COLOR_MAGENTA + "a game \n" + SET_TEXT_COLOR_BLACK;
    static final String LOGGED_IN_LOGOUT = SET_TEXT_COLOR_BLUE + "logout" + SET_TEXT_COLOR_BLACK + " - " + SET_TEXT_COLOR_MAGENTA + "when you are done \n" + SET_TEXT_COLOR_BLACK;
    static final String LOGGED_IN_QUIT = SET_TEXT_COLOR_BLUE + "quit" + SET_TEXT_COLOR_BLACK + " - " + SET_TEXT_COLOR_MAGENTA + "playing chess \n" + SET_TEXT_COLOR_BLACK;
    static final String LOGGED_IN_HELP = SET_TEXT_COLOR_BLUE + "help" + SET_TEXT_COLOR_BLACK + " - " + SET_TEXT_COLOR_MAGENTA + "with possible commands \n" + SET_TEXT_COLOR_BLACK;
    static final String LOGGED_IN_VALID_COMMANDS = LOGGED_IN_CREATE + LOGGED_IN_LIST + LOGGED_IN_JOIN + LOGGED_IN_OBSERVE + LOGGED_IN_LOGOUT + LOGGED_IN_QUIT + LOGGED_IN_HELP;


    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);
        System.out.println(SET_TEXT_COLOR_BLACK + "♕Welcome to 240 chess. Type Help to get started. ♕ \n");

        Scanner scanner = new Scanner(System.in);

        HashMap<String, String> apiValues = new HashMap<>();
        apiValues.put("AUTH", null);
        apiValues.put("USER", null);
        apiValues.put("PASSWORD", null);
        apiValues.put("EMAIL", null);
        apiValues.put("GAME_ID", null);
        apiValues.put("GAME_NAME", null);
        apiValues.put("TEAM", null);
        while (true) {
            System.out.print(LOGGED_OUT);
            String inputString = scanner.nextLine().trim(); // Trim to remove leading/trailing whitespaces

            String[] parts = inputString.split(" ", 4); // Split the input by the first space
            String command = parts[0].toLowerCase();
            Boolean registered = false;

            switch (command) {
                case "help":
                    System.out.println(LOGGED_OUT_VALID_COMMANDS);
                    break;
                case "quit":
                    System.out.println(SET_TEXT_COLOR_YELLOW + "Exiting..." + SET_TEXT_COLOR_BLACK);
                    return;
                case "register":
                    if (parts.length != 4) {
                        System.out.println(SET_TEXT_COLOR_RED + "ERROR: INVALID ARGUMENTS" + SET_TEXT_COLOR_BLACK);
                        break;
                    }
                    System.out.println(SET_TEXT_COLOR_YELLOW + "Attempting to Register");
                    try {
                        AuthData result = facade.register(parts[1], parts[2], parts[3]);
                        apiValues.replace("API", result.authToken());
                    } catch (ResponseException e) {
                        System.out.println(SET_TEXT_COLOR_RED + "ERROR: " + e.getMessage() + SET_TEXT_COLOR_BLACK);
                        break;
                    }

                    // Success message
                    apiValues.replace("USER", parts[1]);
                    apiValues.replace("PASSWORD", parts[2]);
                    apiValues.replace("EMAIL", parts[3]);
                    System.out.println(SET_TEXT_COLOR_YELLOW + "Registered as " + parts[1] + SET_TEXT_COLOR_BLACK);
                    System.out.println(SET_TEXT_COLOR_YELLOW + "Logged in as " + parts[1] + SET_TEXT_COLOR_BLACK);
                    parts = new String[]{"login", parts[1], parts[2]};
                    registered = true;
                case "login":
                    if (parts.length != 3) {
                        if (!registered) {
                            System.out.println(SET_TEXT_COLOR_RED + "ERROR: INVALID ARGUMENTS" + SET_TEXT_COLOR_BLACK);
                            break;
                        }
                    }
                    if (!registered) {
                        System.out.println(SET_TEXT_COLOR_YELLOW + "Attempting to Login");
                        try {
                            AuthData result = facade.login(parts[1], parts[2]);
                            apiValues.replace("API", result.authToken());
                        } catch (ResponseException e) {
                            System.out.println(SET_TEXT_COLOR_RED + "ERROR: " + e.getMessage() + SET_TEXT_COLOR_BLACK);
                            break;
                        }

                        // Success message
                        apiValues.replace("AUTH", "RETURNED_STRING");
                        apiValues.replace("USER", parts[1]);
                        apiValues.replace("PASSWORD", parts[2]);
                        System.out.println(SET_TEXT_COLOR_YELLOW + "Logged in as " + parts[1] + SET_TEXT_COLOR_BLACK);
                    }
                    Boolean loggedIn = true;
                    while (loggedIn) {
                        System.out.print(LOGGED_IN);
                        inputString = scanner.nextLine().trim();
                        String[] loginParts = inputString.split(" ", 3); // Split the input by the first space
                        String loginCommand = loginParts[0].toLowerCase();
                        switch (loginCommand) {
                            case "create":
                                if (loginParts.length != 2) {
                                    System.out.println(SET_TEXT_COLOR_RED + "ERROR: INVALID ARGUMENTS" + SET_TEXT_COLOR_BLACK);
                                    break;
                                }
                                System.out.println(SET_TEXT_COLOR_YELLOW + "Creating game..." + SET_TEXT_COLOR_BLACK);
                                //Creates Game returns ID of new game made
                                try {
                                    GameData result = facade.createGame(apiValues.get("AUTH"), parts[1]);
                                    apiValues.replace("GAME_NAME", parts[1]);
                                    apiValues.replace("GAME_ID", result.gameID().toString());
                                } catch (ResponseException e) {
                                    System.out.println(SET_TEXT_COLOR_RED + "ERROR: " + e.getMessage() + SET_TEXT_COLOR_BLACK);
                                    break;
                                }
                                System.out.println(SET_TEXT_COLOR_YELLOW + "New Game ID: " + apiValues.get("GAME_ID") + SET_TEXT_COLOR_BLACK);
                                break;
                            case "list":
                                if (loginParts.length > 1) {
                                    System.out.println(SET_TEXT_COLOR_RED + "ERROR: INVALID ARGUMENTS" + SET_TEXT_COLOR_BLACK);
                                    break;
                                }
                                //Lists all games on server organized as the following:
                                //// (X == index) X. ID, GAME_NAME, WHITE_USER, BLACK_USER
                                Vector<GameData> result;
                                try {
                                    result = facade.listGames(apiValues.get("AUTH"));
                                } catch (ResponseException e) {
                                    System.out.println(SET_TEXT_COLOR_RED + "ERROR: " + e.getMessage() + SET_TEXT_COLOR_BLACK);
                                    break;
                                }
                                for (int i = 0; i < result.size(); i++) {
                                    Integer value = i;
                                    String push = value.toString() + ": ";
                                    push = push + "Game ID: " + result.elementAt(i).gameID().toString() + " ";
                                    push = push + "Game Name: " + result.elementAt(i).gameName() + " ";
                                    push = push + "White: " + result.elementAt(i).whiteUsername() + " ";
                                    push = push + "Black: " + result.elementAt(i).blackUsername();
                                    System.out.println(SET_TEXT_COLOR_YELLOW + push + SET_TEXT_COLOR_BLACK);
                                }

                                break;
                            case "join":
                                if (loginParts.length < 2) {
                                    System.out.println(SET_TEXT_COLOR_RED + "ERROR: INVALID ARGUMENTS" + SET_TEXT_COLOR_BLACK);
                                    break;
                                }
                                try {
                                    Integer.parseInt(loginParts[1]);
                                } catch (NumberFormatException e) {
                                    System.out.println(SET_TEXT_COLOR_RED + "ERROR: FIRST ARGUMENT IS NOT VALID NUMBER" + SET_TEXT_COLOR_BLACK);
                                    break;
                                }
                                ChessGame.TeamColor cleaned;
                                if (loginParts.length == 2) {
                                    cleaned = null;
                                } else if (loginParts[2].toLowerCase().equals("null")) {
                                    cleaned = null;
                                } else if (loginParts[2].toLowerCase().equals("white")) {
                                    cleaned = ChessGame.TeamColor.WHITE;
                                } else if (loginParts[2].toLowerCase().equals("black")) {
                                    cleaned = ChessGame.TeamColor.BLACK;
                                } else {
                                    System.out.println(SET_TEXT_COLOR_RED + "ERROR: SECOND ARGUMENT IS NOT VALID (WHITE/BLACK/NULL)" + SET_TEXT_COLOR_BLACK);
                                    break;
                                }
                                //Joins Game at given ID, and Color (If Color == null join as observer)
                                try {
                                    facade.joinGame(apiValues.get("AUTH"), loginParts[1], cleaned);
                                } catch (ResponseException e) {
                                    System.out.println(SET_TEXT_COLOR_RED + "ERROR: " + e.getMessage() + SET_TEXT_COLOR_BLACK);
                                    break;
                                }
                                // Displays board depending on Color being played.

                                break;
                            case "observe":
                                if (loginParts.length != 2) {
                                    System.out.println(SET_TEXT_COLOR_RED + "ERROR: INVALID ARGUMENTS" + SET_TEXT_COLOR_BLACK);
                                    break;
                                }
                                try {
                                    Integer.parseInt(loginParts[1]);
                                } catch (NumberFormatException e) {
                                    System.out.println(SET_TEXT_COLOR_RED + "ERROR: FIRST ARGUMENT IS NOT VALID NUMBER" + SET_TEXT_COLOR_BLACK);
                                    break;
                                }
                                //Joins game as observer, color = null
                                try {
                                    facade.joinGame(apiValues.get("AUTH"), loginParts[1], null);
                                } catch (ResponseException e) {
                                    System.out.println(SET_TEXT_COLOR_RED + "ERROR: " + e.getMessage() + SET_TEXT_COLOR_BLACK);
                                    break;
                                }
                                // Displays board from WHITE perspective.


                                break;
                            case "logout":
                                if (loginParts.length != 1) {
                                    System.out.println(SET_TEXT_COLOR_RED + "ERROR: INVALID ARGUMENTS" + SET_TEXT_COLOR_BLACK);
                                    break;
                                }

                                try {
                                    facade.logout(apiValues.get("AUTH"));
                                } catch (ResponseException e) {
                                    System.out.println(SET_TEXT_COLOR_RED + "ERROR: " + e.getMessage() + SET_TEXT_COLOR_BLACK);
                                    break;
                                }
                                apiValues.replace("AUTH", null);
                                apiValues.replace("USER", null);
                                apiValues.replace("PASSWORD", null);
                                apiValues.replace("EMAIL", null);
                                apiValues.replace("GAME_ID", null);
                                apiValues.replace("GAME_NAME", null);
                                apiValues.replace("TEAM", null);
                                //Success message
                                System.out.println(SET_TEXT_COLOR_YELLOW + "Logged out " + apiValues.get("USER") + SET_TEXT_COLOR_BLACK);
                                loggedIn = false;
                                break;
                            case "quit":
                                if (loginParts.length != 1) {
                                    System.out.println(SET_TEXT_COLOR_RED + "ERROR: INVALID ARGUMENTS" + SET_TEXT_COLOR_BLACK);
                                    break;
                                }
                                System.out.println(SET_TEXT_COLOR_YELLOW + "Exiting..." + SET_TEXT_COLOR_BLACK);
                                return;
                            case "help":
                                if (loginParts.length != 1) {
                                    System.out.println(SET_TEXT_COLOR_RED + "ERROR: INVALID ARGUMENTS" + SET_TEXT_COLOR_BLACK);
                                    break;
                                }
                                System.out.println(LOGGED_IN_VALID_COMMANDS);
                                break;
                            default:
                                System.out.println(SET_TEXT_COLOR_RED + "ERROR, INVALID COMMAND. TYPE: help" + SET_TEXT_COLOR_BLACK);
                                break;
                        }
                    }
                    break;
                default:
                    System.out.println(SET_TEXT_COLOR_RED + "ERROR, INVALID COMMAND. TYPE: help" + SET_TEXT_COLOR_BLACK);
                    break;
            }
        }
    }
}