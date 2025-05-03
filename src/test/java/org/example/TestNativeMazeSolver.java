package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.example.Native.MazeSolver;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestNativeMazeSolver {

    private final int SIMPLE_WIDTH = 5;
    private final int SIMPLE_HEIGHT = 5;

    private final int[][] SimpleMazeWithSingleGoal = {
        { Path.WALL.get(), Path.PATH.get(), Path.PATH.get(), Path.WALL.get(), Path.PATH.get() },
        { Path.PATH.get(), Path.WALL.get(), Path.WALL.get(), Path.PATH.get(), Path.PATH.get() },
        { Path.WALL.get(), Path.PATH.get(), Path.WALL.get(), Path.PATH.get(), Path.GOAL.get() },
        { Path.WALL.get(), Path.PATH.get(), Path.WALL.get(), Path.WALL.get(), Path.WALL.get() },
        { Path.WALL.get(), Path.WALL.get(), Path.PATH.get(), Path.PATH.get(), Path.START.get() }, 
    };

    private final int COMPLEX_WIDTH = 6;
    private final int COMPLEX_HEIGHT = 6;
    private final int[][] ComplexMazeWithMultipleGoals = {
        { Path.PATH.get(), Path.PATH.get(), Path.PATH.get(), Path.PATH.get(), Path.START.get(), Path.WALL.get() },
        { Path.PATH.get(), Path.WALL.get(), Path.PATH.get(), Path.WALL.get(), Path.PATH.get(), Path.WALL.get()  },
        { Path.WALL.get(), Path.PATH.get(), Path.PATH.get(), Path.PATH.get(), Path.PATH.get(), Path.PATH.get()  },
        { Path.WALL.get(), Path.WALL.get(), Path.PATH.get(), Path.PATH.get(), Path.WALL.get(), Path.WALL.get()  },
        { Path.GOAL.get(), Path.PATH.get(), Path.WALL.get(), Path.PATH.get(), Path.WALL.get(), Path.GOAL.get()  }, 
        { Path.PATH.get(), Path.GOAL.get(), Path.WALL.get(), Path.WALL.get(), Path.PATH.get(), Path.WALL.get()  }, 
    };

    private final Maze simpleMaze = new Maze(
        this.SimpleMazeWithSingleGoal, 
        this.SIMPLE_WIDTH, 
        this.SIMPLE_HEIGHT
    );
    
    private final Maze complexMaze = new Maze(
        this.ComplexMazeWithMultipleGoals, 
        this.COMPLEX_WIDTH, 
        this.COMPLEX_HEIGHT
    );


    
    @Test
    public void TestLibraryLoaded() {
        assertEquals("returned from cpp code", MazeSolver.returnHello());
    }    

    @Test
    public void TestLibraryNameIsCorrect() {
        assertEquals("native", MazeSolver.LIBRARY_NAME);
    }




    @Test
    public void Test_AStar_Simple_Maze_Correct_Path() {
        final List<int[]> receivedSteps = SolverAPI.GetInstance(this.simpleMaze)
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
    public void Test_AStar_Simple_Maze_Wrong_Path() {
        final List<int[]> receivedSteps = SolverAPI.GetInstance(this.simpleMaze)
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
    public void Test_Dijkstra_Simple_Maze_Correct_Path() {
        final List<int[]> receivedSteps = SolverAPI.GetInstance(this.simpleMaze)
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
    public void Test_Dijkstra_Simple_Maze_Wrong_Path() {
        final List<int[]> receivedSteps = SolverAPI.GetInstance(this.simpleMaze)
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




    @Test
    public void Test_BlockNdPath_Simple_Maze_Correct_Path() {
        final List<int[]> receivedSteps = SolverAPI.GetInstance(this.simpleMaze)
                                                    .SolveWith(SolveStrategy.BLOCKNDPATH);

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
            {1,4},
            {2,4},
        };

        IntStream.range(0, receivedSteps.size())
            .forEach(i -> {
                final boolean isMatch = Arrays.equals(receivedSteps.get(i), expectedSteps[i]);
                assertEquals(true, isMatch);
            });
    }

    @Test
    public void Test_BlockNdPath_Simple_Maze_Wrong_Path() {
        final List<int[]> receivedSteps = SolverAPI.GetInstance(this.simpleMaze)
                                                    .SolveWith(SolveStrategy.BLOCKNDPATH);

        final int[][] expectedSteps = {
            {2,4},
            {1,4},
            {1,3},
            {0,2},
            {0,1},
            {4,4},
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
    public void Test_BlockNdPath_Complex_Maze_Correct_Path() {
        final List<int[]> receivedSteps = SolverAPI.GetInstance(this.complexMaze)
                                                    .SolveWith(SolveStrategy.BLOCKNDPATH);

        final int[][] expectedSteps = {
            {0,4},
            {0,3},
            {0,2},
            {0,1},
            {0,0},
            {1,0},
            {2,1},
            {2,2},
            {2,3},
            {2,4},
            {2,5},
            {1,4},
            {0,4},
            {0,3},
            {0,2},
            {0,1},
            {0,0},
            {1,0},
            {2,1},
            {2,2},
            {2,3},
            {2,4},
            {2,5},
            {0,4},
            {0,3},
            {0,2},
            {0,1},
            {0,0},
            {1,0},
            {2,1},
            {2,2},
            {2,3},
            {2,4},
            {3,3},
            {3,2},
            {4,3},
            {5,4},
            {4,5},
        };

        IntStream.range(0, receivedSteps.size())
            .forEach(i -> {
                final boolean isMatch = Arrays.equals(receivedSteps.get(i), expectedSteps[i]);
                assertEquals(true, isMatch);
            });
    }

    @Test
    public void Test_BlockNdPath_Complex_Maze_Wrong_Path() {
        final List<int[]> receivedSteps = SolverAPI.GetInstance(this.complexMaze)
                                                    .SolveWith(SolveStrategy.BLOCKNDPATH);

        final int[][] expectedSteps = {
            {1,4},
            {2,3},
            {3,2},
            {4,1},
            {5,0},
            {2,0},
            {3,1},
            {4,2},
            {5,3},
            {1,4},
            {3,5},
            {6,4},
            {5,4},
            {0,5},
            {0,7},
            {0,8},
            {0,2},
            {1,2},
            {2,4},
            {2,4},
            {3,3},
            {5,5},
            {1,1},
            {4,4},
            {0,-1},
            {0,-1},
            {0,-1},
            {0,-1},
            {1,-1},
            {2,-1},
            {2,-1},
            {2,-1},
            {2,-1},
            {3,-1},
            {3,-1},
            {4,-1},
            {5,-1},
            {4,-1},
        };

        IntStream.range(0, receivedSteps.size())
            .forEach(i -> {
                final boolean isMatch = Arrays.equals(receivedSteps.get(i), expectedSteps[i]);
                assertEquals(false, isMatch);
            });
    }




    @Test
    public void Test_BFS_Simple_Maze_Correct_Path() {
        final List<int[]> receivedSteps = SolverAPI.GetInstance(this.simpleMaze)
                                                    .SolveWith(SolveStrategy.BFS);

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
            {1,4},
            {2,3},
            {2,4},
        };

        IntStream.range(0, receivedSteps.size())
            .forEach(i -> {
                final boolean isMatch = Arrays.equals(receivedSteps.get(i), expectedSteps[i]);
                assertEquals(true, isMatch);
            });
    }

    @Test
    public void Test_BFS_Simple_Maze_Wrong_Path() {
        final List<int[]> receivedSteps = SolverAPI.GetInstance(this.simpleMaze)
                                                    .SolveWith(SolveStrategy.BFS);

        final int[][] expectedSteps = {
            {2,4},
            {2,3},
            {1,4},
            {1,3},
            {0,2},
            {0,1},
            {4,4},
            {2,1},
            {3,1},
            {4,2},
            {4,3},
            {4,4},
        };

        IntStream.range(0, receivedSteps.size())
            .forEach(i -> {
                final boolean isMatch = Arrays.equals(receivedSteps.get(i), expectedSteps[i]);
                if(isMatch) {
                    System.out.println("Maze is: " + Arrays.toString(receivedSteps.get(i)));
                    System.out.println("Expected Maze is: " + Arrays.toString(expectedSteps[i]));

                }
                assertEquals(false, isMatch);
            });
    }

    @Test
    public void Test_BFS_Complex_Maze_Correct_Path() {
        final List<int[]> receivedSteps = SolverAPI.GetInstance(this.complexMaze)
                                                    .SolveWith(SolveStrategy.BFS);

        final int[][] expectedSteps = {
            {0,4},
            {0,3},
            {1,4},
            {0,2},
            {1,2},
            {2,4},
            {2,5},
            {2,3},
            {0,1},
            {2,2},
            {2,1},
            {3,3},
            {3,2},
            {0,0},
            {1,0},
            {4,3},
            {4,1},
            {5,4},
            {4,0}
        };

        IntStream.range(0, receivedSteps.size())
            .forEach(i -> {
                final boolean isMatch = Arrays.equals(receivedSteps.get(i), expectedSteps[i]);
                assertEquals(true, isMatch);
            });
    }

    @Test
    public void Test_BFS_Complex_Maze_Wrong_Path() {
        final List<int[]> receivedSteps = SolverAPI.GetInstance(this.complexMaze)
                                                    .SolveWith(SolveStrategy.BFS);

        final int[][] expectedSteps = {
            {1,4},
            {1,3},
            {2,4},
            {1,2},
            {2,2},
            {2,3},
            {2,4},
            {2,2},
            {0,2},
            {3,3},
            {3,3},
            {0,0},
            {0,1},
            {23,1},
            {34,12},
            {3,0},
            {2,5},
            {2,6},
            {2,11}
        };

        IntStream.range(0, receivedSteps.size())
            .forEach(i -> {
                final boolean isMatch = Arrays.equals(receivedSteps.get(i), expectedSteps[i]);
                assertEquals(false, isMatch);
            });
    }




    @Test
    public void Test_IDAStar_Simple_Maze_Correct_Path() {
        final List<int[]> receivedSteps = SolverAPI.GetInstance(this.simpleMaze)
                                                    .SolveWith(SolveStrategy.IDASTAR);

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
            {0,4},
            {2,4},
        };

        IntStream.range(0, receivedSteps.size())
            .forEach(i -> {
                final boolean isMatch = Arrays.equals(receivedSteps.get(i), expectedSteps[i]);
                assertEquals(true, isMatch);
            });
    }

    @Test
    public void Test_IDAStar_Simple_Maze_Wrong_Path() {
        final List<int[]> receivedSteps = SolverAPI.GetInstance(this.simpleMaze)
                                                    .SolveWith(SolveStrategy.IDASTAR);

        final int[][] expectedSteps = {
            {2,4},
            {0,4},
            {1,3},
            {0,2},
            {0,1},
            {4,4},
            {2,1},
            {3,1},
            {4,2},
            {4,3},
            {4,4},
        };

        IntStream.range(0, receivedSteps.size())
            .forEach(i -> {
                final boolean isMatch = Arrays.equals(receivedSteps.get(i), expectedSteps[i]);
                if(isMatch) {
                    System.out.println("Maze is: " + Arrays.toString(receivedSteps.get(i)));
                    System.out.println("Expected Maze is: " + Arrays.toString(expectedSteps[i]));

                }
                assertEquals(false, isMatch);
            });
    }

    @Test
    public void Test_IDAStar_Complex_Maze_Correct_Path() {
        final List<int[]> receivedSteps = SolverAPI.GetInstance(this.complexMaze)
                                                    .SolveWith(SolveStrategy.IDASTAR);

        final int[][] expectedSteps = {
            {0,4},
            {1,4},
            {0,3},
            {2,3},
            {1,2},
            {3,2},
            {2,1},
            {4,1},
            {5,0},
            {5,1},
        };

        IntStream.range(0, receivedSteps.size())
            .forEach(i -> {
                final boolean isMatch = Arrays.equals(receivedSteps.get(i), expectedSteps[i]);
                if(!isMatch) {
                    System.out.println("Maze is: " + Arrays.toString(receivedSteps.get(i)));
                    System.out.println("Expected Maze is: " + Arrays.toString(expectedSteps[i]));

                }
                assertEquals(true, isMatch);
            });
    }

    @Test
    public void Test_IDAStar_Complex_Maze_Wrong_Path() {
        final List<int[]> receivedSteps = SolverAPI.GetInstance(this.complexMaze)
                                                    .SolveWith(SolveStrategy.IDASTAR);

        final int[][] expectedSteps = {
            {1,4},
            {2,4},
            {3,3},
            {5,3},
            {2,2},
            {0,2},
            {2,3},
            {4,2},
            {5,5},
            {5,3},
        };

        IntStream.range(0, receivedSteps.size())
            .forEach(i -> {
                final boolean isMatch = Arrays.equals(receivedSteps.get(i), expectedSteps[i]);
                assertEquals(false, isMatch);
            });
    }



}


// if(isMatch) {
//     System.out.println("Maze is: " + Arrays.toString(receivedSteps.get(i)));
//     System.out.println("Expected Maze is: " + Arrays.toString(expectedSteps[i]));

// }