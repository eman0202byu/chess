package chess;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    ChessGame.TeamColor team = null;
    PieceType identity = null;
    long moves = 0;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        identity = type;
        team = pieceColor;
    }

    public void SetMoves(long value) {
        moves = value;
    }

    public void IterateMoves() {
        moves = (moves + 1);
    }

    public long GetMoves() {
        return moves;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        if (team != null) {
            return team;
        } else {
            throw new RuntimeException("ERROR, Team Color has not been initialized yet.");
        }
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return identity;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessBoard currBoard = board;
        ChessPosition currPos = myPosition;
        var currPiece = board.getPiece(myPosition);

        if (currPiece != null) {
            return Rules.MovementRule(currBoard, currPos, currPiece);
        } else {
            return new HashSet<>();
        }
        //throw new RuntimeException("Not implemented" + "\nFatal error: ChessPiece.pieceMoves(Scope_Broken)");
    }
}
