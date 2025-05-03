package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.example.Native.MazeSolver;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestNativeMazeSolver {

    private final int width = 5;
    private final int height = 5;

    private final int[][] SimpleMazeWith1Goal = {
        { Path.WALL.get(), Path.PATH.get(), Path.PATH.get(), Path.WALL.get(), Path.PATH.get() },
        { Path.PATH.get(), Path.WALL.get(), Path.WALL.get(), Path.PATH.get(), Path.PATH.get() },
        { Path.WALL.get(), Path.PATH.get(), Path.WALL.get(), Path.PATH.get(), Path.GOAL.get() },
        { Path.WALL.get(), Path.PATH.get(), Path.WALL.get(), Path.WALL.get(), Path.WALL.get() },
        { Path.WALL.get(), Path.WALL.get(), Path.PATH.get(), Path.PATH.get(), Path.START.get() }, 
    };

    @SuppressWarnings({ "ignore", "unused" })
    private final int[][] ComplexMazeWith2Goals = {
        { Path.WALL.get(), Path.PATH.get(), Path.PATH.get(), Path.WALL.get(), Path.PATH.get() },
        { Path.PATH.get(), Path.WALL.get(), Path.WALL.get(), Path.PATH.get(), Path.PATH.get() },
        { Path.WALL.get(), Path.PATH.get(), Path.WALL.get(), Path.PATH.get(), Path.GOAL.get() },
        { Path.WALL.get(), Path.PATH.get(), Path.WALL.get(), Path.WALL.get(), Path.WALL.get() },
        { Path.WALL.get(), Path.WALL.get(), Path.PATH.get(), Path.PATH.get(), Path.START.get() }, 
    };

    private final Maze maze = new Maze(this.SimpleMazeWith1Goal, this.width, this.height);

    @Test
    public void TestLibraryLoaded() {
        assertEquals("returned from cpp code", MazeSolver.returnHello());
    }    

    @Test
    public void TestLibraryNameIsCorrect() {
        assertEquals("native", MazeSolver.LIBRARY_NAME);
    }



    
    @Test
    public void Test_AStar_SimpleMaze_Correct_Path() {
        final List<int[]> receivedSteps = SolverAPI.GetInstance(this.maze)
                                            .SolveWith(SolveStrategy.ASTAR);
        
        final int[][] expectedSteps = {
            {4,4},
            {4,3},
            {4,2},
            {3,1},
            {2,1},
            {1,0},
            {0,1},
            {0,2},
            {1,3},
            {2,4},
        };

        IntStream.range(0, receivedSteps.size())
            .forEach(i -> {
                final boolean isMatch = Arrays.equals(receivedSteps.get(i), expectedSteps[i]);
                assertEquals(true, isMatch);
            });
    }

    @Test
    public void Test_AStar_SimpleMaze_Wrong_Path() {
        final List<int[]> receivedSteps = SolverAPI.GetInstance(this.maze)
                                            .SolveWith(SolveStrategy.ASTAR);
        
        final int[][] expectedSteps = {
            {2,4},
            {1,3},
            {0,2},
            {0,1},
            {1,0},
            {2,1},
            {3,1},
            {4,2},
            {4,3},
            {4,4},
        };

        IntStream.range(0, receivedSteps.size())
            .forEach(i -> {
                final boolean isMatch = Arrays.equals(receivedSteps.get(i), expectedSteps[i]);
                assertEquals(false, isMatch);
            });
    }




    @Test
    public void Test_Dijkstra_SimpleMaze_Correct_Path() {
        final List<int[]> receivedSteps = SolverAPI.GetInstance(this.maze)
                                                    .SolveWith(SolveStrategy.DIJKSTRA);

        final int[][] expectedSteps = {
            {4,4},
            {4,3},
            {4,2},
            {3,1},
            {2,1},
            {1,0},
            {0,1},
            {0,2},
            {1,3},
            {2,4},
        };

        IntStream.range(0, receivedSteps.size())
            .forEach(i -> {
                final boolean isMatch = Arrays.equals(receivedSteps.get(i), expectedSteps[i]);
                assertEquals(true, isMatch);
            });
    }

    @Test
    public void Test_Dijkstra_SimpleMaze_Wrong_Path() {
        final List<int[]> receivedSteps = SolverAPI.GetInstance(this.maze)
                                                    .SolveWith(SolveStrategy.DIJKSTRA);

        final int[][] expectedSteps = {
            {2,4},
            {1,3},
            {0,2},
            {0,1},
            {1,0},
            {2,1},
            {3,1},
            {4,2},
            {4,3},
            {4,4},
        };

        IntStream.range(0, receivedSteps.size())
            .forEach(i -> {
                final boolean isMatch = Arrays.equals(receivedSteps.get(i), expectedSteps[i]);
                assertEquals(false, isMatch);
            });
    }





}
