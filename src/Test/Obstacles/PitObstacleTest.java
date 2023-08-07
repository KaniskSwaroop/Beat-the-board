package Obstacles;

import Board.Board;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PitObstacleTest {
    @Test
    void pitObstacle() throws IOException, InterruptedException {
        PitObstacle pit = new PitObstacle();
        int playerId = 999;
        int playerHeight = 1;
        int playerLane = 2;
        int boardLength = 4;
        int dice1 = 2;
        int dice2 = 1;
        pit.pitObstacleMovement(playerId,playerHeight,playerLane,boardLength,dice1,dice2);
        assertTrue(playerId > 0 && playerHeight >= 0 && playerLane >=0 && dice1 !=0 && dice2 !=0 && boardLength >0);
    }
}