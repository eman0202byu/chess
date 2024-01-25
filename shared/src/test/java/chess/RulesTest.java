package chess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static passoffTests.TestFactory.*;

class RulesTest {

    @Test
    void pawnMov() {
        validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | |P| | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                startPosition(3, 6),
                endPositions(new int[][]{{4, 6}, {4, 7}, {3, 7}, {2, 7}, {2, 6}, {2, 5}, {3, 5}, {4, 5}})
        );
    }
}