package Main;

import Dice.Dice;
import Player.Player;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static Board.Board.board;
import static Dice.Dice.diceObj;
//import static Dice.Dice.diceStage;
import static Main.Main.*;
import static Player.Player.playerIdToScoreMap;

public class HelloController {
    private Stage stage;
    private Scene scene;
    private Stage addPlayerStage;
    @FXML
    private Label welcomeText;
    @FXML
    private Button addPlayer;
    @FXML
    private Button goToAddPlayerDetailsButton;
    @FXML
    TextField enterPlayerName;
    @FXML
    TextField enterPlayerAge;
    @FXML
    TextField enterBoardLength;
    @FXML
    TextField enterBoardWidth;
    @FXML
    TextField enterPlayersCount;
    @FXML
    TextField dice1;
    @FXML
    TextField dice2;
    @FXML
    Button clickToAddPlayers;
    @FXML
    Button rollDice1Button;
    @FXML
    Button rollDice2Button;
    public static int boardLength;
    public static int boardWidth;
    //public static int noOfPlayers;
    private int noOfPlayers;
    public int getNoOfPlayers(){
        return this.noOfPlayers;
    }
    public void setNoOfPlayers(int noOfPlayers){
        this.noOfPlayers = noOfPlayers;
    }
    public static int count ;
    public static HelloController helloControllerObj = new HelloController();
    public static int displayPlayerScreenCounter;

    /**
     * This function is called when the next button on the home screen is pressed
     * It reads board length, board width and number of players
     * The read values are assigned to a board object mutators
     * The next screen loads which has a button to go ahead and add players
     * @throws IOException
     */
    public void addPlayerScreen() throws IOException {
        System.out.println("code entering add player screen ");
        System.out.println("no of players in addPlayerScreen : "+Integer.parseInt(enterPlayersCount.getText()));
        helloControllerObj.setNoOfPlayers(Integer.parseInt(enterPlayersCount.getText()));
        //while(boardength <2)
        boardLength = Integer.parseInt(enterBoardLength.getText());
        boardWidth = Integer.parseInt(enterBoardWidth.getText());
        boardObj.setBoardLength(boardLength);
        boardObj.setBoardWidth(boardWidth);
        System.out.println("no of players in addPlayerScreen : "+helloControllerObj.getNoOfPlayers());
        count = helloControllerObj.getNoOfPlayers();
        //stage.close();
        Stage primaryStage = new Stage();
        URL url = new File("src\\main\\resources\\Main\\click-to-add-players-button.fxml").toURI().toURL(); // stack overflow reference
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("Next");
        primaryStage.setScene(new Scene(root,500,500));
        PauseTransition delay = new PauseTransition(Duration.seconds(15)); // automatically closing the previous board after 45 seconds
        delay.setOnFinished( event -> primaryStage.close());
        delay.play();
        primaryStage.show();
    }

    /**
     * This function redirects to the screen where players are supposed to be added.
     * This calls the addPlayers function
     * @throws IOException
     * @throws InterruptedException
     */
    @FXML
    public void displayAddPlayerScreen() throws IOException, InterruptedException {
        System.out.println("no of players = "+helloControllerObj.getNoOfPlayers());
        if (playersList.size()<helloControllerObj.getNoOfPlayers()) {
            System.out.println("code entering for player list size = "+playersList.size());
            addPlayerStage = new Stage();
            URL url = new File("src\\main\\resources\\Main\\player-info.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(url);
            addPlayerStage.setTitle("Next");
            addPlayerStage.setScene(new Scene(root, 500, 500));
            PauseTransition delay = new PauseTransition(Duration.seconds(120)); // automatically closing the previous board after 45 seconds
            delay.setOnFinished( event -> addPlayerStage.close());
            //delay.setOnFinished( event -> Dice.diceGUI());
            delay.play();
            addPlayerStage.show();
            //count--;
        }

        addPlayers();
        count--;
        //if(playersList.size()==noOfPlayers)
         //   addPlayerStage.close();



    }

    /**
     * While the count is < no of players , this function takes in the player details
     * It calls the player finder function that searches the database for a player with same name and age
     * If a player is found, then the corresponding playerId from the database is used
     * Else a new random number is generated and assigned to the player as hi/her player id
     * The playerIdToScore file is also searched for the player id
     * if player id is found, then the score from the database is fetched and mapped to the player using a playerIdToScoreMap hash map
     * Once all the players are fed, this function calls the board creation function
     * Then the player lanes population function is called.
     * Then the player list sort function is called
     * Then board and dice GUIs are called
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void addPlayers() throws IOException, InterruptedException {
        System.out.println("code entering add players function for count = "+count);
        System.out.println("no of players in add players function : "+helloControllerObj.getNoOfPlayers());
        //count--;
        //Stage primaryStage = new Stage();
        System.out.println(" coming for count = " +count);
        if(count>0) {
            System.out.println("code 1 ");
           try {
                System.out.println("code 4 ");
               // while(enterPlayerName.getText() == null) {
                   // enterPlayerName.setText("");
                    String playerName = enterPlayerName.getText();
                    System.out.println("player name : " + playerName);
                //}
            } catch (Exception e) {
                System.out.println("code 5 ");
                System.out.println(e.getMessage());
            }
            try {
                //while(enterPlayerAge.getText() == null) {
                    //enterPlayerAge.setText("");
                    System.out.println("player age : " + enterPlayerAge.getText());
                    int playerAge = Integer.parseInt(enterPlayerAge.getText());
               // }
            } catch (Exception e) {
                System.out.println("Please enter a valid age");
            }
            try {
                Random randomNumberGenerator = new Random();
                Player playerObj = new Player();
                int playerFinder = 0;
                int playerId = 0;
                /** Reading the PlayerDatabase file to search for already existing players **/
                if(enterPlayerName.getText() != null && enterPlayerAge.getText() != null){
                    System.out.println("player name : "+enterPlayerName.getText());
                    System.out.println("player age : "+enterPlayerAge.getText());
                    playerFinder = playerObj.playerFinder(enterPlayerName.getText() , Integer.parseInt(enterPlayerAge.getText()));
                    System.out.println("player finder : "+playerFinder);

                }
                /** condition for checking if the player already exists or not **/
                if(playerFinder == -1) {
                    playerId = randomNumberGenerator.nextInt(10000);
                    if(enterPlayerName.getText() != null && enterPlayerAge.getText() != null){
                        playerObj.playerWriter(playerId,enterPlayerName.getText(),Integer.parseInt(enterPlayerAge.getText()));
                    }
                }
                else {
                    playerId = playerFinder;
                    // need to capture player id to score map here
                    File f = new File("src\\main\\java\\Model\\PlayerDatabase.txt");
                    BufferedReader reader = new BufferedReader(new FileReader(f));
                    String line = "";
                    String[] arr;
                    // if the player id matches in the database then set the player score against the player id in the map
                    while ((line = reader.readLine()) != null) {
                        arr = line.split(",");
                        if(playerId == Integer.parseInt(arr[0])){
                            playerIdToScoreMap.put(playerId,Double.parseDouble(arr[3]));
                        }
                    }
                }
                // calling this function to read an existing score.
                playerObj.playerIdToScoreReader(playerId);
                playerIdToAgeMap.put(playerId, Integer.parseInt(enterPlayerAge.getText()));
                playerIdToNameMap.put(playerId, enterPlayerName.getText());
                playersList.add(playerId);
                enterPlayerName.setText("");
                enterPlayerAge.setText("");
                // primaryStage.close();
                System.out.println("size of players list : "+playersList.size());
                for(int i=0;i<playersList.size();i++)
                    System.out.println(playersList.get(i));
                addPlayerStage.close();
                //count--;
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
       }
        else {
            addPlayerStage.close();
            if(playersList.size()==helloControllerObj.getNoOfPlayers())
                addPlayerStage.close();
            addPlayerStage.close();
            System.out.println("your board is getting ready");
            boardObj.creatingBoard();
            System.out.println("Buckle up for the race");
            System.out.println("board object : "+boardObj);
            boardObj.populateObstacleOnBoard();
            System.out.println("populating player lanes");
            Player playerObj = new Player();
            playerObj.populatingPlayerLanes(playersList);
            diceObj = new Dice();
            diceObj.playersListSort();
            System.out.println("calling board GUI");
            boardObj.boardGUI();
            //diceObj.diceRoll();
            System.out.println("before calling dice gui ");
            diceObj.diceGUI();
            //diceObj.diceRoll();
            //stage.close(); // temporarily closing for testing
        }

    }
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to the race");
    }
    @FXML
    public void enterBoardLengthFunction(){
        System.out.println("enter board length function ");
        boardLength =  Integer.parseInt(enterBoardLength.getText());
    }
    @FXML
    public void enterBoardWidthFunction(){
        System.out.println("enter board width function ");

        boardWidth = Integer.parseInt(enterBoardWidth.getText());
    }
    @FXML
    public String enterPlayerNameField(){
        return enterPlayerName.getText();
    }
    @FXML
    public int enterPlayerAgeField(){
        String ageVal = enterPlayerAge.getText();
        return Integer.parseInt(ageVal);
    }
    @FXML
    public void enterPlayerCountFunction(){
        System.out.println("code entering enterPlayerCountFunction:  ");
        helloControllerObj.setNoOfPlayers(Integer.parseInt(enterPlayersCount.getText()));
        System.out.println("code entering enterPlayerCountFunction:  "+helloControllerObj.getNoOfPlayers());

    }

    /**
     * dice 1 is rolled for a random number between 0 and 5
     * if the random number is zero then the dice is assigned the value 1
     */
    @FXML
    public int rollingDice1() {
        try {
            System.out.println("entering rollinDice1");
            Random randomNumberGenerator = new Random();
            diceObj.setFirstDice(randomNumberGenerator.nextInt(5));
            if (diceObj.getFirstDice() == 0)
                diceObj.setFirstDice(1);
            dice1.setText("" + diceObj.getFirstDice());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return diceObj.getFirstDice();
    }
    /**
     * dice 1 is rolled for a random number between 0 and 5
     * if the random number is zero then the dice is assigned the value 1
     */
    @FXML
    public int rollingDice2() {
        try {
            System.out.println("entering rollinDice2");
            Random randomNumberGenerator = new Random();
            diceObj.setSecondDice(randomNumberGenerator.nextInt(5));
            if (diceObj.getSecondDice() == 0)
                diceObj.setSecondDice(1);
            dice2.setText("" + diceObj.getSecondDice());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return diceObj.getSecondDice();
    }
    public static int j=0;

    /**
     * This function calls the player movement function that returns a boolean
     * if winner is false then the current board status is displayed
     * else the leader board is displayed
     * Dice is rolled for every player and then again for all the players in the next round
     * @throws IOException
     * @throws InterruptedException
     */
    public void winnerCheckDiceRoll() throws IOException, InterruptedException {
        //j++;
        Player playerObject = new Player();
        boolean winner = false;
        boolean flag = false;
        int playerLane = Player.playerIdToLaneMap.get(playersList.get(j));
        int playerHeight = Player.playerIdToHeightMap.get(playersList.get(j));
        winner = playerObject.playerMovement(playersList.get(j), boardLength, boardWidth, playerHeight, playerLane, diceObj.getFirstDice(), diceObj.getSecondDice());
        if(winner == false)
        {
            boardObj.boardGUI();
        }
        else{
            ///diceStage.close();
            //GridPane boardGrid = new GridPane();
            //boardGrid.setPrefSize(1000,700);
            TextField winnerDisplay = new TextField();
            winnerDisplay.setPrefSize(500,500);
            //winnerDisplay.setPadding(new Insets(30,30,30,30));
            Stage winnerStage = new Stage();
            winnerStage.setTitle("Simon's Race Winner");
            System.out.println("winner name : "+playerIdToNameMap.get(playersList.get(j)));
            winnerDisplay.setText("YOUR WINNER ISSSS : "+playerIdToNameMap.get(playersList.get(j))+ " !!!!!");
            winnerDisplay.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
            winnerDisplay.setStyle("-fx-control-inner-background: #9777c7;");
            winnerDisplay.setAlignment(Pos.CENTER);
            Scene scene = new Scene(winnerDisplay);

            winnerStage.setScene(scene);
            winnerStage.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(30)); // automatically closing the previous board after 15 seconds
            delay.setOnFinished( event -> winnerStage.close());
            //delay.setOnFinished( event -> Dice.diceGUI());
            delay.play();
            System.out.println("WINNER FOUND");

            //Thread.sleep(15000);
            leaderBoard();
        }
        if(j==playersList.size()-1) {
            j = 0;
            flag=true;
            System.out.println("NEXT ROUND!!!!");
        }
        if(!flag) // incrementing on all the cases except for the last player case
            j++;
        //winner = playerObject.playerMovement(playersList.get(j), boardLength, boardWidth, playerHeight, playerLane, firstDice, secondDice);
    }

    /**
     * The leader board is displayed in a grid pane with text fields
     * The player id to score text file is read using buffered reader and the scores are mapped in a idToScore hash map
     * The stored values are put in a 2d array list as a combination of id and score -> [[id,score],[id,score]]
     * The array list is sorted and then the top 10 scores is read
     * For getting the player name , the player database file is read to find the player id and then the corresponding player name is fed in the grid pane
     * A stage is created and the grid pane is displayed as the leaderboard
     * @throws IOException
     */
    public void leaderBoard() throws IOException {
       try {
            GridPane leaderBoardGrid = new GridPane();
           leaderBoardGrid.setPrefSize(1000, 700);
           File f = new File("src\\main\\java\\Model\\PlayerIdToScore.txt");
           BufferedReader reader = new BufferedReader(new FileReader(f));
           String line = "";
           String[] arr;
           // get all the player Ids in an arraylist
           //  get their highest score values from the file
           HashMap<Integer,Double> idToScore = new HashMap<>();
           while((line = reader.readLine()) != null){
               if(line.length()>0) {
                   arr = line.split(",");
                  // System.out.println("id before : "+arr[0]+" score before : "+arr[1]);
                   idToScore.put(Integer.parseInt(arr[0]),Double.parseDouble(arr[1])); //mapping id to score
                   //System.out.println("id after : "+arr[0]+" score after : "+idToScore.get(Integer.parseInt(arr[0])));
               }
           }
           ArrayList<ArrayList <Integer>> idToScoreArray = new ArrayList<>();
           idToScore.forEach((k,v) -> {
               ArrayList<Integer> internal = new ArrayList<>();
               internal.add(k);
               internal.add((int) Math.round(v));
               idToScoreArray.add(internal);
           });
        Collections.sort(idToScoreArray, new Comparator<ArrayList<Integer>>() { // stack overflow reference
            @Override
            public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
                return o1.get(1).compareTo(o2.get(1));
            }

        });

           if(idToScore.size()>10) {
               int srNo = 1;
                for (int i = idToScoreArray.size()-1; i > idToScoreArray.size()-11; i--) {
                    //String str="";
                    for (int j = 0; j < 4; j++) {
                        TextField tile = new TextField();
                        tile.paddingProperty();
                        tile.alignmentProperty();
                        if(j==0){
                            tile.setText(""+srNo);
                            srNo++;
                        }
                       else if (j == 1) {
                            tile.setText("" + idToScoreArray.get(i).get(0));
                            //str+= "tile id : "+tile.getText()+" ";
                        }
                        else if (j == 2) {
                            FileReader fileReader = new FileReader("src\\main\\java\\Model\\PlayerDatabase.txt");
                            BufferedReader reader1 = new BufferedReader(fileReader);
                            String line1 = "";
                            String[] arr1;
                            while((line1 = reader1.readLine()) != null){
                                if(line1.length()>0) {
                                    arr1 = line1.split(",");
                                    if(idToScoreArray.get(i).get(0) == Integer.parseInt(arr1[0])){
                                        playerIdToNameMap.put(idToScoreArray.get(i).get(0),arr1[1]);
                                    }
                                }
                            }
                            tile.setText(playerIdToNameMap.get(idToScoreArray.get(i).get(0)));
                            //str+= "tile name : "+tile.getText()+" ";
                        }
                        else if (j == 3) {
                            tile.setText("" + idToScoreArray.get(i).get(1));
                            //str+= "tile score : "+tile.getText()+" ";
                        }
                        tile.setStyle("-fx-text-fill: green;");
                        tile.setPadding(new Insets(30, 30, 30, 30));
                        tile.setEditable(false);
                        //System.out.println("printing tile "+tile);
                        //System.out.println(str);
                        leaderBoardGrid.add(tile, j, i);
                    }
                }
            }
           else{
               System.out.println("sorry -> no leader board yet");
           }
            Stage leaderBoardStage = new Stage();
            Scene scene = new Scene(leaderBoardGrid);
            leaderBoardStage.setTitle("Simon's Race leader board");
            leaderBoardStage.setScene(scene);
            leaderBoardStage.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(30)); // automatically closing the previous board after 45 seconds
            delay.setOnFinished(event -> leaderBoardStage.close());
            //delay.setOnFinished( event -> Dice.diceGUI());
            delay.play();
        }catch(Exception e){
            System.out.println("Exception in leaderboard : "+e.getMessage());
        }
    }

}