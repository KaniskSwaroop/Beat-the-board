package Player;

import Board.Board;
import Main.Main;
import Obstacles.PitMoveScoreHalfObstacle;
import Obstacles.PitObstacle;
import Obstacles.SwapObstacle;

import java.io.*;
import java.net.URL;
import java.util.*;

import static Main.Main.boardObj;

//import static Board.Board.boardGrid;

//import static Board.Board.*;

/*
* The player class represents the identity of a player
* It contains the rank and the score of the player
*/
// Kanisk -> can use parameterized constructors for getting data from main
public class Player {
    private int playerId;
    private String playerName;
    private double score; // Kanisk > required ? giving in Scoreboard class
    private int playerRank; // Kanisk -> required ? giving in Scoreboard class
    private int lane;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getPlayerRank() {
        return playerRank;
    }

    public void setPlayerRank(int playerRank) {
        this.playerRank = playerRank;
    }

    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }
    public static HashMap<Integer,Double> playerIdToScoreMap = new HashMap<>(); // declaring as a class variable so that I can access it in main
    public static HashMap<Integer,Integer> playerIdToMoveSkipCounter = new HashMap<>();// declaring as a class variable so that I can access it in main
    public static int playerSidewaysMovement = -1;
    public static HashMap<Integer,Integer> playerIdToLaneMap = new HashMap<>();
    public static HashMap<Integer,Integer> playerIdToHeightMap = new HashMap<>();
    public static File file = new File("src\\main\\java\\Model\\PlayerDatabase.txt");
    public static FileWriter fileWriter;

    static {
        try {
            fileWriter = new FileWriter(file,true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public BufferedWriter writer = new BufferedWriter(fileWriter);


    /**
     * function to check if a player already exists in the PlayerDatabase file.
     * The search is made on the basis of player name and player age
     * return -1 if player is not found in the database
     * else return the player id of the searched player
     * @param playerName
     * @param playerAge
     * @return
     * @throws IOException
     */
    public int playerFinder(String playerName, int playerAge) throws IOException {
        int resultId = -1;
        try {
            //URL url = getClass().getResource("PlayerDatabase.txt");
            File f = new File("src\\main\\java\\Model\\PlayerDatabase.txt");
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String line = "";
            String[] arr;
            while ((line = reader.readLine()) != null) {
                arr = line.split(",");
                int playerId = Integer.parseInt(arr[0]);
                String name = arr[1];
                String age = arr[2];
                if (name.equals(playerName) && Integer.parseInt(age) == playerAge) {
                    // player found in the database;
                    resultId = playerId;
                }
            }
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return resultId;
    }

    /**
     * This function is called whenever a new player is fed
     * The new player data is written in the player database file using buffered writer
     * @param playerId
     * @param playerName
     * @param playerAge
     */
    public void playerWriter(int playerId, String playerName, int playerAge){
        try{
            Double score = 0.0;
            String playerData = "";
            if(playerIdToScoreMap.get(playerId) == null)
                playerData = ""+playerId+","+playerName+","+playerAge+","+0.0;
            else
                playerData = ""+playerId+","+playerName+","+playerAge+","+playerIdToScoreMap.get(playerId);
            System.out.println("writing data on file : "+playerData);
            writer.write(playerData);
            writer.newLine();
            System.out.println("data write successful");

            writer.flush();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * This function reads the playerIdToScore text file that contains ids of players mapped to score
     * This searches the file for a played id and if the id is found then the score is mapped to the player for the current game.
     * @param playerId
     * @throws IOException
     */
    public void playerIdToScoreReader(int playerId) throws IOException {
        File f = new File("src\\main\\java\\Model\\PlayerIdToScore.txt");
        BufferedReader reader = new BufferedReader(new FileReader(f));
        String line = "";
        String[] arr;
        playerIdToScoreMap.put(playerId,0.0);
        while ((line = reader.readLine()) != null) {
            arr = line.split(",");
            if(playerId ==Integer.parseInt(arr[0]))
            {
                playerIdToScoreMap.put(playerId,Double.parseDouble(arr[1]));
            }
        }
    }

    /**
     * Function called when a player wins to map the player's score to his/her ID
     * The data is written in the playerIdToScore text file.
     * @param playerId
     * @throws IOException
     */
    public void playerIdToScoreWriter(int playerId) throws IOException {
        File file1 = new File("src\\main\\java\\Model\\PlayerIdToScore.txt");
        FileWriter fileWriter1 = new FileWriter(file1,true);
        BufferedWriter writer1 = new BufferedWriter(fileWriter1);
        String idToScore = ""+playerId +","+playerIdToScoreMap.get(playerId);
        writer1.write(idToScore);
        writer1.newLine();
        writer1.close();

    }

    /**
     * whenever there's a win , then this function is called
     * it records all the player Ids from the board and call the score writer function
     * The score write function writes he score mapped to the id in the PlayerIdToScore.txt file
     * @throws IOException
     */
    public void playerScoreTrackerOnWin() throws IOException {
        for(int i=0;i< boardObj.getBoardWidth();i++){
            for(int j=0;j<boardObj.getBoardLength();j++){
                if(Board.board.get(i).get(j)!=0 && Board.board.get(i).get(j)!=11 && Board.board.get(i).get(j)!=12 && Board.board.get(i).get(j)!=13){
                    playerIdToScoreWriter(Board.board.get(i).get(j));
                }
            }
        }
    }

    /**
     * The population of player lanes is also done at random
     * for every player, a random number is generated from [0 to the board length]
     * if that shell of the board is empty then that lane is allocated to the player
     * else, until an empty shell os not found and while loop keeps running
     * On finding an empty shell, that shell is allocated to the player as the starting position for the race.
     * @param playersList
     */
    public void populatingPlayerLanes(ArrayList<Integer> playersList){
        Random randomNumberGenerator = new Random();
        for(int i=0;i<playersList.size();i++){ // corner case check if unique lane is assigned to each player
            int randomLane = randomNumberGenerator.nextInt(boardObj.getBoardLength());
            if(Board.board.get(boardObj.getBoardWidth() - 1).get(randomLane) == 0) {
                playerIdToLaneMap.put(playersList.get(i), randomLane);
                Board.board.get(boardObj.getBoardWidth()-1).set(randomLane,playersList.get(i));
            }
            else{
                while(Board.board.get(boardObj.getBoardWidth() - 1).get(randomLane) != 0){ /** Uniques lane to each player **/
                    randomLane = randomNumberGenerator.nextInt(boardObj.getBoardLength());
                }
                playerIdToLaneMap.put(playersList.get(i),randomLane);
                Board.board.get(boardObj.getBoardWidth()-1).set(randomLane,playersList.get(i));
            }
        }
        ArrayList<Integer> playerLanes = new ArrayList<>();
        Collection getLaneValues = playerIdToLaneMap.values();
        Iterator lanes = getLaneValues.iterator();
        while(lanes.hasNext()){
            playerLanes.add((Integer)lanes.next());
        }
        System.out.println("playerLanes.size() : " +playerLanes.size());
       /* for(int i=0;i<playersList.size();i++){
            Board.board.get(boardObj.getBoardWidth()-1).set(playerIdToLaneMap.get(playersList.get(i)),playersList.get(i));
        }*/
        for(int i=0;i<playerLanes.size();i++)
            System.out.print(playerLanes.get(i)+" ");
        System.out.println("");
    }
    /** function for sideways movement
     * A variable class variable playerSidewaysMovement is taken and its value is switched between -1 and 1
     * if the value is -1, then left movement is possible, else right movement
     * Alternate left and right movements are possible.
     *
     * If a right movement is possible considering the board boundaries or any other obstacle -> the player is allowed to move sideways
     * If left is occupied then right movement is considered and vice versa
     * if both left and right are blocked then it is checked if the player has a skip available with him/her
     * If skip is available (3 continuous skips are allowed . Values are mapped using hashmap) then the player is allowed to skip the chance and not lose points
     * else if the skips are also exhausted , then player has to lose 5 points and the updated score is mapped to the player id
     * The function returns a boolean stating if a sideways movement was made or not
     * **/
    public boolean sidewaysMovementChecker (int playerId, int playerHeight, int playerLane, int boardLength, int dice1, int dice2) throws IOException, InterruptedException {
            boolean sidewaysMovement = false;
            if((playerLane + 1 < boardLength) || (playerLane - 1 >= 0)){
                if(playerSidewaysMovement == -1){ // move left
                    if(playerLane -1 >=0){
                        Board.board.get(playerHeight).set(playerLane,0);
                        Board.board.get(playerHeight).set(playerLane-1,playerId); // successful left movement
                        boardObj.boardGUI();
                        playerIdToMoveSkipCounter.put(playerId,0);
                        double currentScore = 0;
                        if(playerIdToScoreMap.containsKey(playerId)){
                            currentScore = playerIdToScoreMap.get(playerId);
                        }
                        currentScore += 2.0;
                        playerIdToScoreMap.put(playerId,currentScore);
                        playerSidewaysMovement = 1; // ready for right movement next time
                        sidewaysMovement = true;
                        System.out.println("player moving left when dice 1 is: "+dice1+" and dice2 is : "+dice2);
                    }
                    else{
                        // check right move
                        if(playerLane +1 < boardLength){
                            if(Board.board.get(playerHeight).get(playerLane+1) == 0){ // ERROR -> check this if
                                Board.board.get(playerHeight).set(playerLane,0);
                                Board.board.get(playerHeight).set(playerLane + 1,playerId); // successful right movement
                                boardObj.boardGUI();
                                playerIdToMoveSkipCounter.put(playerId,0);
                                double currentScore = 0;
                                if(playerIdToScoreMap.containsKey(playerId)){
                                    currentScore = playerIdToScoreMap.get(playerId);
                                }
                                currentScore += 2.0;
                                playerIdToScoreMap.put(playerId,currentScore);
                                sidewaysMovement = true;
                                System.out.println("successful player right movement when initial left movement was intended");
                                // successful player right move
                            }
                        }
                        else{
                            // skip
                            if(playerIdToMoveSkipCounter.get(playerId) < 3){
                                int currSkipCount = playerIdToMoveSkipCounter.get(playerId);
                                currSkipCount++;
                                playerIdToMoveSkipCounter.put(playerId,currSkipCount);
                            }
                            else{
                                // lose points
                                double currentScore = 0;
                                if(playerIdToScoreMap.containsKey(playerId)){
                                    currentScore = playerIdToScoreMap.get(playerId);
                                }
                                currentScore -= 5.0; // directly reducing the score by 5
                                playerIdToScoreMap.put(playerId,currentScore);
                                boardObj.boardGUI();
                                // board need not be updated as the player remains on the current shell
                                playerIdToMoveSkipCounter.put(playerId,0); // successful move -> so skip counter is put back to 0;
                            }
                        }
                    }
                }
                else{ // moving right
                    if(playerLane + 1 < boardLength){
                        Board.board.get(playerHeight).set(playerLane,0); // current lane to 0
                        Board.board.get(playerHeight).set(playerLane+1,playerId); // successful right movement // ERROR -> check playerLane + 1 < board length
                        boardObj.boardGUI();
                        playerIdToMoveSkipCounter.put(playerId,0);
                        double currentScore = 0;
                        if(playerIdToScoreMap.containsKey(playerId)){
                            currentScore = playerIdToScoreMap.get(playerId);
                        }
                        currentScore += 2.0;
                        playerIdToScoreMap.put(playerId,currentScore);
                        playerSidewaysMovement = -1; // ready for left movement next time
                        System.out.println("player moving right when dice1 is : "+dice1+" and dice 2 is: "+dice2);
                        sidewaysMovement = true;
                    }
                    else{
                        // check left move
                        if(playerLane -1 >= 0){
                            if(Board.board.get(playerHeight).get(playerLane-1) == 0){  // ERROR -> check this if
                                Board.board.get(playerHeight).set(playerLane,0); // set current shell to 0
                                Board.board.get(playerHeight).set(playerLane - 1,playerId); // left to player id // ERROR -> check playerLane  - 1 >=0
                                boardObj.boardGUI();
                                playerIdToMoveSkipCounter.put(playerId,0);
                                double currentScore = 0;
                                if(playerIdToScoreMap.containsKey(playerId)){
                                    currentScore = playerIdToScoreMap.get(playerId);
                                }
                                currentScore += 2.0;
                                playerIdToScoreMap.put(playerId,currentScore);
                                System.out.println("successful left movement when initial right was intended");
                                // successful player right move
                                sidewaysMovement = true;
                            }
                        }
                        else{
                            // skip
                            if(playerIdToMoveSkipCounter.get(playerId) < 3){
                                int currSkipCount = playerIdToMoveSkipCounter.get(playerId);
                                currSkipCount++;
                                playerIdToMoveSkipCounter.put(playerId,currSkipCount);
                            }
                            else{
                                // lose points
                                double currentScore = 0;
                                if(playerIdToScoreMap.containsKey(playerId)){
                                    currentScore = playerIdToScoreMap.get(playerId);
                                }
                                currentScore -= 5.0; // directly reducing player score by 5
                                playerIdToScoreMap.put(playerId,currentScore);
                                // board need not be updated as the player remains on the current shell
                                boardObj.boardGUI();
                                playerIdToMoveSkipCounter.put(playerId,0); // successful move -> so skip counter is put back to 0;
                            }
                        }
                    }

                }
            }
            else{ // backward move is not possible -> player has to move ahead and lose 5 points
                return false;
            }
            return sidewaysMovement;
    }

    /**
     *
     * @param playerId
     * @param boardWidth
     * @param boardLength
     * @param playerHeight
     * @param playerLane
     * @param dice1
     * @param dice2
     * @return
     * @throws IOException
     * @throws InterruptedException
     * This is the main function for making a player movement
     * cases are considered on the basis of dice value
     * 1,2,3,4 on dice 1 indicates number of moves
     * 1,2,3,4 on dice 2 indicates forward, forward, backward and skip respectively
     * So, dice1 -> 1 , dice2 -> 2 indicates 1 move forward
     * a nested switch is taken with outer switch being for dice 1 and inner for dice 2
     * The board boundaries and other obstacles are checked before making a move
     * If an obstacle is found then the player is allowed to use a skip from the vault
     * Three continuous skips are allowed and the data is mapped using a playerIdToMoveSkipCounter hashmap
     * Whenever a successful move is made -> skip counter is set to  for that player id
     * If the player has exhausted the continuous number of skips then
     *  if obstacle 11 (pit) is found then pitObstacleMovement function is called for making the move
     *  if obstacle 12 (swap) is found then swapObstacleMovement function is called for making the move
     *  if obstacle 13 (score half) is found then scoreHalfObstacleMovement function is called for making the move
     *  if a player is found then -> FOR FUTURE SCOPE -> a trade off will be offered to the player who is higher in the race
     *      the trade off will be of the score of the player behind with the position of the player ahead.
     *      The score to be traded will be lifetime achieved score.
     *      The players will have a choice , if they wish to choose the trade.
     *      This functionality is current not implemented and has been kept as a future scope development of the game
     * Every successful move allocates 5 points to the player
     * The player is allowed to avoid a backward move with three continuous skips available.
     * If the skips are exhausted then the played is forced to make a move and lose points according to the obstacle.
     * When a move makes the player move to the end line and above -> The winner if found -> Winner is displayed -> 20 points are added to the player id
     * The leaderboard is displayed
     *  The playerScoreTrackerOnWin function is called which is used for writing the updated scores of all the players in the playerIdToScore.txt file
     *  The final board is also displayed
     * ADDITIONAL ->
     *  The board gui is called for every move.
     * The function return a boolean for win . This function is called until a winner is found
     */
    public boolean playerMovement(int playerId, int boardWidth, int boardLength, int playerHeight, int playerLane, int dice1, int dice2) throws IOException, InterruptedException {
        System.out.println("player's current height in playerMovement : "+playerHeight);
        System.out.println("player's current lane in playerMovement : "+playerLane);
        System.out.println("board width in playerMovement : "+boardWidth);
        System.out.println("board length in playerMovement : "+boardLength);
        System.out.println("player id of current player : "+playerId);
        boolean winner = false;
        playerIdToMoveSkipCounter.put(playerId,0);
        //HashMap<Integer,Integer> playerIdToMoveSkipCounter = new HashMap<>();
        //HashMap<Integer,Double> playerIdToScoreMap = new HashMap<>();
        /** Task **/
        // Manipulate current board with player movement
        // get dice1 and dice2 values and depending on the obstacles on the board, decide the motion of the players
        // populate a player shell with player ID -> easy identification of player position and mapping of other player information
        PitObstacle pitObstacleObj = new PitObstacle();
        SwapObstacle swapObstacleObj = new SwapObstacle();
        PitMoveScoreHalfObstacle scoreHalfObstacleObj = new PitMoveScoreHalfObstacle();
        switch(dice1){ /** recording number of moves **/
            case 1:
                switch(dice2){ /** recording direction of moves **/
                    case 1:
                    case 2:
                        /** need to get the mapping of player id with the current position of the player **/
                        /** if the possible forward motion has no obstacles then player always moves forward **/
                        if(playerHeight - 1 > 0){ // forward move without winning -> ERROR -> check this if condition
                            if(Board.board.get(playerHeight-1).get(playerLane)  == 0){ //checking if the above shell is 0
                                Board.board.get(playerHeight).set(playerLane,0); // current shell is set back to 0;
                                Board.board.get(playerHeight -1).set(playerLane,playerId); // setting the above shell value to be the player ID indicating that the player has moved ahead
                                playerIdToHeightMap.put(playerId,playerHeight-1); // incrementing player height
                                double currentScore = 0;
                                if(playerIdToScoreMap.containsKey(playerId)){
                                    currentScore = playerIdToScoreMap.get(playerId);
                                }
                                currentScore += 5.0;
                                playerIdToScoreMap.put(playerId,currentScore); // mapped updated player score to player ID
                                playerIdToMoveSkipCounter.put(playerId,0); // a successful forward move -> skip counter is back to 0
                                boardObj.boardGUI();
                               // return ;
                            }
                            else if(Board.board.get(playerHeight-1).get(playerLane)  == 11){ // pit
                                // player can move forward and lose 5 points and get redirected to previous shell
                                // player can skip chance
                                /** calling function for handling pit obstacle **/
                                pitObstacleObj.pitObstacleMovement(playerId,playerHeight,playerIdToLaneMap.get(playerId), boardLength, dice1, dice2);
                            }
                            else if(Board.board.get(playerHeight-1).get(playerLane)  == 12){ // swap
                                // no other option move -> no need to but since player cannot skip more than two straight chances -> so, either move backward or swap
                                /** Task **/
                                swapObstacleObj.swapObstacleMovement(playerId,playerHeight,playerIdToLaneMap.get(playerId), boardLength, boardWidth, dice1, dice2);
                            }
                            else if(Board.board.get(playerHeight-1).get(playerLane)  == 13){ // point half // ERROR -> check corner case for playerHeight + 1 <= width
                                // if a win is possible soon then this can be an option .
                                // since this is just 1 move forward -> won't consider doing this if it can be avoided
                                System.out.println("code entering point half obstacle");
                                scoreHalfObstacleObj.scoreHalfObstacleMovement(playerId, playerHeight, playerIdToLaneMap.get(playerId), boardLength, boardWidth, dice1, dice2);

                            }
                            else{
                                System.out.println("You have a competitor there who refuses to move "); // ERROR -> put some penalty here
                                System.out.println("Willing to make an offer to the player for trading ?"); // ERROR -> work on this feature
                                // if player on the back has better score than the one above , then propose a swap in their positions
                                boardObj.boardGUI();
                            }
                        }
                        //else if(playerHeight - 1 ==0){

                        //}
                        else {
                            //winner = true;
                            System.out.println("Code entering winning case for 1 forward move");
                            Board.board.get(0).set(playerLane,playerId);
                            double currentScore = playerIdToScoreMap.get(playerId);
                            currentScore += 20.0;
                            playerIdToScoreMap.put(playerId,currentScore);
                            System.out.println(" Player won!!!!! " + Main.playerIdToNameMap.get(playerId));
                            playerScoreTrackerOnWin(); // calling for getting scores
                            boardObj.boardGUI();
                            playerIdToHeightMap.put(playerId,0);// setting player height to tbe the finish line
                            return true;
                        }
                        break;
                    case 3: //backward
                        if(playerIdToMoveSkipCounter.get(playerId) < 3){ // if skip is possible
                            // skip
                            int currentSkipCount = playerIdToMoveSkipCounter.get(playerId);
                            currentSkipCount ++;
                            playerIdToMoveSkipCounter.put(playerId,currentSkipCount);
                            boardObj.boardGUI();
                            System.out.println("Successfully skipped a forced backward motion : "+playerId);
                        }
                        else {
                            if(playerHeight + 1 < boardWidth - 1){ // corner case
                                if(Board.board.get(playerHeight + 1).get(playerLane) == 0){
                                    Board.board.get(playerHeight).set(playerLane,0); //setting current shell 0;
                                    Board.board.get(playerHeight + 1).set(playerLane,playerId); // setting backward lane to player ID
                                    playerIdToHeightMap.put(playerId,playerHeight+1);
                                    boardObj.boardGUI();
                                    System.out.println("successful forced backwards move ");
                                    playerIdToMoveSkipCounter.put(playerId,0);
                                }
                                else{
                                    // player was supposed to move backwards -> but since it is blocked -> player has to lose points
                                    // and stay at the current shell
                                    // cannot allow sideways movement and give +2 points
                                    double currentScore = 0;
                                    if(playerIdToScoreMap.containsKey(playerId)){
                                        currentScore = playerIdToScoreMap.get(playerId);
                                    }
                                    currentScore -= 5.0; //assigning negative score to player
                                    playerIdToScoreMap.put(playerId,currentScore);
                                    playerIdToMoveSkipCounter.put(playerId,0);
                                    boardObj.boardGUI();
                                }
                            }
                            else{
                                boardObj.boardGUI();
                                System.out.println("Damn! you cant even move back. So, stay put !! : " + playerId);
                            }
                        }
                        break;
                    case 4: //miss
                        System.out.println("chance skipped for player : "+playerId);
                        boardObj.boardGUI();
                        break;
                }
                break;
            case 2:    /******************** Case for movement of 2 shells *********************/
                switch(dice2){
                    case 1:
                    case 2:            // forward
                        if(playerHeight - 2 >0){  //ERROR -> check this corner case
                            if(Board.board.get(playerHeight - 2).get(playerLane) == 0){
                                Board.board.get(playerHeight).set(playerLane,0); // setting current shell to 0
                                Board.board.get(playerHeight - 2).set(playerLane,playerId); // setting destination shell as player ID
                                playerIdToHeightMap.put(playerId,playerHeight-2);
                                playerIdToMoveSkipCounter.put(playerId,0);
                                boardObj.boardGUI();
                            }
                            else if(Board.board.get(playerHeight-2).get(playerLane)  == 11){ // pit //ERROR-> check this if
                                // player can move forward and lose 5 points and get redirected to previous shell
                                // player can skip chance
                                /** calling function for handling pit obstacle **/
                                pitObstacleObj.pitObstacleMovement(playerId,playerHeight,playerIdToLaneMap.get(playerId), boardLength, dice1, dice2);
                            }
                            else if(Board.board.get(playerHeight-2).get(playerLane)  == 12){ // swap // ERROR -> check this if
                                // no other option move -> no need to but since player cannot skip more than two straight chances -> so, either move backward or swap
                                /** Task **/
                                swapObstacleObj.swapObstacleMovement(playerId,playerHeight,playerIdToLaneMap.get(playerId), boardLength, boardWidth, dice1, dice2);
                            }
                            else if(Board.board.get(playerHeight-2).get(playerLane)  == 13){ // point half // ERROR -> check corner case
                                // if a win is possible soon then this can be an option .
                                // since this is just 1 move forward -> won't consider doing this if it can be avoided
                                System.out.println("code entering point half obstacle");
                                scoreHalfObstacleObj.scoreHalfObstacleMovement(playerId, playerHeight, playerIdToLaneMap.get(playerId), boardLength, boardWidth, dice1, dice2);

                            }
                            else{
                                boardObj.boardGUI();
                                System.out.println("You have a competitor there who refuses to move "); // ERROR -> put some penalty here
                            }
                        }
                        else{
                            Board.board.get(playerHeight).set(playerLane,0);
                            playerIdToHeightMap.put(playerId,0); // winning case
                            //winner = true;
                            Board.board.get(0).set(playerLane,playerId);
                            double currentScore = playerIdToScoreMap.get(playerId);
                            currentScore += 20.0;
                            playerIdToScoreMap.put(playerId,currentScore);
                            playerScoreTrackerOnWin(); // calling for getting scores
                            boardObj.boardGUI();
                            System.out.println("Congratulations on the win : "+Main.playerIdToNameMap.get(playerId)); // ERROR -> get player name instead of ID
                            return true;
                        }
                        break;
                    case 3:            // backward
                        if(playerIdToMoveSkipCounter.get(playerId) < 3){ // if skip is possible
                            // skip
                            int currentSkipCount = playerIdToMoveSkipCounter.get(playerId);
                            currentSkipCount ++;
                            playerIdToMoveSkipCounter.put(playerId,currentSkipCount);
                            boardObj.boardGUI();
                            System.out.println("Successfully skipped a forced backward motion : "+playerId);
                        }
                        else {
                            if(playerHeight + 2 < boardWidth-1){ // ERROR -> check this condition
                                if(Board.board.get(playerHeight + 2).get(playerLane) == 0){
                                    Board.board.get(playerHeight).set(playerLane,0); //setting current shell 0;
                                    Board.board.get(playerHeight + 2).set(playerLane,playerId); // setting backward lane to player ID
                                    playerIdToHeightMap.put(playerId,playerHeight+2);
                                    boardObj.boardGUI();
                                    System.out.println("successful forced double backwards move ");
                                    playerIdToMoveSkipCounter.put(playerId,0);
                                }
                                else{
                                    // player was supposed to move backwards -> but since it is blocked -> player has to lose points
                                    // and stay at the current shell
                                    double currentScore = 0;
                                    if(playerIdToScoreMap.containsKey(playerId)){
                                        currentScore = playerIdToScoreMap.get(playerId);
                                    }
                                    currentScore -= 5.0; // assigning negative score to player
                                    playerIdToScoreMap.put(playerId,currentScore);
                                    playerIdToMoveSkipCounter.put(playerId,0);
                                    boardObj.boardGUI();
                                }
                            }
                            else{
                                boardObj.boardGUI();
                                System.out.println("Damn! you cant even move back. So, stay put !! : " + playerId);
                            }
                        }
                        break;
                    case 4:            // miss
                        boardObj.boardGUI();
                        System.out.println("chance skipped for player : "+playerId);
                        break;
                }
                break;
            case 3: /*************************** Case for movement of 3 shells **********************************/
                switch(dice2){
                    case 1:            // forward
                    case 2:            // forward
                        if(playerHeight - 3 >0){
                            if(Board.board.get(playerHeight - 3).get(playerLane)==0){
                                Board.board.get(playerHeight).set(playerLane,0);
                                Board.board.get(playerHeight - 3).set(playerLane,playerId);
                                double currentScore = playerIdToScoreMap.get(playerId);
                                currentScore += 5.0;
                                playerIdToScoreMap.put(playerId,currentScore);
                                playerIdToHeightMap.put(playerId,playerHeight-3);
                                playerIdToMoveSkipCounter.put(playerId,0);
                                boardObj.boardGUI();
                            }
                            else if(Board.board.get(playerHeight-3).get(playerLane)  == 11){ // pit
                                // player can move forward and lose 5 points and get redirected to previous shell
                                // player can skip chance
                                /** calling function for handling pit obstacle **/
                                pitObstacleObj.pitObstacleMovement(playerId,playerHeight,playerIdToLaneMap.get(playerId), boardLength, dice1, dice2);
                            }
                            else if(Board.board.get(playerHeight-3).get(playerLane)  == 12){ // swap
                                // no other option move -> no need to but since player cannot skip more than two straight chances -> so, either move backward or swap
                                /** Task **/
                                swapObstacleObj.swapObstacleMovement(playerId,playerHeight,playerIdToLaneMap.get(playerId), boardLength, boardWidth, dice1, dice2);
                            }
                            else if(Board.board.get(playerHeight-3).get(playerLane)  == 13){ // point half // ERROR -> check corner case for playerHeight + 1 <= width
                                // if a win is possible soon then this can be an option .
                                // since this is just 1 move forward -> won't consider doing this if it can be avoided
                                System.out.println("code entering point half obstacle");
                                scoreHalfObstacleObj.scoreHalfObstacleMovement(playerId, playerHeight, playerIdToLaneMap.get(playerId), boardLength, boardWidth, dice1, dice2);
                            }
                            else{
                                boardObj.boardGUI();
                                System.out.println("You have a competitor there who refuses to move "); // ERROR -> put some penalty here
                            }
                        }
                        else{
                            Board.board.get(playerHeight).set(playerLane,0);
                            playerIdToHeightMap.put(playerId,0); // winning case
                            //winner = true;
                            Board.board.get(0).set(playerLane,playerId);
                            double currentScore = playerIdToScoreMap.get(playerId);
                            currentScore += 20.0;
                            playerIdToScoreMap.put(playerId,currentScore);
                            playerScoreTrackerOnWin(); // calling for getting scores
                            boardObj.boardGUI();
                            System.out.println("YEESSSSS!! We have a winner : "+Main.playerIdToNameMap.get(playerId)); // ERROR -> get player name instead of ID
                            return true;
                        }
                        break;
                    case 3:            // backward
                        if(playerIdToMoveSkipCounter.get(playerId) < 3){ // if skip is possible
                            // skip
                            int currentSkipCount = playerIdToMoveSkipCounter.get(playerId);
                            currentSkipCount ++;
                            playerIdToMoveSkipCounter.put(playerId,currentSkipCount);
                            boardObj.boardGUI();
                            System.out.println("Successfully skipped a forced backward motion : "+playerId);
                        }
                        else {
                            if(playerHeight + 3 < boardWidth){
                                if(Board.board.get(playerHeight + 3).get(playerLane) == 0){
                                    Board.board.get(playerHeight).set(playerLane,0); //setting current shell 0;
                                    Board.board.get(playerHeight + 3).set(playerLane,playerId); // setting backward lane to player ID
                                    playerIdToHeightMap.put(playerId,playerHeight+3);
                                    System.out.println("successful forced triple backwards move ");
                                    playerIdToMoveSkipCounter.put(playerId,0);
                                    boardObj.boardGUI();
                                }
                                else{
                                    // player was supposed to move backwards -> but since it is blocked -> player has to lose points
                                    // and stay at the current shell
                                    double currentScore = 0;
                                    if(playerIdToScoreMap.containsKey(playerId)){
                                        currentScore = playerIdToScoreMap.get(playerId);
                                    }
                                    currentScore -= 5.0;
                                    playerIdToScoreMap.put(playerId,currentScore);
                                    playerIdToMoveSkipCounter.put(playerId,0);
                                    boardObj.boardGUI();
                                }
                            }
                            else{
                                boardObj.boardGUI();
                                System.out.println("Damn! you cant even move back. So, stay put !! : " + playerId);
                            }
                        }
                        break;
                    case 4:            // miss
                        boardObj.boardGUI();
                        System.out.println("chance skipped for player : "+playerId);
                        break;
                }
                break;
            case 4:
                switch(dice2){
                    case 1:            // forward
                    case 2:
                        if(playerHeight - 4 > 0){
                            if(Board.board.get(playerHeight-4).get(playerLane) == 0){
                                Board.board.get(playerHeight).set(playerLane,0);
                                Board.board.get(playerHeight-4).set(playerLane,playerId);
                                double currentScore = playerIdToScoreMap.get(playerId);
                                currentScore += 5.0;
                                playerIdToScoreMap.put(playerId,currentScore);
                                playerIdToHeightMap.put(playerId,playerHeight-4);
                                playerIdToMoveSkipCounter.put(playerId,0);
                                boardObj.boardGUI();
                            }
                            else if(Board.board.get(playerHeight-4).get(playerLane)  == 11){ // pit
                                // player can move forward and lose 5 points and get redirected to previous shell
                                // player can skip chance
                                /** calling function for handling pit obstacle **/
                                pitObstacleObj.pitObstacleMovement(playerId,playerHeight,playerIdToLaneMap.get(playerId), boardLength, dice1, dice2);
                            }
                            else if(Board.board.get(playerHeight-4).get(playerLane)  == 12){ // swap
                                // no other option move -> no need to but since player cannot skip more than two straight chances -> so, either move backward or swap
                                /** Task **/
                                swapObstacleObj.swapObstacleMovement(playerId,playerHeight,playerIdToLaneMap.get(playerId), boardLength, boardWidth, dice1, dice2);
                            }
                            else if(Board.board.get(playerHeight-4).get(playerLane)  == 13){ // point half // ERROR -> check corner case for playerHeight + 1 <= width
                                // if a win is possible soon then this can be an option .
                                // since this is just 1 move forward -> won't consider doing this if it can be avoided
                                System.out.println("code entering point half obstacle");
                                scoreHalfObstacleObj.scoreHalfObstacleMovement(playerId, playerHeight, playerIdToLaneMap.get(playerId), boardLength, boardWidth, dice1, dice2);
                            }
                            else{
                                boardObj.boardGUI();
                                System.out.println("You have a competitor there who refuses to move "); // ERROR -> put some penalty here
                            }

                        }
                        else {
                            Board.board.get(playerHeight).set(playerLane,0);
                            playerIdToHeightMap.put(playerId,0);
                            //winner = true;
                            Board.board.get(0).set(playerLane,playerId);
                            double currentScore = playerIdToScoreMap.get(playerId);
                            currentScore += 20.0;
                            playerIdToScoreMap.put(playerId,currentScore);
                            playerScoreTrackerOnWin(); // calling for getting scores
                            boardObj.boardGUI();
                            System.out.println("Hurray!! here is your winner!! : "+Main.playerIdToNameMap.get(playerId));
                            return true;
                        }
                        break;
                    case 3:            // backward
                        if(playerIdToMoveSkipCounter.get(playerId) < 3){ // if skip is possible
                            // skip
                            int currentSkipCount = playerIdToMoveSkipCounter.get(playerId);
                            currentSkipCount ++;
                            playerIdToMoveSkipCounter.put(playerId,currentSkipCount);
                            boardObj.boardGUI();
                            System.out.println("Successfully skipped a forced backward motion : "+playerId);
                        }
                        else {
                            if(playerHeight + 4 < boardWidth){
                                if(Board.board.get(playerHeight + 4).get(playerLane) == 0){
                                    Board.board.get(playerHeight).set(playerLane,0); //setting current shell 0;
                                    Board.board.get(playerHeight + 4).set(playerLane,playerId); // setting backward lane to player ID
                                    playerIdToHeightMap.put(playerId,playerHeight+4);
                                    boardObj.boardGUI();
                                    System.out.println("successful forced four times backward move ");
                                    playerIdToMoveSkipCounter.put(playerId,0);
                                }
                                else{
                                    // player was supposed to move backwards -> but since it is blocked -> player has to lose points
                                    // and stay at the current shell
                                    double currentScore = 0;
                                    if(playerIdToScoreMap.containsKey(playerId)){
                                        currentScore = playerIdToScoreMap.get(playerId);
                                    }
                                    currentScore -= 5.0;
                                    playerIdToScoreMap.put(playerId,currentScore);
                                    playerIdToMoveSkipCounter.put(playerId,0);
                                    boardObj.boardGUI();
                                }
                            }
                            else{
                                boardObj.boardGUI();
                                System.out.println("Damn! you cant even move back. So, stay put !! : " + playerId);
                            }
                        }
                        break;
                    case 4:            // miss
                        boardObj.boardGUI();
                        System.out.println("chance skipped for player : "+playerId);
                        break;
                }
                break;
        }
        System.out.println("code exiting player movement for : "+playerId);
        return winner;
    }
}
