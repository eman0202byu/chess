package model;

import chess.ChessGame;

public record GameData(Integer gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public GameData changeWhiteUsername(String newWhiteUsername) {
        return new GameData(gameID, newWhiteUsername, blackUsername, gameName, game);
    }

    public GameData changeBlackUsername(String newBlackUsername) {
        return new GameData(gameID, whiteUsername, newBlackUsername, gameName, game);
    }

    public GameData changeGameName(String newGameName) {
        return new GameData(gameID, whiteUsername, blackUsername, newGameName, game);
    }

    public GameData changeGameID(int newGameID) {
        return new GameData(newGameID, whiteUsername, blackUsername, gameName, game);
    }

    public GameData changeGame(ChessGame newGame) {
        return new GameData(gameID, whiteUsername, blackUsername, gameName, newGame);
    }
}
