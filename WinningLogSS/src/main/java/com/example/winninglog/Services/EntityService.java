package com.example.winninglog.Services;

import com.example.winninglog.Models.LogEntity;
import com.example.winninglog.Models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public interface EntityService {
    List<LogEntity> findAll();
    LogEntity findById(Long id);

    boolean checkDateInterval(String checkingDate, String startDate, String endDate);

    LogEntity save(LogEntity logEntity);
    void delete(LogEntity logEntity);
}
