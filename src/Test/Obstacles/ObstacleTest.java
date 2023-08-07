package Obstacles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObstacleTest {
    @Test
    void obstacleIdTest(){
        Obstacle obstacleObj = new Obstacle();
        obstacleObj.setObstacleId(13);
        assertTrue(obstacleObj.getObstacleId() == 11 || obstacleObj.getObstacleId() == 12 || obstacleObj.getObstacleId() == 13);
        assertFalse(obstacleObj.getObstacleId() == 0);
    }

}