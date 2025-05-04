package org.example;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestPathEnum {
    @Test
    public void Test_Path_WALL_Value() {
        assertEquals(8, Path.WALL.get());
    }

    @Test
    public void Test_Path_PATH_Value() {
        assertEquals(0, Path.PATH.get());
    }

    @Test
    public void Test_Path_GOAL_Value() {
        assertEquals(1, Path.GOAL.get());
    }

    @Test
    public void Test_Path_START_Value() {
        assertEquals(2, Path.START.get());
    }
}
