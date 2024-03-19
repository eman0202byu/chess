package ui;

import chess.ChessGame;
import chess.ChessPiece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerminalRenderBoardTest {

    @Test
    void renderFromGame() {
        ChessGame game = new ChessGame();
        game.getBoard().resetBoard();
        System.out.println("PRINTING NOW");
        TerminalRenderBoard renderer = new TerminalRenderBoard();
        renderer.renderFromGame(game);

        ChessPiece holding = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
    }
}