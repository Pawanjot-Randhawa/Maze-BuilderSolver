package org.example;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestSolveStrategyEnum {
    @Test
    public void Test_AStar_Value() {
        assertEquals("AStar", SolveStrategy.ASTAR.get());
    }

    @Test
    public void Test_Dijkstra_Value() {
        assertEquals("Dijkstra", SolveStrategy.DIJKSTRA.get());
    }

    @Test
    public void Test_BlockNdPath_Value() {
        assertEquals("BlockNdPath", SolveStrategy.BLOCKNDPATH.get());
    }

    @Test
    public void Test_BFS_Value() {
        assertEquals("Bfs", SolveStrategy.BFS.get());
    }

    @Test
    public void Test_IDAStar_Value() {
        assertEquals("IDAStar", SolveStrategy.IDASTAR.get());
    }
}
