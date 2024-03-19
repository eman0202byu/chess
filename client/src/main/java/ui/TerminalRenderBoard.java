package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import javax.swing.*;

import static ui.EscapeSequences.*;

public class TerminalRenderBoard {
    final private Integer BOARD_SIZE = 8;

    public TerminalRenderBoard() {
        System.out.print(ERASE_SCREEN);
    }

    public void renderFromGame(ChessGame game) {
        ChessPiece[][] board = game.getBoard().getBoard();
        String boardWhite = renderWhite(board);
        String boardBlack = renderBlack(board);
        System.out.print(boardWhite);
        System.out.println();
        System.out.print(boardBlack);
    }

    private String renderWhite(ChessPiece[][] board) {
        StringBuilder sb = new StringBuilder();
        Boolean alternator = false;
        for (int i = BOARD_SIZE - 1; i > -1; i--) {
            if (i == 7) {
                sb.append(SET_BG_COLOR_LIGHT_GREY);
                sb.append(EMPTY);
                sb.append(RESET_BG_COLOR);
                for (int y = 1; y < 9; y++) {
                    sb.append(SET_BG_COLOR_LIGHT_GREY);
                    sb.append(SET_TEXT_BOLD);
                    sb.append(SET_TEXT_COLOR_BLACK);
                    String z = null;
                    if (y == 8) {
                        z = "  h";
                    } else if (y == 7) {
                        z = "  g ";
                    } else if (y == 6) {
                        z = " f ";
                    } else if (y == 5) {
                        z = "  e ";
                    } else if (y == 4) {
                        z = "  d ";
                    } else if (y == 3) {
                        z = "  c ";
                    } else if (y == 2) {
                        z = " b ";
                    } else if (y == 1) {
                        z = " a ";
                    }
                    sb.append(z);
                    sb.append(RESET_BG_COLOR);
                    sb.append(RESET_TEXT_COLOR);
                }
                sb.append(SET_BG_COLOR_LIGHT_GREY);
                sb.append(EMPTY);
                sb.append(RESET_BG_COLOR);
                sb.append("\n");
            }
            if (i != 7 || i != 0) {
                Integer num = i + 1;
                if (num == 8) {
                    num = 1;
                } else if (num == 7) {
                    num = 2;
                } else if (num == 6) {
                    num = 3;
                } else if (num == 5) {
                    num = 4;
                } else if (num == 4) {
                    num = 5;
                } else if (num == 3) {
                    num = 6;
                } else if (num == 2) {
                    num = 7;
                } else if (num == 1) {
                    num = 8;
                }
                sb.append(SET_BG_COLOR_LIGHT_GREY);
                sb.append(" ").append(num).append(" ");
                sb.append(RESET_BG_COLOR);
            }
            for (int j = 0; j < BOARD_SIZE; j++) {
                alternator = !alternator;
                if (alternator) {
                    //White
                    if (board[i][j] != null) {
                        sb.append(SET_BG_COLOR_WHITE);
                        ChessPiece.PieceType type = board[i][j].getPieceType();
                        ChessGame.TeamColor color = board[i][j].getTeamColor();
                        if (type == ChessPiece.PieceType.PAWN && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_PAWN);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.PAWN && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_PAWN);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.BISHOP && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_BISHOP);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.BISHOP && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_BISHOP);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.KNIGHT && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_KNIGHT);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.KNIGHT && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_KNIGHT);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.ROOK && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_ROOK);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.ROOK && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_ROOK);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.QUEEN && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_QUEEN);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.QUEEN && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_QUEEN);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.KING && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_KING);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.KING && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_KING);
                            sb.append(RESET_TEXT_COLOR);
                        } else {
                            sb.append("+");
                        }
                        sb.append(RESET_BG_COLOR);
                    } else {
                        sb.append(SET_BG_COLOR_WHITE);
                        sb.append(SET_TEXT_COLOR_WHITE);
                        sb.append(EMPTY);
                        sb.append(RESET_BG_COLOR);
                        sb.append(RESET_TEXT_COLOR);

                    }

                } else {
                    //Black
                    if (board[i][j] != null) {
                        sb.append(SET_BG_COLOR_BLACK);
                        ChessPiece.PieceType type = board[i][j].getPieceType();
                        ChessGame.TeamColor color = board[i][j].getTeamColor();
                        if (type == ChessPiece.PieceType.PAWN && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_PAWN);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.PAWN && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_PAWN);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.BISHOP && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_BISHOP);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.BISHOP && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_BISHOP);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.KNIGHT && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_KNIGHT);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.KNIGHT && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_KNIGHT);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.ROOK && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_ROOK);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.ROOK && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_ROOK);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.QUEEN && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_QUEEN);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.QUEEN && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_QUEEN);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.KING && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_KING);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.KING && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_KING);
                            sb.append(RESET_TEXT_COLOR);
                        } else {
                            sb.append("+");
                        }
                        sb.append(RESET_BG_COLOR);
                    } else {
                        sb.append(SET_BG_COLOR_BLACK);
                        sb.append(SET_TEXT_COLOR_BLACK);
                        sb.append(EMPTY);
                        sb.append(RESET_BG_COLOR);
                        sb.append(RESET_TEXT_COLOR);
                    }
                }

            }
            alternator = !alternator;
            if (i != 7 || i != 0) {
                Integer num = i + 1;
                if (num == 8) {
                    num = 1;
                } else if (num == 7) {
                    num = 2;
                } else if (num == 6) {
                    num = 3;
                } else if (num == 5) {
                    num = 4;
                } else if (num == 4) {
                    num = 5;
                } else if (num == 3) {
                    num = 6;
                } else if (num == 2) {
                    num = 7;
                } else if (num == 1) {
                    num = 8;
                }
                sb.append(SET_BG_COLOR_LIGHT_GREY);
                sb.append(" ").append(num).append(" ");
                sb.append(RESET_BG_COLOR);
            }
            sb.append('\n');
            if (i == 0) {
                sb.append(SET_BG_COLOR_LIGHT_GREY);
                sb.append(EMPTY);
                sb.append(RESET_BG_COLOR);
                for (int y = 1; y < 9; y++) {
                    sb.append(SET_BG_COLOR_LIGHT_GREY);
                    sb.append(SET_TEXT_BOLD);
                    sb.append(SET_TEXT_COLOR_BLACK);
                    String z = null;
                    if (y == 8) {
                        z = "  h";
                    } else if (y == 7) {
                        z = "  g ";
                    } else if (y == 6) {
                        z = " f ";
                    } else if (y == 5) {
                        z = "  e ";
                    } else if (y == 4) {
                        z = "  d ";
                    } else if (y == 3) {
                        z = "  c ";
                    } else if (y == 2) {
                        z = " b ";
                    } else if (y == 1) {
                        z = " a ";
                    }
                    sb.append(z);
                    sb.append(RESET_BG_COLOR);
                    sb.append(RESET_TEXT_COLOR);
                }
                sb.append(SET_BG_COLOR_LIGHT_GREY);
                sb.append(EMPTY);
                sb.append(RESET_BG_COLOR);
                sb.append("\n");
            }
        }
        String output = sb.toString();
        return output;
    }

    private String renderBlack(ChessPiece[][] board) {
        StringBuilder sb = new StringBuilder();
        Boolean alternator = false;
        for (int i = 0; i < 8; i++) {
            if (i == 0) {
                sb.append(SET_BG_COLOR_LIGHT_GREY);
                sb.append(EMPTY);
                sb.append(RESET_BG_COLOR);
                for (int y = 8; y > 0; y--) {
                    sb.append(SET_BG_COLOR_LIGHT_GREY);
                    sb.append(SET_TEXT_BOLD);
                    sb.append(SET_TEXT_COLOR_BLACK);
                    String z = null;
                    if (y == 8) {
                        z = "  h";
                    } else if (y == 7) {
                        z = "  g ";
                    } else if (y == 6) {
                        z = " f ";
                    } else if (y == 5) {
                        z = "  e ";
                    } else if (y == 4) {
                        z = "  d ";
                    } else if (y == 3) {
                        z = "  c ";
                    } else if (y == 2) {
                        z = " b ";
                    } else if (y == 1) {
                        z = " a ";
                    }
                    sb.append(z);
                    sb.append(RESET_BG_COLOR);
                    sb.append(RESET_TEXT_COLOR);
                }
                sb.append(SET_BG_COLOR_LIGHT_GREY);
                sb.append(EMPTY);
                sb.append(RESET_BG_COLOR);
                sb.append("\n");
            }
            if (i != 7 || i != 0) {
                Integer num = i + 1;
                sb.append(SET_BG_COLOR_LIGHT_GREY);
                sb.append(" ").append(num).append(" ");
                sb.append(RESET_BG_COLOR);
            }
            for (int j = BOARD_SIZE - 1; j > -1; j--) {
                alternator = !alternator;
                if (alternator) {
                    //White
                    if (board[i][j] != null) {
                        sb.append(SET_BG_COLOR_WHITE);
                        ChessPiece.PieceType type = board[i][j].getPieceType();
                        ChessGame.TeamColor color = board[i][j].getTeamColor();
                        if (type == ChessPiece.PieceType.PAWN && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_PAWN);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.PAWN && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_PAWN);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.BISHOP && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_BISHOP);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.BISHOP && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_BISHOP);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.KNIGHT && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_KNIGHT);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.KNIGHT && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_KNIGHT);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.ROOK && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_ROOK);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.ROOK && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_ROOK);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.QUEEN && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_QUEEN);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.QUEEN && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_QUEEN);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.KING && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_KING);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.KING && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_KING);
                            sb.append(RESET_TEXT_COLOR);
                        } else {
                            sb.append("+");
                        }
                        sb.append(RESET_BG_COLOR);
                    } else {
                        sb.append(SET_BG_COLOR_WHITE);
                        sb.append(SET_TEXT_COLOR_WHITE);
                        sb.append(EMPTY);
                        sb.append(RESET_BG_COLOR);
                        sb.append(RESET_TEXT_COLOR);

                    }

                } else {
                    //Black
                    if (board[i][j] != null) {
                        sb.append(SET_BG_COLOR_BLACK);
                        ChessPiece.PieceType type = board[i][j].getPieceType();
                        ChessGame.TeamColor color = board[i][j].getTeamColor();
                        if (type == ChessPiece.PieceType.PAWN && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_PAWN);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.PAWN && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_PAWN);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.BISHOP && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_BISHOP);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.BISHOP && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_BISHOP);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.KNIGHT && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_KNIGHT);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.KNIGHT && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_KNIGHT);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.ROOK && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_ROOK);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.ROOK && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_ROOK);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.QUEEN && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_QUEEN);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.QUEEN && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_QUEEN);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.KING && color == ChessGame.TeamColor.BLACK) {
                            sb.append(SET_TEXT_COLOR_BLUE);
                            sb.append(BLACK_KING);
                            sb.append(RESET_TEXT_COLOR);
                        } else if (type == ChessPiece.PieceType.KING && color == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                            sb.append(WHITE_KING);
                            sb.append(RESET_TEXT_COLOR);
                        } else {
                            sb.append("+");
                        }
                        sb.append(RESET_BG_COLOR);
                    } else {
                        sb.append(SET_BG_COLOR_BLACK);
                        sb.append(SET_TEXT_COLOR_BLACK);
                        sb.append(EMPTY);
                        sb.append(RESET_BG_COLOR);
                        sb.append(RESET_TEXT_COLOR);
                    }
                }

            }
            alternator = !alternator;
            if (i != 7 || i != 0) {
                Integer num = i + 1;
                sb.append(SET_BG_COLOR_LIGHT_GREY);
                sb.append(" ").append(num).append(" ");
                sb.append(RESET_BG_COLOR);
            }
            sb.append('\n');
            if (i == 7) {
                sb.append(SET_BG_COLOR_LIGHT_GREY);
                sb.append(EMPTY);
                sb.append(RESET_BG_COLOR);
                for (int y = 8; y > 0; y--) {
                    sb.append(SET_BG_COLOR_LIGHT_GREY);
                    sb.append(SET_TEXT_BOLD);
                    sb.append(SET_TEXT_COLOR_BLACK);
                    String z = null;
                    if (y == 8) {
                        z = "  h";
                    } else if (y == 7) {
                        z = "  g ";
                    } else if (y == 6) {
                        z = " f ";
                    } else if (y == 5) {
                        z = "  e ";
                    } else if (y == 4) {
                        z = "  d ";
                    } else if (y == 3) {
                        z = "  c ";
                    } else if (y == 2) {
                        z = " b ";
                    } else if (y == 1) {
                        z = " a ";
                    }
                    sb.append(z);
                    sb.append(RESET_BG_COLOR);
                    sb.append(RESET_TEXT_COLOR);
                }
                sb.append(SET_BG_COLOR_LIGHT_GREY);
                sb.append(EMPTY);
                sb.append(RESET_BG_COLOR);
                sb.append("\n");
            }
        }
        String output = sb.toString();
        return output;
    }

}
