package com.example.backend.History;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;

    @Autowired
    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public List<History> getAllHistories() {
        List<History> histories = new ArrayList<>();

        historyRepository.findAll().forEach(histories::add);

        return histories;
    }
    public History getHistory(Long id){
        return historyRepository.findById(id).orElse(null);
    }
    public History createHistory(History input) {
        List<History> existingHistories = historyRepository.findByUserId(input.getUser().getId());
    if (!existingHistories.isEmpty()) {
    }

    History history = new History()
                        .setData(input.getData())
                        .setResult(input.getResult())
                        .setUser(input.getUser());

    return historyRepository.save(history);
    }
    public void deleteHistory(Long id){
        historyRepository.deleteById(id);
    }
    public List<History> getHistoriesByUserId(Long user_id) {
        return historyRepository.findByUserId(user_id);
    }    
}
