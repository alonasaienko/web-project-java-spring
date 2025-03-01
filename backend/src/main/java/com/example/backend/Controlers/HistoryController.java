package com.example.backend.Controlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.core.Authentication;

import com.example.backend.History.History;
import com.example.backend.History.HistoryService;
import com.example.backend.User.User;


@RestController
@RequestMapping("/api")
public class HistoryController {
    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/histories/all")
    public ResponseEntity<List<History>> getAllHistories() {
        List<History> histories = historyService.getAllHistories();
        return ResponseEntity.ok(histories);
    }

    @PostMapping("/jacobi/histories")
    public ResponseEntity<?> createHistory(@RequestBody History history) {
        System.out.println("Received request with history data: " + history);

        if (history.getData() == null || history.getResult() == null) {
            System.out.println("History data or result is null");
            return ResponseEntity.badRequest().build();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof User)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User user = (User) authentication.getPrincipal();
        history.setUser(user);
        try {
            History createdHistory = historyService.createHistory(history);
            System.out.println("Received history: " + createdHistory);
            return ResponseEntity.ok(createdHistory);
        } catch (Exception e) {
            System.out.println("Error saving history: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/histories/user/{id}")
    public ResponseEntity<History> getHistoryById(@PathVariable Long id) {
        History history = historyService.getHistory(id);
        return history != null ? ResponseEntity.ok(history) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/histories/{id}")
    public ResponseEntity<Void> deleteHistory(@PathVariable Long id) {
        historyService.deleteHistory(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/histories/user")
    public ResponseEntity<List<History>> getUserHistories() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<History> userHistories = historyService.getHistoriesByUserId(user.getId());
        return ResponseEntity.ok(userHistories);
    }
    
    @PostMapping("/jacobi/histories/save")
    public ResponseEntity<?> saveHistory(@RequestBody History history) {
        System.out.println("Request received for saving history: " + history);
        History savedHistory = historyService.createHistory(history);
        return ResponseEntity.ok(savedHistory);
    }


}

