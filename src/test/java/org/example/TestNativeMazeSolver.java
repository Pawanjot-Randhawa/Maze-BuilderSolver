package org.example;

import org.example.Native.MazeSolver;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestNativeMazeSolver {
    @Test
    public void TestLibraryLoaded() {
        assertEquals("returned from cpp code", MazeSolver.returnHello());
    }    
}
