package Board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    void boardLengthTest(){
        Board boardObj = new Board();
        boardObj.setBoardLength(1);
        int boardLength = boardObj.getBoardLength();
        assertTrue(boardLength>2);
    }
    @Test
    void boardWidthTest(){
        Board boardObj = new Board();
        boardObj.setBoardWidth(1);
        int boardWidth = boardObj.getBoardWidth();
        assertTrue(boardWidth>2);
    }
    @Test
    void boardTest(){
        Board boardObj = new Board();
        boardObj.setBoardLength(6);
        boardObj.setBoardWidth(5);
        for(int i=0;i<boardObj.getBoardWidth();i++){
            for(int j=0;j<boardObj.getBoardLength();j++){
                assertFalse(Board.board.get(i).get(j)>=0);
            }
        }
    }
}