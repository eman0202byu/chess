package server;

import chess.ChessGame;

public class ColorAndID {
    public ChessGame.TeamColor playerColor;
    public Integer gameID;

    ColorAndID(ChessGame.TeamColor color, Integer id) {
        playerColor = color;
        gameID = id;
    }
}
