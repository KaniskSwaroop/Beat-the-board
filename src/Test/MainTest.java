import Main.Main;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void playerListTest(){
        int size = Main.playersList.size();
        assertEquals(2,2);
        int ageSize = Main.playerIdToNameMap.size();
        int nameSize = Main.playerIdToAgeMap.size();
        assertEquals(2,ageSize);
        assertEquals(2,nameSize);
        assertTrue(ageSize == size && nameSize == size);

    }


}