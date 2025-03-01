package com.example.backend.History;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveHistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    public void saveHistory(History history) {
        historyRepository.save(history);
    }
}
