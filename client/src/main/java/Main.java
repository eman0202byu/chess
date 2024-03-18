import chess.*;
import ui.EscapeSequences;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Main {

    static final String LOGGED_OUT = "[LOGGED_OUT] >>> ";
    static final String LOGGED_OUT_REGISTER = SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>" + SET_TEXT_COLOR_BLACK + " - " + SET_TEXT_COLOR_MAGENTA + "to create an account \n" + SET_TEXT_COLOR_BLACK;
    static final String LOGGED_OUT_LOGIN = SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>" + SET_TEXT_COLOR_BLACK + " - " + SET_TEXT_COLOR_MAGENTA + "to play chess \n" + SET_TEXT_COLOR_BLACK;
    static final String LOGGED_OUT_QUIT = SET_TEXT_COLOR_BLUE + "quit" + SET_TEXT_COLOR_BLACK + " - " + SET_TEXT_COLOR_MAGENTA + "playing chess \n" + SET_TEXT_COLOR_BLACK;
    static final String LOGGED_OUT_HELP = SET_TEXT_COLOR_BLUE + "help" + SET_TEXT_COLOR_BLACK + " - " + SET_TEXT_COLOR_MAGENTA + "with possible commands \n" + SET_TEXT_COLOR_BLACK;

    static final String LOGGED_OUT_VALID_COMMANDS = LOGGED_OUT_REGISTER + LOGGED_OUT_LOGIN + LOGGED_OUT_QUIT + LOGGED_OUT_HELP;

    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);
        System.out.println(SET_TEXT_COLOR_BLACK + "♕Welcome to 240 chess. Type Help to get started. ♕ \n");

        Scanner scanner = new Scanner(System.in);


        while (true) {
            System.out.print(LOGGED_OUT);
            String inputString = scanner.nextLine().trim(); // Trim to remove leading/trailing whitespaces

            String[] parts = inputString.split(" ", 2); // Split the input by the first space
            String command = parts[0].toLowerCase();
            String argument = parts.length > 1 ? parts[1] : ""; // If no space found, set argument to an empty string

            switch (command) {
                case "help":
                    System.out.println(LOGGED_OUT_VALID_COMMANDS);
                    break;
                case "quit":
                    System.out.println(SET_TEXT_COLOR_YELLOW + "Exiting" + SET_TEXT_COLOR_BLACK);
                    return;
                case "register":
                    System.out.println(SET_TEXT_COLOR_YELLOW + "Attempting to Register");
                    // API Register
                    System.out.println(SET_TEXT_COLOR_RED + "ERROR: FAILURE TO REACH SERVER" + SET_TEXT_COLOR_BLACK);
                    break;
                case "login":
                    System.out.println(SET_TEXT_COLOR_YELLOW + "Attempting to Login");
                    // API Login
                    // New While loop for Logged in
                    System.out.println(SET_TEXT_COLOR_RED + "ERROR: FAILURE TO REACH SERVER" + SET_TEXT_COLOR_BLACK);
                    break;
                default:
                    System.out.println(SET_TEXT_COLOR_RED + "ERROR, INVALID COMMAND. TYPE: help" + SET_TEXT_COLOR_BLACK);
            }
        }
    }
}