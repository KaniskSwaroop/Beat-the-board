package Obstacles;

import Player.*;

import java.io.IOException;

import static Board.Board.board;
import static Main.Main.boardObj;

public class SwapObstacle extends Obstacle {
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
     * This function handles the score half obstacle
     *  It checks if the player has a skip available using the playerIdToMoveSkipCounter map
     *  if skip is available, then the player move is skipped
     *  Else the possibility of sideways movement is checked.
     *  If a sideways movement is allowed, then sideways movements is made and the board is displayed
     *  else the last player in the race is searched and a swap is made
     *      If no last player is found then the player is swapped to the start line of the current lane
     *      else last lane was also occupied then no swap was made at all
     *  In case of a successful swap the skip counter is set to 0 again for the player
     */
    public void swapObstacleMovement(int playerId, int playerHeight, int playerLane, int boardLength, int boardWidth, int dice1, int dice2) throws IOException, InterruptedException {
        try {
            Player playerObj = new Player();
            if (Player.playerIdToMoveSkipCounter.get(playerId)!= null && Player.playerIdToMoveSkipCounter.get(playerId) < 3) {
                // lucky player -> chance skipped
                int currentSkipCount = Player.playerIdToMoveSkipCounter.get(playerId);
                currentSkipCount++;
                boardObj.boardGUI();
                Player.playerIdToMoveSkipCounter.put(playerId, currentSkipCount);
            } else {
                /** player swapping **/
                // player swapping
                // traverse playerList and find a player with the least height just below the current player and swap the two players;
                // or traverse the board and find the player at the bottom and swap
                boolean sidewaysMovementChecker = playerObj.sidewaysMovementChecker(playerId, playerHeight, playerLane, boardLength, dice1, dice2);
                if (sidewaysMovementChecker) {
                    System.out.println("sideways movement made. ");
                } else {
                    // no other options. player has to swap;
                    boolean swapped = false;
                    for (int i = boardWidth - 1; i < playerHeight; i++) {
                        for (int j = 0; j < boardLength; j++) {
                            if (board.get(i).get(j) != 0 && board.get(i).get(j) != 11 && board.get(i).get(j) != 12 && board.get(i).get(j) != 13) {
                                int tempPlayerId = playerId;
                                playerId = board.get(i).get(j);
                                board.get(i).set(j, tempPlayerId);
                                swapped = true;
                                break; //or return
                            }
                        }
                        if (swapped)
                            break; // or return
                    }
                    if (swapped) {
                        boardObj.boardGUI();
                        Player.playerIdToMoveSkipCounter.put(playerId, 0); // successful swap
                    } else { // Corner case handled
                        board.get(playerHeight).set(playerLane, 0); // setting current lane to 0;
                        if (board.get(0).get(playerLane) == 0) {
                            System.out.println("moving player to start position because no other swapping was possible");
                            board.get(0).set(playerLane, playerId);
                            boardObj.boardGUI();
                            Player.playerIdToMoveSkipCounter.put(playerId, 0); // successful swap
                        } else {
                            boardObj.boardGUI();
                            System.out.println("player not swapped because start point was occupied");
                        }

                    }
                }

            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
