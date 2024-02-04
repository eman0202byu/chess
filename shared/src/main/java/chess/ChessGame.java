package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    //Turn = Team Color (White = true, Black = false)
    Boolean Turn = true;
    //Field = Chess Board
    ChessBoard Field = new ChessBoard();

    public ChessGame(ChessBoard start) {
        Field = start;
    }

    public ChessGame() {
        Field.resetBoard();
    }


    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        if (Turn) {
            return TeamColor.WHITE;
        } else if (!Turn) {
            return TeamColor.BLACK;
        } else {
            throw new RuntimeException("getTeamTurn()::bool Turn != (true || false)");
        }
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        if (team == TeamColor.WHITE) {
            Turn = true;
        } else if (team == TeamColor.BLACK) {
            Turn = false;
        } else {
            throw new RuntimeException("setTeamTurn()::TeamColor team != (WHITE || BLACK)");
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if ((startPosition.getRow() <= 8 && startPosition.getColumn() <= 8) && (startPosition.getRow() >= 1 && startPosition.getColumn() >= 1)) {
            var Row = startPosition.getArrayRow();
            var Col = startPosition.getArrayColumn();
            Collection<ChessMove> Moves = Field.getBoard()[Row][Col].pieceMoves(Field, startPosition);
            return Moves;
        } else {
            throw new RuntimeException("validMoves()::ChessPosition startPosition = INVALID_LOCATION");
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        var end_pos = move.getEndPosition();
        var start_pos = move.getStartPosition();
        Collection<ChessMove> valid = validMoves(start_pos);
        Boolean sentinal = false;
        for (ChessMove curr : valid) {
            if (curr.getEndPosition().equals(end_pos)) {
                sentinal = true;
            }
        }
        if (sentinal) {
            Field.movePiece(move);
        } else {
            throw new InvalidMoveException("Invalid_Move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        Field = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return Field;
    }
}
