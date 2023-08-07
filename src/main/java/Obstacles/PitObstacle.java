package Obstacles;
import Player.*;

import java.io.IOException;

import static Main.Main.boardObj;

public class PitObstacle extends Obstacle {

    int obstacleId;
    String obstacleName;
    Double obstacleEffect;

    @Override
    public int getObstacleId() {
        return obstacleId;
    }

    public void setObstacleId(int obstacleId) {
        this.obstacleId = obstacleId;
    }
    @Override
    public String getObstacleName() {
        return obstacleName;
    }

    @Override
    public void setObstacleName(String obstacleName) {
        this.obstacleName = obstacleName;
    }

    @Override
    public Double getObstacleEffect() {
        return obstacleEffect;
    }

    @Override
    public void setObstacleEffect(Double obstacleEffect) {
        this.obstacleEffect = obstacleEffect;
    }

    /**
     *
     * @param playerId
     * @param playerHeight
     * @param playerLane
     * @param boardLength
     * @param dice1
     * @param dice2
     * @throws IOException
     * @throws InterruptedException
     * This function handles the score half obstacle
     *  It checks if the player has a skip available using the playerIdToMoveSkipCounter map
     *  if skip is available, then the player move is skipped
     *  Else the possibility of sideways movement is checked.
     *  If a sideways movement is allowed, then sideways movements is made and the board is displayed
     *  Else, the current score is fetched from playerIdToScoreMap and is reduced by 5 and mapped again using the player id.
     *  And, since the move was made so, the skip counter for the player is updated to 0.
     */
    public void pitObstacleMovement (int playerId, int playerHeight, int playerLane,int boardLength, int dice1, int dice2) throws IOException, InterruptedException {
        try {
            Player playerObj = new Player();
            if (Player.playerIdToMoveSkipCounter.get(playerId)!=null && Player.playerIdToMoveSkipCounter.get(playerId) < 3) { // player has less than 3 skips
                int skipCount = Player.playerIdToMoveSkipCounter.get(playerId);
                skipCount++;                                               // skip counter incremented
                Player.playerIdToMoveSkipCounter.put(playerId, skipCount);
            } else {
                boolean sidewaysMovementChecker = playerObj.sidewaysMovementChecker(playerId, playerHeight, playerLane, boardLength, dice1, dice2);
                if (sidewaysMovementChecker) {
                    // sideways movement
                    System.out.println(" sideways movement made. ");
                } else { // backward move is not possible -> player has to move ahead and lose 5 points
                    double currentScore = Player.playerIdToScoreMap.get(playerId);
                    currentScore -= 5; // directly reducing player score by 5
                    Player.playerIdToScoreMap.put(playerId, currentScore);
                    // board need not be updated as the player remains on the current shell
                    boardObj.boardGUI();
                    Player.playerIdToMoveSkipCounter.put(playerId, 0); // successful move -> so skip counter is put back to 0;
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
