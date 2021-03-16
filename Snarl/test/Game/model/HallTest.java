package Game.model;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

//Tests for the Hall class
public class HallTest {
  Hall hall1;
  Hall hall2;
  Room dummyRoom1;
  Room dummyRoom2;

  @Before
  public void initEntities() {
    ModelCreator creator = new ModelCreator();
    this.hall1 = creator.initializeHall4();
    this.hall1 = creator.initializeHall5();
    this.dummyRoom1 = creator.getDummyRoom1();
    this.dummyRoom2 = creator.getDummyRoom2();
  }

  //Constructor and connectRooms tested by initializing hallways
  //Since a hallway must connect two rooms the connectRooms function must be called
  //before we display it. In the future we may want to put the connectRooms functionality
  //into the constructor.
  @Test
  public void testHall1Constructor() {
    String expectedOut = ""
        + "     \n"
        + "    *\n"
        + "    *\n"
        + "    *\n"
        + " ****\n";

    LevelComponentTest.checkLevelComponentLooksLike(this.hall1, expectedOut);
  }

  @Test
  public void testHall2Constructor() {
    String expectedOut = ""
        + "  **\n"
        + "   *\n"
        + "   *\n"
        + "***P\n"
        + "Z   \n"
        + "G   \n"
        + "*   \n"
        + "*   \n"
        + "    \n";

    LevelComponentTest.checkLevelComponentLooksLike(this.hall2, expectedOut);
  }

  //Tests that the starting rooms are initialized properly. This is done in the constructor
  //and the returned room should be pointing to the same thing that we passed into the constructor.
  @Test
  public void testGetStartRoom() {
    assertEquals(this.dummyRoom1, this.hall1.getStartRoom());
    assertEquals(this.dummyRoom2, this.hall2.getStartRoom());
  }

  //Tests that the ending rooms are initialized properly. This is done in the constructor
  //and the returned room should be pointing to the same thing that we passed into the constructor.
  @Test
  public void testGetEndRoom() {
    assertEquals(this.dummyRoom2, this.hall1.getEndRoom());
    assertEquals(this.dummyRoom1, this.hall2.getEndRoom());
  }

}