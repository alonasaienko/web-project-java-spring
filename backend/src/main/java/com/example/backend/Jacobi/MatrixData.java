package com.example.backend.Jacobi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;


public class MatrixData {
    @JsonProperty("A")
    private double[][] A;
    
    @JsonProperty("B")
    private double[] B;

    private static final Logger logger = LoggerFactory.getLogger(JacobiController.class);

    @JsonCreator
    public MatrixData(@JsonProperty("A") double[][] A, @JsonProperty("B") double[] B) {
        this.A = A;
        this.B = B;
        logger.info("Created MatrixData: {}", this);
    }
    
    public double[][] getA() {
        return A;
    }
    public double[] getB() {
        return B;
    }
    public void setA(double[][] a) {
        logger.info("Setting A: {}", Arrays.deepToString(a));
        A = a;
    }
    public void setB(double[] b) {
        logger.info("Setting B: {}", Arrays.toString(b));
        B = b;
    }
    @Override
    public String toString() {
        return "MatrixData{" +
                "A=" + Arrays.deepToString(A) +
                ", B=" + Arrays.toString(B) +
                '}';
    }
}