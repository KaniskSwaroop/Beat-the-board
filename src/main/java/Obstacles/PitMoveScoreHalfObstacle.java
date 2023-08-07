package Obstacles;
import Player.*;

import java.io.IOException;

import static Main.Main.boardObj;

public class PitMoveScoreHalfObstacle extends Obstacle{
    String obstacleName;
    Double obstacleEffect;

    /**
     *
     * @param playerId
     * @param playerHeight
     * @param playerLane
     * @param boardLength
     * @param boardWidth
     * @param dice1
     * @param dice2
     * @throws IOException
     * @throws InterruptedException
     *
     * This function handles the score half obstacle
     * It checks if the player has a skip available using the playerIdToMoveSkipCounter map
     * if skip is available, then the player move is skipped
     * Else the possibility of sideways movement is checked.
     * If a sideways movement is allowed, then sideways movements is made and the board is displayed
     * Else, the current score is fetched from playerIdToScoreMap and is updated to half and mapped again using the player id.
     * And, since the move was made so, the skip counter for the player is updated to 0.
     */
    public void scoreHalfObstacleMovement (int playerId, int playerHeight, int playerLane, int boardLength, int boardWidth, int dice1, int dice2 ) throws IOException, InterruptedException {
        try {
            Player playerObj = new Player();
            if (Player.playerIdToMoveSkipCounter.get(playerId) < 3) {
                int currentSkipCount = Player.playerIdToMoveSkipCounter.get(playerId);
                currentSkipCount++;
                boardObj.boardGUI();
                Player.playerIdToMoveSkipCounter.put(playerId, currentSkipCount);
            } else {
                /** check for sideways movement move **/
                boolean sidewaysMovementChecker = playerObj.sidewaysMovementChecker(playerId, playerHeight, playerLane, boardLength, dice1, dice2);
                if (sidewaysMovementChecker) {
                    boardObj.boardGUI();
                    System.out.println("Sideways movement made for score half obstacle. ");
                } else {
                    System.out.println("Alas!! you just lost half of your points");
                    double currentScore = Player.playerIdToScoreMap.get(playerId);
                    currentScore /= 2;
                    boardObj.boardGUI();
                    Player.playerIdToScoreMap.put(playerId, currentScore);
                    Player.playerIdToMoveSkipCounter.put(playerId, 0);
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
