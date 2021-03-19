package Game.modelView;

import static org.junit.Assert.*;

import Game.model.ModelCreator;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

//Tests for the LevelModelView interface which gives the view everything it needs to draw
//without allowing the view to edit the model at all.
public class LevelModelViewTest {
 
  //This is mostly tested by TextualLevelViewTest but we will add a couple tests here to confirm
  //It is much easier to visualize tests with the textual output and they are essentially identical
  //so that is why we do most of the testing there.

  //Test that getting the map for the level returns the correct
  //map of EntityType
  @Test
  public void testGetMap() {
    ModelCreator creator = new ModelCreator();
    //Initialize ModelView
    LevelModelView modelView = creator.initializeLevel1();

    EntityType w = EntityType.WALL;
    EntityType s = EntityType.SPACE;
    EntityType h = EntityType.HALL_SPACE;
    EntityType e = EntityType.EMPTY;
    EntityType k = EntityType.KEY;
    EntityType x = EntityType.EXIT;
    EntityType p = EntityType.PLAYER;
    EntityType g = EntityType.GHOST;
    EntityType z = EntityType.ZOMBIE;

    List<List<EntityType>> expectedMap = Arrays.asList(
        Arrays.asList(w, w, w, w, e, e, e, e, e, e, e, e, e, e, e, e, e, e),
        Arrays.asList(w, s, s, w, e, e, e, e, e, e, e, e, e, e, e, e, e, e),
        Arrays.asList(w, s, s, s, p, h, h, e, e, e, e, e, e, e, e, e, e, e),
        Arrays.asList(w, w, w, w, e, e, h, e, e, e, e, e, e, e, e, e, e, e),
        Arrays.asList(e, e, e, e, e, e, h, e, e, e, e, e, e, e, e, e, e, e),
        Arrays.asList(e, e, e, e, e, e, h, e, e, e, e, e, e, e, e, e, e, e),
        Arrays.asList(e, e, e, e, e, e, h, e, e, e, e, e, e, e, e, e, e, e),
        Arrays.asList(e, e, e, e, e, w, s, w, w, e, e, e, e, e, e, e, e, e),
        Arrays.asList(e, e, e, e, e, w, s, g, w, e, e, e, e, e, e, e, e, e),
        Arrays.asList(e, e, e, e, e, w, s, s, w, e, e, e, e, e, e, e, e, e),
        Arrays.asList(e, e, e, e, e, w, s, p, w, e, e, e, e, w, w, w, w, w),
        Arrays.asList(e, e, h, h, h, s, s, x, s, h, h, h, h, s, s, s, s, w),
        Arrays.asList(e, e, h, e, e, w, w, w, w, e, e, e, e, w, s, s, s, w),
        Arrays.asList(e, e, h, e, e, e, e, e, e, e, e, e, e, w, s, s, s, w),
        Arrays.asList(w, w, g, w, w, w, e, e, e, e, e, e, e, w, s, s, s, w),
        Arrays.asList(w, s, s, s, s, w, e, e, e, e, e, e, e, w, w, w, w, w),
        Arrays.asList(w, s, s, s, s, w, e, e, e, e, e, e, e, e, e, e, e, e),
        Arrays.asList(w, s, z, p, k, w, e, e, e, e, e, e, e, e, e, e, e, e),
        Arrays.asList(w, w, w, w, w, w, e, e, e, e, e, e, e, e, e, e, e, e)
    );

    //Check that level was represented as expected
    assertEquals(expectedMap, modelView.getMap());

  }
}