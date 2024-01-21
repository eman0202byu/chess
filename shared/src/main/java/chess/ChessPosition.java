package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    int Row = 0;
    int Col = 0;
    public ChessPosition(int row, int col) {
        Row = row;
        Col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return Row;
    }

    public void setRow(int row){
        Row = row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return Col;
    }

    public void setColumn(int col){
        Col = col;
    }
}


