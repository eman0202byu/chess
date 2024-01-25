package chess;

import java.util.Objects;

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


    public void setRow(int row) {
        Row = row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return Col;
    }

    public void setColumn(int col) {
        Col = col;
    }

    public int getArrayRow() {
        return (Row - 1);
    }

    public int getArrayColumn() {
        return (Col - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return Row == that.Row && Col == that.Col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Row, Col);
    }

    @Override
    public String toString() {
        return String.format("R%d:C%d", Row, Col);
    }
}


