package com.example.backend.History;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findByUserId(Long user_id);
}

