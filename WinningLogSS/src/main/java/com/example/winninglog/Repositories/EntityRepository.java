package com.example.winninglog.Repositories;

import com.example.winninglog.Models.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntityRepository extends JpaRepository <LogEntity, Long> {

}
