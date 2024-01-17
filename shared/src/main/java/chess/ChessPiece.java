package chess;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    ChessGame.TeamColor team = null;
    PieceType identity = null;

    private static boolean isValidTeam(String input) {
        for (ChessGame.TeamColor main_body : ChessGame.TeamColor.values()) {
            if (main_body.name().equals(input)) {
                return true;
            }
        }
        return false;
    }
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        if (isValidTeam(pieceColor.toString())){
            team = pieceColor;
        }else{
            throw new RuntimeException("Invalid pieceColor upon instantiation of ChessPiece Constructor");
        }
        if(isValidType(type.toString())){
            identity = type;
        }else{
            throw new RuntimeException("Invalid PieceType upon instantiation of ChessPiece Constructor");
        }
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

    private static boolean isValidType(String input) {
        for (PieceType main_body : PieceType.values()) {
            if (main_body.name().equals(input)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        if (team != null) {
            return team;
        }else {
            throw new RuntimeException("ERROR, Team Color has not been initialized yet.");
        }
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }
}
