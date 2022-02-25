package com.example.winninglog.Repositories;

import com.example.winninglog.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);


    /*@Query(value = "DELETE FROM project_user u WHERE u.user_id = 14", nativeQuery = true)
    void deleteUser(Long id);*/
}
