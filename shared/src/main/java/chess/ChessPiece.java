package chess;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

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

    public void promote(PieceType promotion) {
        identity = promotion;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return moves == that.moves && team == that.team && identity == that.identity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, identity, moves);
    }

    @Override
    public String toString() {
        switch (team) {
            case BLACK -> {
                switch (identity) {
                    case KING -> {
                        return "k";
                    }
                    case QUEEN -> {
                        return "q";
                    }
                    case BISHOP -> {
                        return "b";
                    }
                    case KNIGHT -> {
                        return "n";
                    }
                    case ROOK -> {
                        return "r";
                    }
                    case PAWN -> {
                        return "p";
                    }
                }
            }
            case WHITE -> {
                switch (identity) {
                    case KING -> {
                        return "K";
                    }
                    case QUEEN -> {
                        return "Q";
                    }
                    case BISHOP -> {
                        return "B";
                    }
                    case KNIGHT -> {
                        return "N";
                    }
                    case ROOK -> {
                        return "R";
                    }
                    case PAWN -> {
                        return "P";
                    }
                }
            }
            case null, default -> {
                return "0";
            }
        }
        return "8";
    }
}
