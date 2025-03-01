package com.example.backend.Jacobi;

public class JacobiResult {
    private int iteration;
    private double[] values;

    public JacobiResult() {}

    public JacobiResult(int iteration, double[] values) {
        this.iteration = iteration;
        this.values = values;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }
}

