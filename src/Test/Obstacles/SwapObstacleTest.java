package Obstacles;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SwapObstacleTest {
    @Test
    void swapTest() throws IOException, InterruptedException {
        SwapObstacle swap = new SwapObstacle();
        int playerId = 999;
        int playerHeight = 1;
        int playerLane = 2;
        int boardLength = 4;
        int boardWidth = 8;
        int dice1 = 2;
        int dice2 = 1;
        swap.swapObstacleMovement(playerId,playerHeight,playerLane,boardLength,boardWidth,dice1,dice2);
        assertTrue(playerId > 0 && playerHeight >= 0 && playerLane >=0 && dice1 !=0 && dice2 !=0 && boardLength >0);
        assertFalse(boardLength < playerHeight || boardWidth < playerLane || dice1 >4 || dice2 >4);
    }
}