package Game.model;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import org.junit.Before;
import org.junit.Test;

//Tests for the Hall class
public class HallTest {
  ModelCreator creator;
  Hall hall1;
  Hall hall2;
  Hall hall3;
  Room room1;
  Room room2;
  Room room4;

  @Before
  public void initEntities() {
    this.creator = new ModelCreator();
    this.room1 = this.creator.initializeRoom1();
    this.room2 = this.creator.initializeRoom2();
    this.room4 = this.creator.initializeRoom4();
    this.hall1 = creator.initializeHall1(room1, room2);
    this.hall2 = creator.initializeHall1Snake(room1, room2);
    this.hall3 = creator.initializeHall3(room2, room4);
  }

  //Constructor and connectRooms tested by initializing hallways
  @Test
  public void testHall1Constructor() {
    String expectedOut = ""
        + " ***\n"
        + "   *\n"
        + "   *\n"
        + "   *\n"
        + "   *\n"
        + "    \n";

    LevelComponentTest.checkLevelComponentLooksLike(this.hall1, expectedOut);
  }

  @Test
  public void testHall2Constructor() {
    String expectedOut = ""
        + " ******\n"
        + "      *\n"
        + "   ****\n"
        + "   *   \n"
        + "   *   \n"
        + "       \n";

    LevelComponentTest.checkLevelComponentLooksLike(this.hall2, expectedOut);
  }

  //Tests that the starting rooms are initialized properly. This is done in the constructor
  //and the returned room should be pointing to the same thing that we passed into the constructor.
  @Test
  public void testGetStartRoom() {
    assertEquals(this.room1, this.hall1.getStartRoom());
    assertEquals(this.room2, this.hall3.getStartRoom());
  }

  //Tests that the ending rooms are initialized properly. This is done in the constructor
  //and the returned room should be pointing to the same thing that we passed into the constructor.
  @Test
  public void testGetEndRoom() {
    assertEquals(this.room2, this.hall1.getEndRoom());
    assertEquals(this.room4, this.hall3.getEndRoom());
  }

}