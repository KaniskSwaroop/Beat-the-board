package Dice;

import Player.Player;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static Main.HelloController.boardLength;
import static Main.HelloController.boardWidth;
import static Main.Main.*;

public class Dice {
    private Stage stage ;
    private Button rollDice;
    private int firstDice;
    private int secondDice;
    public void setFirstDice(int firstDice) {
        this.firstDice = firstDice;
    }
    public int getFirstDice(){
        return this.firstDice;
    }
    public void setSecondDice(int secondDice){
        this.secondDice = secondDice;
    }
    public int getSecondDice(){
        return this.secondDice;
    }
    //public static Stage diceStage =new Stage();
    public static Dice diceObj = new Dice();
    @FXML
    Button rollDice1Button;
    @FXML
    Button rollDice2Button;
    @FXML
    Button nextPlayer;

    /**
     * This function sorts the player list
     * The sorting is done (playerAgeValueJ + playerNameLengthJ) / 2
     * The player with higher (playerAgeValueJ + playerNameLengthJ) / 2 value will be ahead in the list and will be rolling the dice first
     * The final for loop assigns the initial height of the players to be the start line
     */
    public void playersListSort() {
        for (int i = 0; i < playersList.size() - 1; i++) {
            int playerNameLengthI = playerIdToNameMap.get(playersList.get(i)).length();
            int playerAgeValueI = playerIdToAgeMap.get(playersList.get(i));
            for (int j = i + 1; j < playersList.size(); j++) {
                int playerNameLengthJ = playerIdToNameMap.get(playersList.get(j)).length();
                int playerAgeValueJ = playerIdToAgeMap.get(playersList.get(j));
                if ((playerAgeValueJ + playerNameLengthJ) / 2 > (playerAgeValueI + playerNameLengthI) / 2) {
                    Collections.swap(playersList, i, j);
                }
            }
        }
        for (int i = 0; i < playersList.size(); i++) {
            Player.playerIdToHeightMap.put(playersList.get(i), boardObj.getBoardWidth() - 1);
        }
    }

    /**
     *
     * @throws IOException
     * @throws InterruptedException
     * This is the dice gui function that loads the dice-view.fxml file
     */
    public void diceGUI() throws IOException, InterruptedException {
        // try {
        Stage diceStage = new Stage();
        System.out.println("code 2 in dice GUI ");
        URL url = new File("src\\main\\resources\\Main\\dice-view.fxml").toURI().toURL(); // stack overflow reference
        System.out.println("URL :"+url);
        Parent root = FXMLLoader.load(url);
        System.out.println("code 3 in dice GUI");
        //System.out.println("dice 1 : " + dice1);
        //System.out.println("dice 2 : " + dice2);
        diceStage.setTitle("Dice");
        diceStage.setScene(new Scene(root, 500, 800)); //(width, height)
        diceStage.setX(10);
        diceStage.setY(0);
        diceStage.show();
        Thread.sleep(4000);
        //dice1.setText("");
        //dice2.setText("");
        System.out.println("code dice GUI");
       // PauseTransition delay = new PauseTransition(Duration.seconds(60)); // automatically closing the previous board after 15 seconds
        //delay.setOnFinished( event -> diceStage.close());
        //delay.setOnFinished( event -> Dice.diceGUI());
        //delay.play();
        //}catch (Exception e){
        //  System.out.println("Exception in dice GUI : "+e.getMessage());
        //}
    }

    /*public void diceRoll() throws IOException, InterruptedException {
        Player playerObject = new Player();
        boolean winner = false;
        int j=0;
        /******************** while (winner == false) {
            System.out.println("code entering winner while loop");

            for (int i = 0; i < playersList.size(); i++) {
                System.out.println("Rolling dice for : "+playersList.get(i));
                System.out.println("firstDice : " + firstDice);
                System.out.println("secondDice : " + secondDice);
                diceGUI();
                int playerLane = Player.playerIdToLaneMap.get(playersList.get(i));
                System.out.println("playerLane : " + playerLane);
                int playerHeight = Player.playerIdToHeightMap.get(playersList.get(i));
                winner = playerObject.playerMovement(playersList.get(i), boardLength, boardWidth, playerHeight, playerLane, firstDice, secondDice);
                if (winner)
                    break;
            }
            System.out.println("!!!!! NEXT ROUND !!!!!!!");
        }*********************/
        // check for winner after a move -> if winner == false -> press next again and make a move
        /*while (winner == false) {
            j++;
            diceGUI();
            int playerLane = Player.playerIdToLaneMap.get(playersList.get(j));
            int playerHeight = Player.playerIdToHeightMap.get(playersList.get(j));
            winner = playerObject.playerMovement(playersList.get(j), boardLength, boardWidth, playerHeight, playerLane, firstDice, secondDice);
            if (winner)
                break;
            if(j==playersList.size()-1) {
                j = 0;
                System.out.println("!!!!! NEXT ROUND !!!!!!!");
            }
        }
        boardObj.boardGUI();
    }
    public static int j=0;
    public void winnerCheckDiceRoll() throws IOException, InterruptedException {
        //j++;
        Player playerObject = new Player();
        boolean winner = false;
        boolean flag = false;
        int playerLane = Player.playerIdToLaneMap.get(playersList.get(j));
        int playerHeight = Player.playerIdToHeightMap.get(playersList.get(j));
        winner = playerObject.playerMovement(playersList.get(j), boardLength, boardWidth, playerHeight, playerLane, firstDice, secondDice);
        if(winner == false)
        {
            boardObj.boardGUI();
        }
        else{
            diceStage.close();
            System.out.println("WINNER FOUND");
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
    @FXML
    public void rollingDice1() {
        System.out.println("entering rollinDice1 function");
        Random randomNumberGenerator = new Random();
        firstDice = randomNumberGenerator.nextInt(4);
        if (firstDice == 0)
            firstDice = 1;
    }
    @FXML
    public void rollingDice2() {
        System.out.println("entering rollinDice2 function");
        Random randomNumberGenerator = new Random();
        secondDice = randomNumberGenerator.nextInt(4);
        if(secondDice == 0)
            secondDice =1;
    }*/

}
