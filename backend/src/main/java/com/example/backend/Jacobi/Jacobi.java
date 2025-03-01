package com.example.backend.Jacobi;

import java.util.ArrayList;
import java.util.List;

public class Jacobi {
    static int MAX_ITER = 100;
    static final int SLEEP_TIME = 100;

    public static List<JacobiResult> JacobiMethod(double[][] A, double[] B, double EPS) {
        int n = A.length;
        double[] x = new double[n];
        double[] xNew = new double[n];

        List<JacobiResult> results = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            x[i] = 0.0;
        }

        for (int count = 0; count < MAX_ITER; count++) {
            for (int i = 0; i < n; i++) {
                double sum = 0.0;
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        sum += A[i][j] * x[j];
                    }
                }
                xNew[i] = (B[i] - sum) / A[i][i];
            }

            results.add(new JacobiResult(count + 1, xNew.clone()));

            boolean stop = true;
            for (int i = 0; i < n; i++) {
                if (Math.abs(xNew[i] - x[i]) > EPS) {
                    stop = false;
                    break;
                }
            }

            System.arraycopy(xNew, 0, x, 0, n);
            
            if (stop) break;

            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return results;
    }
}
