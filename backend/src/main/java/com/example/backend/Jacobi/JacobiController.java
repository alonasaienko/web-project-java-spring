package com.example.backend.Jacobi;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api")
public class JacobiController {
    @Async
    @PostMapping("/jacobi")
    public CompletableFuture<ResponseEntity<?>> calculate(@RequestBody MatrixData matrixData, @RequestHeader(value = "Authorization", required = false) String token) {
        System.out.println("Received request with data: " + matrixData);
        
        if (matrixData.getA() == null || matrixData.getB() == null) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body("Matrix A or B is null"));
        }
        
        double[][] A = matrixData.getA();
        double[] B = matrixData.getB();
        double EPS = 1e-10;

        List<JacobiResult> allResults = Jacobi.JacobiMethod(A, B, EPS);
        
        if (allResults.size() == Jacobi.MAX_ITER) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body("Did not converge within the maximum number of iterations."));
        }
        
        if (isAuthenticated(token)) { 
            return CompletableFuture.completedFuture(ResponseEntity.ok(allResults));
        } else {
            JacobiResult finalResult = allResults.get(allResults.size() - 1);
            return CompletableFuture.completedFuture(ResponseEntity.ok(Arrays.asList(finalResult)));
        }
    }

    private boolean isAuthenticated(String token) {
        return token != null && token.startsWith("Bearer ");
    }
}