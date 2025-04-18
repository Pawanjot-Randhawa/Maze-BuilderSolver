package org.example;

import java.util.List;
public class StepGenerator {
    private List<int[]> path;
    private int index = 0;

    public StepGenerator() {
        this.path = null;
    }

    public StepGenerator(List<int[]> steps) {
        this.path = steps;
    }


    public void SetSteps(List<int[]> steps) {
        this.path = steps;
    }

    
    /**
     * Sets the index
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
    }


    /**
     * 
     * @return previous step
     */
    public int[] StepUp() {
        if(this.index >= this.path.size()) {
            this.index = this.path.size() - 1;
            return null;
        }

        final int[] result = this.path.get(this.index);
        this.index = (this.index + 1) >= this.path.size() ? this.index : this.index + 1;
        
        return result;
    }


    public void Reset() {
        this.index = 0;
    }


    public int[] StepBack() {
        if(this.index < 0) {
            this.index = 0;
            return null;
        }

        final int[] result = this.path.get(this.index);
        this.index = (this.index - 1) < 0 ? this.index : this.index - 1;

        return result;
    }

    public boolean finished() {
        System.out.println(index);
        return (this.index >= this.path.size()) || (this.index < 0);
    }
}
