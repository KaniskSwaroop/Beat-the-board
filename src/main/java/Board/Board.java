package Board;

import Main.Main;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import static Main.HelloController.boardLength;
import static Main.HelloController.boardWidth;
import static Main.Main.playerIdToNameMap;
import static javafx.scene.paint.Color.WHITE;
import static javafx.scene.paint.Color.color;

public class Board {
    int boardWidth;
    int boardLength;

    public void setBoardWidth(int boardWidth) {
        System.out.println("board width in board setter : "+boardWidth);
        this.boardWidth = boardWidth;
        System.out.println("board width after setting in board : "+this.boardWidth);
    }

    public int getBoardWidth() {
        if(this.boardWidth<=0){
            throw new IllegalArgumentException("Please enter a valid board width");
        }
        return this.boardWidth;
    }

    public void setBoardLength(int boardLength) {
        this.boardLength = boardLength;
    }

    public int getBoardLength(){
        if(boardLength<=0){
            throw new IllegalArgumentException("Please enter a valid board length");
        }
        return boardLength;
    }
    public static ArrayList<ArrayList<Integer>> board = new ArrayList<>();

    /**
     * This functions creates a board with the loaded dimensions and each shell is initially assigned the value 0
     */
    public void creatingBoard (){
        for(int i=0;i<boardWidth;i++){
            ArrayList<Integer> lengthElements = new ArrayList<>(boardLength);
            for(int j=0;j<boardLength;j++){
                lengthElements.add(0);
            }
            board.add(lengthElements);  // populating all the elements with 0
        }
    }

    /**
     * there are 3 types of obstacles
     * pit obstacle -> If a player enters this pit then the player loses 5 points and is redirected to its previous position
     * Swap obstacle -> If a player enters this shell then it has to swap positions with the player who holds the last spot in the race.
     * Score half obstacle -> Player score is halved
     * The Id for pit obstacle is 11
     *  if board width < 4 then 1*1 blocks are used for pit obstacles
     *  variable till board length and board width are stored in 2 different array lists.
     *  The two array lists are shuffled using Collection.shuffle and the first two indices are used as the index for putting the obstacle.
     *  All the obstacles populations are made random using the above algorithm.
     *  if board width >4 and board length > 8 then the board is big enough for keeping 2*2 pit obstacles.
     *          Again the indices are kept in 2 array lists and shuffled to get random instances .
     *          The first (boardLength + boardWidth)/4 are taken as the shells for obstacles population
     *  else if board length is < 8 then first (boardLength+boardWidth)/8)+1 obstacles are taken from the shuffled array for placing the pits
     *  This number of pits is taken to be more if board size is high and less if board is small.
     *  Both are achieved using two mathematical operation -> (boardLength + boardWidth)/4 and (boardLength+boardWidth)/8)+1 for big and small boards respectively
     * The id for swap obstacle is 12
     *  This obstacle is only taken if the board is big enough i.e  board width and length >6
     *  The same obstacle of shuffle arrays are taken for randomly allocating the position of the obstacle
     * The id for score half obstacles is 13
     *  This obstacle halves the score of the player.
     *  Only used when board width and board length > 6
     *  All the obstacle populations on the board are completely random and the number of obstacles is dependent on the size of the board.
     */
    public void populateObstacleOnBoard(){
        ArrayList<Integer> obstacleIds = new ArrayList<>(3); //currently 3 obstacles are present
        Main.pitObstacle.setObstacleId(11);
        Main.swapObstacle.setObstacleId(12);
        Main.pitMoveScoreHalfObstacle.setObstacleId(13);
        obstacleIds.add(Main.pitObstacle.getObstacleId());
        obstacleIds.add(Main.swapObstacle.getObstacleId());
        obstacleIds.add(Main.pitMoveScoreHalfObstacle.getObstacleId());
        System.out.println("getObstacleId.size() : "+obstacleIds.size());
        for(int i=0;i<3;i++)
            System.out.println("obstacle id " +obstacleIds.get(i));
        for(int i=0;i<obstacleIds.size();i++){
            System.out.println("inside obstacle for loop ");
            switch(obstacleIds.get(i)){
                case 11:
                    System.out.println("testing pit obstacle code entry");
                    /** Task **/
                    // occupy (board length + board width)/4 number of 2*2 blocks if board width > 4
                    // occupy (board length + board width)/4 number of 1*1 blocks if board width < 4
                    ArrayList<Integer> pitPopulationWidth = new ArrayList<>();
                    ArrayList<Integer> pitPopulationLength = new ArrayList<>();
                    if(getBoardWidth() <= 4){
                        // occupy 1*1 random blocks with obstacle
                        if(getBoardLength() > 4){
                            //starting from one as the 1st lane is occupied by the starting point
                            for(int j=boardWidth-2;j>=0;j--)
                                pitPopulationWidth.add(j);
                            for(int j=0;j<getBoardLength();j++)
                                pitPopulationLength.add(j);
                            Collections.shuffle(pitPopulationWidth);
                            Collections.shuffle(pitPopulationLength);
                            for(int j=0;j<2;j++){ //getting first 2 elements from shuffled collection for adding the obstacle
                                for(int k=0;k<(pitPopulationLength.size())/4;k++){
                                    board.get(pitPopulationWidth.get(j)).set(pitPopulationLength.get(k),Main.pitObstacle.getObstacleId());
                                }
                            }
                            pitPopulationWidth.clear();
                            pitPopulationLength.clear();
                            System.out.println("1) did pit population array lists gets cleared ? :"+ pitPopulationWidth.size());
                        }
                        else{ // ERROR -> Test this corner case
                            // very small board -> do we really need 3 different obstacles ?
                            for(int j=1;j<getBoardWidth();j++)
                                pitPopulationWidth.add(j);
                            for(int j=0;j<getBoardLength();j++)
                                pitPopulationLength.add(j);
                            Collections.shuffle(pitPopulationWidth);
                            Collections.shuffle(pitPopulationLength);
                            board.get(pitPopulationWidth.get(0)).set(pitPopulationLength.get(0),Main.pitObstacle.getObstacleId()); //ERROR: Set according to obstacle ID
                            pitPopulationWidth.clear();
                            pitPopulationLength.clear();
                            System.out.println("2) did pit population array lists gets cleared ? :"+ pitPopulationWidth.size());
                        }
                    }
                    else{
                        if(getBoardLength() > 8){
                            //big enough board for multiple types of obstacles
                            // adding ((length+width)/4 + 1) number of pits
                            /** big enough board for 2*2 pits **/
                            for(int j=boardWidth - 2;j>0;j--) //starting from 2 because 1st is start line might consider arr[i-1][j-1] is obstacle matrix
                                pitPopulationWidth.add(j);
                            for(int j=1;j<boardLength-1;j++)
                                pitPopulationLength.add(j);
                            Collections.shuffle(pitPopulationWidth);
                            Collections.shuffle(pitPopulationLength);
                            // populating 2*2 shells with obstacle ID
                            for(int j=0;j<((boardLength + boardWidth)/4)+1;j++){
                                board.get(pitPopulationWidth.get(j)).set(pitPopulationLength.get(j),Main.pitObstacle.getObstacleId());
                                board.get(pitPopulationWidth.get(j)-1).set(pitPopulationLength.get(j),Main.pitObstacle.getObstacleId());
                                board.get(pitPopulationWidth.get(j)).set(pitPopulationLength.get(j)+1,Main.pitObstacle.getObstacleId());
                                board.get(pitPopulationWidth.get(j)-1).set(pitPopulationLength.get(j)+1,Main.pitObstacle.getObstacleId());
                            }
                            pitPopulationWidth.clear();
                            pitPopulationLength.clear();
                            System.out.println("3) did pit population array lists gets cleared ? :"+ pitPopulationWidth.size());
                        }
                        else{
                            // small length on the board -> so, might not need 3 different types of obstacles
                            // adding ((length+width)/8 + 1) number of pits
                            for(int j=boardWidth - 2;j>0;j--) //starting from 2 because 1st is start line might consider arr[i-1][j-1] is obstacle matrix
                            {
                                System.out.println("pitPopulationWidth j : "+j);
                                pitPopulationWidth.add(j);
                            }
                            for(int j=1;j<boardLength-1;j++)
                                pitPopulationLength.add(j);
                            Collections.shuffle(pitPopulationWidth);
                            Collections.shuffle(pitPopulationLength);
                            for(int j=0;j<((boardLength+boardWidth)/8)+1;j++){ // using first 2 elements in the random pit obstacle width and length array
                                board.get(pitPopulationWidth.get(j)).set(pitPopulationLength.get(j),Main.pitObstacle.getObstacleId());
                                board.get(pitPopulationWidth.get(j)-1).set(pitPopulationLength.get(j),Main.pitObstacle.getObstacleId());
                            }
                            pitPopulationWidth.clear();
                            pitPopulationLength.clear();
                            System.out.println("4) did pit population array lists gets cleared ? :"+ pitPopulationWidth.size());
                        }
                    }
                    break;
                case 12:
                    System.out.println("testing swap obstacle code entry");
                    /** Task **/
                    ArrayList<Integer> swapObstacleWidth = new ArrayList<>();
                    ArrayList<Integer> swapObstacleLength = new ArrayList<>();

                    //add 2 swap obstacles in the top half of the board;
                    //only if width and length of board > 6 -> else we don't need a swap obstacle
                    //setting 1*1 swap obstacle shell
                    if(getBoardWidth() > 6 && getBoardLength() > 6){
                        for(int j=getBoardWidth()/2;j>=0;j--) //starting from 2 because 1st is start line -> might consider arr[i-1][j-1] in obstacle matrix
                            swapObstacleWidth.add(j);
                        for(int j=1;j<getBoardLength()-1;j++)
                            swapObstacleLength.add(j);
                        Collections.shuffle(swapObstacleWidth);
                        Collections.shuffle(swapObstacleLength);
                        for(int j=0;j<2;j++){
                            board.get(swapObstacleWidth.get(j)).set(swapObstacleLength.get(j),Main.swapObstacle.getObstacleId());
                        }
                    }
                    break;
                case 13:
                    System.out.println("testing pit and score half obstacle code entry");
                    ArrayList<Integer> scoreHalfObstacleWidth = new ArrayList<>();
                    ArrayList<Integer> scoreHalfObstacleLength = new ArrayList<>();
                    if(boardWidth > 6 && boardLength > 6){
                        for(int j=boardWidth - 2;j>0;j--)//not going to the finish row because that would be the end of the race
                            scoreHalfObstacleWidth.add(j);
                        for(int j=0;j<boardLength;j++)
                            scoreHalfObstacleLength.add(j);
                        Collections.shuffle(scoreHalfObstacleWidth);
                        Collections.shuffle(scoreHalfObstacleLength);
                        for(int j=0;j<3;j++){ // keeping 3 obstacles with point half effect
                            if(board.get(scoreHalfObstacleWidth.get(j)).get(scoreHalfObstacleLength.get(j))==0)
                                board.get(scoreHalfObstacleWidth.get(j)).set(scoreHalfObstacleLength.get(j),Main.pitMoveScoreHalfObstacle.getObstacleId());
                        }
                    }
                    break;
            }
        }
    }
    /**
     * This is the GUI of board.
     * Done using grid pane with text fields
     * The board 2d arraylist is traversed
     *      if player is fund the green
     *      if nothing in that shell in light purple
     *      and 3 different colors for 3 different obstacles are set.
     * The board stage is set to automatically close after 15 seconds.
     * After every dice roll a new board stage is built and the old one get closed down in 15 seconds
     */
    public void boardGUI() throws IOException, InterruptedException {
        System.out.println("entering board gui");
        System.out.println("code 2 ");
        System.out.println("code 3 ");
        GridPane boardGrid = new GridPane();
        boardGrid.setPrefSize(1000,700);
        for(int i=0;i<boardWidth;i++){
            for(int j=0;j<boardLength;j++){
                TextField tile = new TextField();
                tile.paddingProperty();
                tile.alignmentProperty();
                //tile.setStroke(Color.BLACK);
                if(board.get(i).get(j)!=0 && board.get(i).get(j)!=11 && board.get(i).get(j)!=12 && board.get(i).get(j)!=13) {
                    tile.setText(playerIdToNameMap.get(board.get(i).get(j)));
                    tile.setStyle("-fx-control-inner-background: #508c75");
                }
                else {
                    if (board.get(i).get(j) == 11) {
                        tile.setStyle("-fx-control-inner-background: #6e4d34;");
                    } else if (board.get(i).get(j) == 12) {
                        tile.setStyle("-fx-control-inner-background: #332e2a;");
                    } else if (board.get(i).get(j) == 13) {
                        tile.setStyle("-fx-control-inner-background: #17100a;");
                    }else if(board.get(i).get(j) ==0){
                        tile.setStyle("-fx-control-inner-background: #9777c7;");

                    }
                    tile.setText("");
                }
               // tile.setStyle("-fx-control-inner-background: #81a1d4;");
                tile.setPadding(new Insets(30,30,30,30));
                tile.setEditable(false);
                boardGrid.add(tile,j,i);
            }
        }
        Stage boardStage = new Stage();
        Scene scene = new Scene(boardGrid);
        boardStage.setTitle("Simon's Race");
        boardStage.setScene(scene);
        boardStage.setX(510);
        boardStage.setY(0);
        boardStage.show();
       // boardStage.setFullScreen(true);

        PauseTransition delay = new PauseTransition(Duration.seconds(30)); // automatically closing the previous board after 15 seconds
        delay.setOnFinished( event -> boardStage.close());
        //delay.setOnFinished( event -> Dice.diceGUI());
        delay.play();
        //Thread.sleep(2000);
       // primaryStage.close();

        //return boardGrid;
    }
}
