The Main class in the Main package compiles and runs the code


The initial home page is displayed when the code is compiled.
The home page requests for board length, board width and number of players.
The NEXT button on home page call a button for adding player details
The player details page compiles for each player and the data is fed.
After add player button is clicked -> player name and age combination is searched in the PlayerDatabase.txt file
If the player already exists then the corresponding player id in the database is fetched and the same id is used.
The corresponding player's score is also fetched from the PlayerIdToScore.txt file is fetched and mapped to the current game.
Else a new random number is allocated to the new player as his/her player id

Once all the players are entered , the board is prepared
    Player lanes are allocated randomly
    Obstacle types and the number of obstacles are decided on the basis of board dimensions
    The positions o the obstacles are also selected at random.
All the data is created and fed in the board.
The final created board is displayed with players at their respective randomly selected lanes and the randomly allocated obstacles.
A dice gui is loaded along with the board.
Before the first dice is rolled the players list array is sorted on the basis of (playerAge+ playerNameLength)/2 .
The player with higher values of (playerAge+ playerNameLength)/2 is given the priority in rolling the dice.
The first round of dice is rolled anonymously as the players are not aware who is the first one.
Once the first round finishes, players a re aware of their sequence adn the die is rolled accordingly.
There are two dices rolled .
Dice 1 -> number of moves
Dice 2 -> direction of move (1,2 -> forward || 3-> backward || 4 -> skip)
Once the dices are rolled , the possibility of the move is checked
    If an obstacle exists at that position then the player is given an option of make a sideways movement.
    If a sideways movement is possible then the player is allowed to move sideways
    Else, the payer is given to skip the move.
    Possibility of skip depends on the skip counter available for a player. Three continuous skips are allowed to a player.
    After which the player has to make a move and lose points.

    If there are no obstacles present then the player successfully makes a move and the skip counter is set to 0 for that player.
The dices is rolled until a winner is found
Once a winner is found then the all the players on the board with their current score is taken and the data is written in the playerIdToScore.txt
file.
The leader board is updated after fetching data from the playerIdToScore.txt file and the leaderboard is displayed with the top 10 overall players of the game.
The final congratulations screen is displayed before the leaderboard with the name of the winner on it.

INFORMATION:

JUnit 5.8.2 is being used for unit testing
The project is a JavaFx project.
No additional manual installations or imports should be required. intelliJ should do all by itself.

Note:
I couldn't complete writing all the test cases in the stipulated time because of excessive workload combining all the modules. I apologise for that.
I will further continue working on this project and make some future advancements. One of them is also mentioned in the java doc.

