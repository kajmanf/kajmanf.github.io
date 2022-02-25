package com.example.winninglog.Services;

import com.example.winninglog.Models.LogEntity;
import com.example.winninglog.Repositories.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class DatabaseEntityService implements EntityService {

    private final EntityRepository entityRepository;

    @Autowired
    public DatabaseEntityService(EntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    public boolean checkDateInterval(String checkingDate, String startDate, String endDate) {
        try {
            Date start = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(startDate);
            Date end = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(endDate);
            Date checking = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(checkingDate);
            if (start.compareTo(checking) <= 0 && end.compareTo(checking) >= 0){
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<LogEntity> findAll() {
        return entityRepository.findAll();
    }

    @Override
    public LogEntity findById(Long id) {
        Optional<LogEntity> logEntity = entityRepository.findById(id);
        if (logEntity.isPresent()){
            return logEntity.get();
        }
        return null;
    }

    @Override
    public LogEntity save(LogEntity logEntity) {
        return entityRepository.save(logEntity);
    }

    @Override
    public void delete(LogEntity logEntity) {
        entityRepository.delete(logEntity);
    }
}
