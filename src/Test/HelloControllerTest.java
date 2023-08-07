import Main.HelloController;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {
    @Test
    void enteredDataTest(){
        HelloController control = new HelloController();
        control.setNoOfPlayers(3);
        int noOfPlayers = control.getNoOfPlayers();
   // assertEquals(noOfPlayers>2,noOfPlayers);
        assertTrue(noOfPlayers>2);
    }
    @Test
    void dataTest(){
        HelloController control = new HelloController();
        control.setNoOfPlayers(3);
        int noOfPlayers = control.getNoOfPlayers();
        int boardLength = HelloController.boardLength;
        int boardWidth = HelloController.boardWidth;
        assertFalse(noOfPlayers > boardLength);
    }
    @Test
    void rollingDice(){
        HelloController dice = new HelloController();
        int dice1 = dice.rollingDice1();
        int dice2 = dice.rollingDice2();
        assertTrue(dice1 >0 && dice1 < 5 && dice2 > 0 && dice2 <5);
    }

}