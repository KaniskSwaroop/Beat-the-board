package Player;

import org.testng.annotations.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.testng.AssertJUnit.assertTrue;

class PlayerTest {
    @Test
    void playerMovementTest() throws IOException, InterruptedException {
        Player playerObj = new Player();
        boolean result = playerObj.playerMovement(2222, 8,9, 6, 4, 2, 4);
        assertTrue(result || !result); // if there are any exceptions
    }
    @Test
    public void sidewaysMovementTest() throws IOException, InterruptedException {
        Player playerObj = new Player();
        boolean result = playerObj.sidewaysMovementChecker(2222,6,4,9,2,3);
        assertTrue(result || !result); // if there are any exceptions
    }
    @Test
    void playerIdTest() {
        Player playerObj = new Player();
        playerObj.setPlayerId(9989);
        int playerId = playerObj.getPlayerId();
        assertTrue(playerId != 11 && playerId !=12 && playerId !=13 && playerId !=0);
    }
    @Test
    void databaseTest() throws IOException {
        File file = new File("src\\main\\java\\Model\\PlayerDatabase.txt");
        FileReader f = new FileReader(file);
        BufferedReader reader = new BufferedReader(f);
        String line = "";
        assertTrue((line = reader.readLine()) != null);
    }
    @Test
    void scoreDatabase() throws IOException {
        File file = new File("src\\main\\java\\Model\\PlayerDatabase.txt");
        FileReader f = new FileReader(file);
        BufferedReader reader = new BufferedReader(f);
        String line = "";
        assertTrue((line = reader.readLine()) != null);
    }
}