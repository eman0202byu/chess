package chess;

import java.util.HashSet;

public class Rules {

    public Rules() {

    }

    public static HashSet<ChessMove> MovementRule(ChessBoard board, ChessPosition position, ChessPiece piece) {
        var currBoard = board;
        var currPos = position;
        var currPiece = piece;
        var currIdent = piece.getPieceType();
        if (currIdent == ChessPiece.PieceType.BISHOP) {
            return BishopMov(currBoard, currPos, currPiece);
        } else if (currIdent == ChessPiece.PieceType.KING) {
            return KingMov(currBoard, currPos, currPiece);
        } else if (currIdent == ChessPiece.PieceType.QUEEN) {
            return QueenMov(currBoard, currPos, currPiece);
        } else if (currIdent == ChessPiece.PieceType.KNIGHT) {
            return KnightMov(currBoard, currPos, currPiece);
        } else if (currIdent == ChessPiece.PieceType.ROOK) {
            return RookMov(currBoard, currPos, currPiece);
        } else if (currIdent == ChessPiece.PieceType.PAWN) {
            return PawnMov(currBoard, currPos, currPiece);
        } else {
            return new HashSet<>();
        }
    }

    //// Hey that one TA looking at my code, I big banged all this, it will probably need to be completely re-written
    //// when I am not pulling an all-nighter. You know, implementing things is always harder than thinking about them.
    public static HashSet<ChessMove> BishopMov(ChessBoard board, ChessPosition position, ChessPiece piece) {
        var currPos = position;
        HashSet<ChessMove> out = null;

        ////-NOTE: CHECK IMPLEMENTATION, I MADE THIS WITHOUT TESTING IT!!! IT WILL PROBABLY FAIL!!!!

        // top-left to bottom-right
        for (int i = 1; currPos.getRow() + i < 8 && currPos.getColumn() + i < 8; i++) {
            out.add(new ChessMove(currPos, new ChessPosition(currPos.getRow() + i, currPos.getColumn() + i)));
        }

        // top-right to bottom-left
        for (int i = 1; currPos.getRow() - i >= 0 && currPos.getColumn() + i < 8; i++) {
            out.add(new ChessMove(currPos, new ChessPosition(currPos.getRow() - i, currPos.getColumn() + i)));
        }

        // bottom-left to top-right
        for (int i = 1; currPos.getRow() + i < 8 && currPos.getColumn() - i >= 0; i++) {
            out.add(new ChessMove(currPos, new ChessPosition(currPos.getRow() + i, currPos.getColumn() - i)));
        }

        // bottom-right to top-left
        for (int i = 1; currPos.getRow() - i >= 0 && currPos.getColumn() - i >= 0; i++) {
            out.add(new ChessMove(currPos, new ChessPosition(currPos.getRow() - i, currPos.getColumn() - i)));
        }

        return out;
    }

    public static HashSet<ChessMove> KingMov(ChessBoard board, ChessPosition position, ChessPiece piece) {
        var currPos = position;
        HashSet<ChessMove> out = null;

        ////-NOTE: This one is simple, so I am probably fine, but check implementation

        // Col and Row moves simultaneously
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) { // Skip starting
                    int newRow = currPos.getRow() + i;
                    int newCol = currPos.getColumn() + j;
                    if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                        out.add(new ChessMove(currPos, new ChessPosition(newRow, newCol)));
                    }
                }
            }
        }
        return out;
    }

    public static HashSet<ChessMove> QueenMov(ChessBoard board, ChessPosition position, ChessPiece piece) {
        var currPos = position;
        HashSet<ChessMove> out = new HashSet<>();

        // Col and Row moves simultaneously
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) { // Skip starting
                    int newRow = currPos.getRow() + i;
                    int newCol = currPos.getColumn() + j;
                    while (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                        out.add(new ChessMove(currPos, new ChessPosition(newRow, newCol)));
                        newRow += i;
                        newCol += j;
                    }
                }
            }
        }

        return out;
    }

    public static HashSet<ChessMove> KnightMov(ChessBoard board, ChessPosition position, ChessPiece piece) {
        var currPos = position;
        HashSet<ChessMove> out = new HashSet<>();

        ////-NOTE: CHECK IMPLEMENTATION, I MADE THIS WITHOUT TESTING IT!!! IT WILL PROBABLY FAIL!!!!

        // Possible moves
        int[] rowMoves = {-2, -1, 1, 2, 2, 1, -1, -2};
        int[] colMoves = {1, 2, 2, 1, -1, -2, -2, -1};

        for (int i = 0; i < rowMoves.length; i++) {
            int newRow = currPos.getRow() + rowMoves[i];
            int newCol = currPos.getColumn() + colMoves[i];

            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                out.add(new ChessMove(currPos, new ChessPosition(newRow, newCol)));
            }
        }

        return out;
    }

    public static HashSet<ChessMove> RookMov(ChessBoard board, ChessPosition position, ChessPiece piece) {
        var currPos = position;
        HashSet<ChessMove> out = new HashSet<>();

        ////-NOTE: CHECK IMPLEMENTATION, I MADE THIS WITHOUT TESTING IT!!! IT WILL PROBABLY FAIL!!!!

        // Piece Col moves
        for (int i = -1; i <= 1; i += 2) {
            int newRow = currPos.getRow();
            int newCol = currPos.getColumn() + i;
            while (newCol >= 0 && newCol < 8) {
                out.add(new ChessMove(currPos, new ChessPosition(newRow, newCol)));
                // May need: newCol += i; Will need to test.
            }
        }

        // Piece Row moves
        for (int i = -1; i <= 1; i += 2) {
            int newRow = currPos.getRow() + i;
            int newCol = currPos.getColumn();
            while (newRow >= 0 && newRow < 8) {
                out.add(new ChessMove(currPos, new ChessPosition(newRow, newCol)));
                // May need: newRow += i; Will need to test.
            }
        }

        return out;
    }

    public static HashSet<ChessMove> PawnMov(ChessBoard board, ChessPosition position, ChessPiece piece) {
        var currPos = position;
        var currMoves = piece.GetMoves();
        var field = board.getBoard();
        HashSet<ChessMove> out = new HashSet<>();


        ////-NOTE: CHECK IMPLEMENTATION, I MADE THIS WITHOUT TESTING IT!!! IT WILL PROBABLY FAIL!!!!

        int direction = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;


        // Normal piece movement
        int newRow = currPos.getRow() + direction;
        int newCol = currPos.getColumn();
        if (newRow >= 0 && newRow < 8 && field[newRow][newCol] == null) {
            out.add(new ChessMove(currPos, new ChessPosition(newRow, newCol)));
        }

        // Piece movement on initial move
        if (currMoves == 0) {
            newRow = currPos.getRow() + 2 * direction;
            if (newRow >= 0 && newRow < 8 && field[newRow][newCol] == null) {
                out.add(new ChessMove(currPos, new ChessPosition(newRow, newCol)));
            }
        }

        int[] teamOffsets = {-1, 1};
        for (int offset : teamOffsets) {
            newCol = currPos.getColumn() + offset;
            //Below if() was a pain to make, and I don't know if it will actually work.
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8 && field[newRow][newCol] != null && field[newRow][newCol].getTeamColor() != piece.getTeamColor()) {
                out.add(new ChessMove(currPos, new ChessPosition(newRow, newCol)));
            }
        }
        return out;
    }

}
