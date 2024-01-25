package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {


    ChessPosition start = null;
    ChessPosition end = null;
    ChessPiece.PieceType update = null;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        start = startPosition;
        end = endPosition;
        update = promotionPiece;
    }

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition) {
        start = startPosition;
        end = endPosition;
        update = null;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return start;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return end;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return update;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(start, chessMove.start) && Objects.equals(end, chessMove.end) && update == chessMove.update;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, update);
    }

    @Override
    public String toString() {
        if (update != null) {
            return String.format("%s->%s::%s", start, end, update);
        } else {
            return String.format("%s->%s", start, end);
        }
    }
}
