package chess;

import java.util.HashSet;

public class Rules {

    public Rules(){

    }

    public HashSet movementRule(ChessBoard board, ChessPosition position, ChessPiece piece){
        var currBoard = board;
        var currPos = position;
        var currPiece = piece;
        var currIdent = piece.getPieceType();
        if (currIdent == ChessPiece.PieceType.BISHOP){
            return BishopMov(currBoard, currPos, currPiece);
        }else if(currIdent == ChessPiece.PieceType.KING){
            return KingMov(currBoard, currPos, currPiece);
        }else if(currIdent == ChessPiece.PieceType.QUEEN){
            return QueenMov(currBoard, currPos, currPiece);
        }else if(currIdent == ChessPiece.PieceType.KNIGHT){
            return KnightMov(currBoard, currPos, currPiece);
        }else if(currIdent == ChessPiece.PieceType.ROOK){
            return RookMov(currBoard, currPos, currPiece);
        }else if(currIdent == ChessPiece.PieceType.PAWN){
            return PawnMov(currBoard, currPos, currPiece);
        }else{
            return new HashSet<>();
        }
    }

    public HashSet BishopMov(ChessBoard board, ChessPosition position, ChessPiece piece){
        var currBoard = board;
        var currPos = position;
        var currPiece = piece;
        var currMoves = piece.GetMoves();


        return new HashSet<>();
    }

    public HashSet KingMov(ChessBoard board, ChessPosition position, ChessPiece piece){
        var currBoard = board;
        var currPos = position;
        var currPiece = piece;
        var currMoves = piece.GetMoves();


        return new HashSet<>();
    }

    public HashSet QueenMov(ChessBoard board, ChessPosition position, ChessPiece piece){
        var currBoard = board;
        var currPos = position;
        var currPiece = piece;
        var currMoves = piece.GetMoves();


        return new HashSet<>();
    }

    public HashSet KnightMov(ChessBoard board, ChessPosition position, ChessPiece piece){
        var currBoard = board;
        var currPos = position;
        var currPiece = piece;
        var currMoves = piece.GetMoves();


        return new HashSet<>();
    }

    public HashSet RookMov(ChessBoard board, ChessPosition position, ChessPiece piece){
        var currBoard = board;
        var currPos = position;
        var currPiece = piece;
        var currMoves = piece.GetMoves();


        return new HashSet<>();
    }

    public HashSet PawnMov(ChessBoard board, ChessPosition position, ChessPiece piece){
        var currBoard = board;
        var currPos = position;
        var currPiece = piece;
        var currMoves = piece.GetMoves();
        if(currMoves == 0){

            return new HashSet<>();
        }else if(currMoves > 0){

            return new HashSet<>();
        }else{
            return new HashSet<>();
        }
    }

}
