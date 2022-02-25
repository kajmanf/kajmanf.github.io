package com.example.winninglog.Services;

import com.example.winninglog.Models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> findAll();
    User findById(Long id);
    User findByUsername(String username);

    User save(User user);
    void delete(User user);
    //void deleteUser(Long id);

    boolean checkPassword(String pwd);
}
