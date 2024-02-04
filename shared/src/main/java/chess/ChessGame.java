package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

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
            HashSet<ChessMove> All_Moves = null;
            if (Field.getBoard()[Row][Col] != null) {
                All_Moves = Field.getBoard()[Row][Col].pieceMoves(Field, startPosition);
            } else {
                return null;
            }
            HashSet<ChessMove> Clean_Moves = remove_invalid_moves(All_Moves);
            ChessPiece.PieceType type = null;
            if (Field.getBoard()[Row][Col] != null) {
                type = Field.getBoard()[Row][Col].getPieceType();
            } else {
                type = null;
            }
            if (type == ChessPiece.PieceType.PAWN) {
                HashSet<ChessMove> movesToRemove = new HashSet<>();
                for (var move : Clean_Moves) {
                    if ((move.getEndPosition().getArrayRow() == 0) || (move.getEndPosition().getArrayRow() == 7)) {
//                        movesToRemove.add(move);
                        Clean_Moves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.QUEEN));
                        Clean_Moves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.BISHOP));
                        Clean_Moves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.KNIGHT));
                        Clean_Moves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.ROOK));
                    }
                }
//                Clean_Moves.removeAll(movesToRemove);
            }
            return Clean_Moves;
        } else {
            throw new RuntimeException("validMoves()::ChessPosition startPosition = INVALID_LOCATION");
        }
    }

    private HashSet<ChessMove> remove_invalid_moves(Collection<ChessMove> old) {
        HashSet<ChessMove> output = new HashSet<ChessMove>();
        for (ChessMove curr : old) {
            ChessGame tmp = this.dupe();
            try {
                tmp.checkMove(curr);

                //This will only happen if the move is valid
                output.add(curr);
            } catch (InvalidMoveException e) {

            }
        }
        return output;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */

    private void checkMove(ChessMove move) throws InvalidMoveException {
        var end_pos = move.getEndPosition();
        var start_pos = move.getStartPosition();
        TeamColor my_team = null;
        if (Field.getBoard()[start_pos.getArrayRow()][start_pos.getArrayColumn()] != null) {
            my_team = Field.getBoard()[start_pos.getArrayRow()][start_pos.getArrayColumn()].getTeamColor();
        } else {
            my_team = null;
        }

        Collection<ChessMove> valid = null;
        if (true) {
            var Row = start_pos.getArrayRow();
            var Col = start_pos.getArrayColumn();
            HashSet<ChessMove> All_Moves = null;
            if (Field.getBoard()[Row][Col] != null) {
                valid = Field.getBoard()[Row][Col].pieceMoves(Field, start_pos);
            } else {

            }
        }

        Boolean sentinal = false;
        if (valid == null) {
            throw new InvalidMoveException("NO_VALID_MOVES");
        }
        for (ChessMove curr : valid) {
            if (curr.getEndPosition().equals(end_pos)) {
                sentinal = true;
            }
        }
        //Check 1 -> Check if end_pos is valid move (No Check, Stall)
        if (sentinal) {
            var tmp = this.dupe();
            tmp.getBoard().movePiece(move);
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (tmp.getBoard().getBoard()[i][j] != null) {
                        if (tmp.getBoard().getBoard()[i][j].getTeamColor() != my_team) {
                            Collection<ChessMove> enemy_moves = tmp.getBoard().getBoard()[i][j].pieceMoves(tmp.getBoard(), new ChessPosition(i + 1, j + 1));
                            for (var enemy_move : enemy_moves) {
                                if (tmp.getBoard().getBoard()[enemy_move.getEndPosition().getArrayRow()][enemy_move.getEndPosition().getArrayColumn()] != null) {
                                    if (tmp.getBoard().getBoard()[enemy_move.getEndPosition().getArrayRow()][enemy_move.getEndPosition().getArrayColumn()].getPieceType() == ChessPiece.PieceType.KING) {
                                        throw new InvalidMoveException("Enemy_Can_Capture_King");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            throw new InvalidMoveException("Invalid_Move");
        }
    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        var end_pos = move.getEndPosition();
        var start_pos = move.getStartPosition();
        TeamColor my_team = null;
        if (Field.getBoard()[start_pos.getArrayRow()][start_pos.getArrayColumn()] != null) {
            my_team = Field.getBoard()[start_pos.getArrayRow()][start_pos.getArrayColumn()].getTeamColor();
        } else {
            my_team = null;
        }
        Collection<ChessMove> valid = validMoves(start_pos);
        Boolean sentinal = false;
        if (valid == null) {
            throw new InvalidMoveException("NO_VALID_MOVES");
        }
        for (ChessMove curr : valid) {
            if (curr.getEndPosition().equals(end_pos)) {
                sentinal = true;
            }
        }
        //Check 1 -> Check if end_pos is valid move (No Check, Stall)
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

    // Creates a copy of Field
    private ChessGame dupe() {
        ChessGame out = new ChessGame();
        out.Turn = Turn;
        out.Field = Field.deepCopy(); // Create a deep copy of ChessBoard
        return out;
    }
}
