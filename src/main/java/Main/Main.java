package Main;

import Board.Board;
import Obstacles.PitMoveScoreHalfObstacle;
import Obstacles.PitObstacle;
import Obstacles.SwapObstacle;
import Player.Player;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

public class Main extends Application {
    /**
     * This is the main stage that loads when the code is run
     * It loads the home screen asking for board length,width and number of players
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 800);
        Image icon = new Image("F:\\UCD\\JavaFXtrial\\src\\gameIcon.jpg");
        stage.getIcons().add(icon); //adding icon
        stage.setWidth(560);
        stage.setHeight(560);
        stage.setResizable(false); // height and width can't be resized
        stage.setFullScreen(true);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        PauseTransition delay = new PauseTransition(Duration.seconds(45)); // automatically closing the previous board after 45 seconds
        delay.setOnFinished( event -> stage.close());
        delay.play();
        stage.show();
    }
    public static ArrayList<Integer> playersList = new ArrayList<>(); //Array list for storing player Ids // // / declaring static in order to use it in other classes4
    public static Board boardObj = new Board();
    public static PitObstacle pitObstacle = new PitObstacle();
    public static SwapObstacle swapObstacle = new SwapObstacle();
    public static PitMoveScoreHalfObstacle pitMoveScoreHalfObstacle = new PitMoveScoreHalfObstacle();
    public static HashMap<Integer,String> playerIdToNameMap = new HashMap<>();
    public static HashMap<Integer,Integer> playerIdToAgeMap = new HashMap<>();
    public static int numberOfPlayers = 0;
    public static void main(String[] args) throws IOException, InterruptedException {
        launch();
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter board length : ");
        int boardLength = userInput.nextInt();
        boardObj.setBoardLength(boardLength);
        System.out.println("Enter board width : ");
        int boardWidth = userInput.nextInt();
        boardObj.setBoardWidth(boardWidth);
        System.out.println(boardObj.getBoardWidth());
        System.out.println("Enter number of players : ");
        numberOfPlayers = userInput.nextInt();
        HashMap<Integer,Integer> playerIdToRankMap = new HashMap<>();
        Random randomNumberGenerator = new Random(); //generating Random object for random playerId

        pitObstacle.setObstacleName("Pit!!!!");
        swapObstacle.setObstacleName("Swap with a competitor?");
        pitMoveScoreHalfObstacle.setObstacleName("Want to get your score halved?");
        pitObstacle.setObstacleId(11);
        swapObstacle.setObstacleId(12);
        pitMoveScoreHalfObstacle.setObstacleId(13);
        String playerName = "";
        int playerAge = 0;
        int playerId = 0;
        /** creating player object for player functionalities **/
        Player playerObject = new Player();
        /** assigning IDs to players **/
        for(int i=0;i<numberOfPlayers;i++){
            playerId = randomNumberGenerator.nextInt(10000);
            System.out.println("Enter player name : ");
            playerName = userInput.next();
            System.out.println("Enter player age : ");
            playerAge = userInput.nextInt();
            playerIdToAgeMap.put(playerId,playerAge);
            playerIdToNameMap.put(playerId,playerName);
            playersList.add(playerId);
        }
        /** calling function that populates the board **/
        boardObj.creatingBoard();
        /** Function call for populating board with obstacles **/
        boardObj.populateObstacleOnBoard();
        System.out.println("code out of populating obstacles");

        /** calling function for playerId to lane map and populating board with player start positions **/
        playerObject.populatingPlayerLanes(playersList);
        for(int i=0;i<boardWidth;i++){
            for(int j=0;j<boardLength;j++){
                System.out.print(Board.board.get(i).get(j)+" ");
            }
            System.out.println("");
        }
        System.out.println("code out of player population function ");
        /** ranking players on the basis of name length and age. Higher is getting the priority for rolling the dice **/
        for(int i=0;i<playersList.size()-1;i++){
            //System.out.println("playerIdToNameMap : "+playerIdToNameMap.size());
            //System.out.println("playerIdToAgeMap : "+playerIdToAgeMap.size());
            int playerNameLengthI = playerIdToNameMap.get(playersList.get(i)).length();
            //System.out.println("playerNameLengthI : "+playerNameLengthI);
            int playerAgeValueI = playerIdToAgeMap.get(playersList.get(i));
            //System.out.println("playerAgeValueI : "+playerAgeValueI);
            for(int j=i+1;j<playersList.size();j++){
                int playerNameLengthJ = playerIdToNameMap.get(playersList.get(j)).length();
                int playerAgeValueJ = playerIdToAgeMap.get(playersList.get(j));
                if((playerAgeValueJ + playerNameLengthJ)/2 > (playerAgeValueI + playerNameLengthI)/2){
                    Collections.swap(playersList,i,j);
                }
            }
        }
        System.out.println("code after sorting players List");

        System.out.println("code after dice switch");
        Boolean winner = false;

        /** Initializing board as a 2D integer arrayList with all elements 0 **/
        /** name of 2D array -> boardTraversal **/

        /** **/
        /** game code **/
        //setting initial player height to boardWidth -1
        for(int i=0;i<playersList.size();i++){
            Player.playerIdToHeightMap.put(playersList.get(i), boardObj.getBoardWidth()-1);
        }
        while(winner == false){
            System.out.println("code entering winner while loop");
            for(int i=0;i<playersList.size();i++){
                /* 2 dice rolls for each player */
                //ERROR -> add check for getting 0 case;
                System.out.println("entering loop for : "+playerName);
                int firstDice = randomNumberGenerator.nextInt(4);
                int secondDice = randomNumberGenerator.nextInt(4);
                while(firstDice ==0){
                    //System.out.println("entering first dice:");
                    firstDice = randomNumberGenerator.nextInt(4);
                }
                while(secondDice ==0){
                    //System.out.println("entering second dice");
                    secondDice = randomNumberGenerator.nextInt(4);
                }
                System.out.println("firstDice : "+firstDice);
                System.out.println("secondDice : "+secondDice);
                /************ Tasks *************/
                // perform a movement on the basis of dice roll
                // if an obstacle is caught -> do the necessary transitions.

                // use the player to lane map -> because only straight line movement is needed
                // only position on board width will vary
                int playerLane = Player.playerIdToLaneMap.get(playersList.get(i));
                System.out.println("playerLane : "+playerLane);
                int playerHeight = Player.playerIdToHeightMap.get(playersList.get(i));
                winner = playerObject.playerMovement(playersList.get(i), boardLength, boardWidth, playerHeight, playerLane, firstDice,secondDice/** need ot pass dice number to direction map **/);
                if(winner)
                    break;
            }
            //winner = true;
        }
        /** final board after race **/
        for(int i=0;i<boardWidth;i++){
            for(int j=0;j<boardLength;j++){
                System.out.print(Board.board.get(i).get(j)+" ");
            }
            System.out.println("");
        }
    }
}