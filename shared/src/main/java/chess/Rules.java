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

    public static HashSet<ChessMove> FriendCheck(HashSet<ChessMove> old, ChessBoard board, ChessPiece piece){
        var out = new HashSet<ChessMove>();
        for(ChessMove curr : old){
            var start = curr.getStartPosition();
            var end = curr.getEndPosition();

            if (board.getBoard()[end.getArrayRow()][end.getArrayColumn()] == null) {
                out.add(curr);
            }else if(board.getBoard()[end.getArrayRow()][end.getArrayColumn()].getTeamColor() != piece.getTeamColor()){
                out.add(curr);
            }else{

            }
        }
        return out;
    }

    public static HashSet<ChessMove> BishopMov(ChessBoard board, ChessPosition position, ChessPiece piece) {
        var currPos = position;
        var field = board.getBoard();
        HashSet<ChessMove> out = new HashSet<ChessMove>();

        ////-NOTE: CHECK IMPLEMENTATION, I MADE THIS WITHOUT TESTING IT!!! IT WILL PROBABLY FAIL!!!!

        // top-left to bottom-right
        for (int i = 1; currPos.getArrayRow() + i < 8 && currPos.getArrayColumn() + i < 8; i++) {
            if (field[(currPos.getArrayRow() + i)][(currPos.getArrayColumn() + i)] == null) {
                out.add(new ChessMove(currPos, new ChessPosition(currPos.getRow() + i, currPos.getColumn() + i)));
            } else {
                out.add(new ChessMove(currPos, new ChessPosition(currPos.getRow() + i, currPos.getColumn() + i)));
                break;
            }
        }

        // top-right to bottom-left
        for (int i = 1; currPos.getArrayRow() - i >= 0 && currPos.getArrayColumn() + i < 8; i++) {
            if (field[(currPos.getArrayRow() - i)][(currPos.getArrayColumn() + i)] == null) {
                out.add(new ChessMove(currPos, new ChessPosition(currPos.getRow() - i, currPos.getColumn() + i)));
            } else {
                out.add(new ChessMove(currPos, new ChessPosition(currPos.getRow() - i, currPos.getColumn() + i)));
                break;
            }
        }

        // bottom-left to top-right
        for (int i = 1; currPos.getArrayRow() + i < 8 && currPos.getArrayColumn() - i >= 0; i++) {
            if (field[(currPos.getArrayRow() + i)][(currPos.getArrayColumn() - i)] == null) {
                out.add(new ChessMove(currPos, new ChessPosition(currPos.getRow() + i, currPos.getColumn() - i)));
            } else {
                out.add(new ChessMove(currPos, new ChessPosition(currPos.getRow() + i, currPos.getColumn() - i)));
                break;
            }
        }

        // bottom-right to top-left
        for (int i = 1; currPos.getArrayRow() - i >= 0 && currPos.getArrayColumn() - i >= 0; i++) {
            if (field[(currPos.getArrayRow() - i)][(currPos.getArrayColumn() - i)] == null) {
                out.add(new ChessMove(currPos, new ChessPosition(currPos.getRow() - i, currPos.getColumn() - i)));
            } else {
                out.add(new ChessMove(currPos, new ChessPosition(currPos.getRow() - i, currPos.getColumn() - i)));
                break;
            }
        }

        return FriendCheck(out,board,piece);
    }

    public static HashSet<ChessMove> KingMov(ChessBoard board, ChessPosition position, ChessPiece piece) {
        var currPos = position;
        HashSet<ChessMove> out = new HashSet<ChessMove>();

        ////-NOTE: This one is simple, so I am probably fine, but check implementation

        // Col and Row moves simultaneously
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) { // Skip starting
                    int newRow = currPos.getArrayRow() + i;
                    int newCol = currPos.getArrayColumn() + j;
                    if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                        out.add(new ChessMove(currPos, new ChessPosition((newRow + 1), (newCol + 1))));
                    }
                }
            }
        }
        return FriendCheck(out,board,piece);
    }

    public static HashSet<ChessMove> QueenMov(ChessBoard board, ChessPosition position, ChessPiece piece) {
        var currPos = position;
        var field = board.getBoard();
        HashSet<ChessMove> out = new HashSet<ChessMove>();

        // Col and Row moves simultaneously
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) { // Skip starting
                    int newRow = currPos.getArrayRow() + i;
                    int newCol = currPos.getArrayColumn() + j;
                    if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                        while ((newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) && ((field[newRow][newCol] == null) || (field[newRow][newCol] == null ? false : (field[newRow][newCol].getTeamColor() != piece.getTeamColor())))){
                            out.add(new ChessMove(currPos, new ChessPosition((newRow + 1), (newCol + 1))));
                            newRow += i;
                            newCol += j;
                            if(field[(newRow - i)][(newCol - j)] != null) {
                                if (field[(newRow - i)][(newCol - j)].getTeamColor() != piece.getTeamColor()) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        out = FriendCheck(out,board,piece);
        return out;
    }

    public static HashSet<ChessMove> KnightMov(ChessBoard board, ChessPosition position, ChessPiece piece) {
        var currPos = position;
        HashSet<ChessMove> out = new HashSet<ChessMove>();

        ////-NOTE: CHECK IMPLEMENTATION, I MADE THIS WITHOUT TESTING IT!!! IT WILL PROBABLY FAIL!!!!

        // Possible moves
        int[] rowMoves = {-2, -1, 1, 2, 2, 1, -1, -2};
        int[] colMoves = {1, 2, 2, 1, -1, -2, -2, -1};

        for (int i = 0; i < rowMoves.length; i++) {
            int newRow = currPos.getArrayRow() + rowMoves[i];
            int newCol = currPos.getArrayColumn() + colMoves[i];

            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                out.add(new ChessMove(currPos, new ChessPosition((newRow + 1), (newCol + 1))));
            }
        }
        return FriendCheck(out,board,piece);
    }

    public static HashSet<ChessMove> RookMov(ChessBoard board, ChessPosition position, ChessPiece piece) {
        var currPos = position;
        var field = board.getBoard();
        HashSet<ChessMove> out = new HashSet<ChessMove>();

        ////-NOTE: CHECK IMPLEMENTATION, I MADE THIS WITHOUT TESTING IT!!! IT WILL PROBABLY FAIL!!!!


        // Piece Row moves
        for (int i = -1; i <= 1; i += 2) {
            int newRow = currPos.getArrayRow();
            int newCol = currPos.getArrayColumn() + i;
            while ((newRow >= 0 && newRow < 8) && (newCol >= 0 && newCol < 8)) {
                if (field[newRow][newCol] == null) {
                    out.add(new ChessMove(currPos, new ChessPosition((newRow + 1), (newCol + 1))));
                    newCol += i;
                }else if(field[newRow][newCol].getTeamColor() != piece.getTeamColor()) {
                    out.add(new ChessMove(currPos, new ChessPosition((newRow + 1), (newCol + 1))));
                    newCol = 99;
                }else{
                    newCol = 19;
                }
            }
        }
        //// TODO:KNOWN ERROR -> COL 3 (array 2) IT thinks that you are in a different COL when doing these calculations

        // Piece Col moves
        for (int i = -1; i <= 1; i += 2) {
            int newRow = currPos.getArrayRow() + i;
            int newCol = currPos.getArrayColumn();
            while ((newRow >= 0 && newRow < 8) && (newCol >= 0 && newCol < 8)) {
                if (field[newRow][newCol] == null) {
                out.add(new ChessMove(currPos, new ChessPosition((newRow + 1), (newCol + 1))));
                newRow += i;
                }else if(field[newRow][newCol].getTeamColor() != piece.getTeamColor()) {
                    out.add(new ChessMove(currPos, new ChessPosition((newRow + 1), (newCol + 1))));
                    newCol = 98;
                }else{
                    newRow = 91;
                }
            }
        }

        out = FriendCheck(out,board,piece);
        return out;
    }

    public static HashSet<ChessMove> PawnMov(ChessBoard board, ChessPosition position, ChessPiece piece) {
        var currPos = position;
        var currMoves = piece.GetMoves();
        var field = board.getBoard();
        HashSet<ChessMove> out = new HashSet<ChessMove>();


        ////-NOTE: CHECK IMPLEMENTATION, I MADE THIS WITHOUT TESTING IT!!! IT WILL PROBABLY FAIL!!!!

        int direction = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;


        // Normal piece movement
        int newRow = currPos.getArrayRow() + direction;
        int newCol = currPos.getArrayColumn();
        if (newRow >= 0 && newRow < 8 && field[newRow][newCol] == null) {
            out.add(new ChessMove(currPos, new ChessPosition((newRow + 1), (newCol + 1))));
        }

        // Piece movement on initial move
        if (currPos.getRow() == 2 || currPos.getRow() == 7) {
            newRow = currPos.getArrayRow() + 2 * direction;
            if (newRow >= 0 && newRow < 8 && field[newRow][newCol] == null && field[newRow - direction][newCol] == null) {
                out.add(new ChessMove(currPos, new ChessPosition((newRow + 1), (newCol + 1))));
            }
        }


        // Capture
        int[] teamOffsets = {-1, 1};
        for (int offset : teamOffsets) {
            newCol = currPos.getArrayColumn() + offset;
            newRow = currPos.getArrayRow() + direction;
            //Below if() was a pain to make, and I don't know if it will actually work.
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8 && field[newRow][newCol] != null && field[newRow][newCol].getTeamColor() != piece.getTeamColor()) {
                out.add(new ChessMove(currPos, new ChessPosition((newRow + 1), (newCol + 1))));
            }
        }

        // Promotion
        HashSet<ChessMove> realOut = new HashSet<>();
        for (var a : out) {
            if (a.end.getRow() == 1 || a.end.getRow() == 8) {
                ChessMove cm = new ChessMove(a.start, a.end, ChessPiece.PieceType.KNIGHT);
                realOut.add(cm);
                cm = new ChessMove(a.start, a.end, ChessPiece.PieceType.BISHOP);
                realOut.add(cm);
                cm = new ChessMove(a.start, a.end, ChessPiece.PieceType.QUEEN);
                realOut.add(cm);
                cm = new ChessMove(a.start, a.end, ChessPiece.PieceType.ROOK);
                realOut.add(cm);
            } else {
                realOut.add(a);
            }
        }
        return realOut;
    }
}
