package com.example.winninglog.Services;

import com.example.winninglog.Models.Project;
import com.example.winninglog.Models.User;
import com.example.winninglog.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DatabaseUserService implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired

    public DatabaseUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            return user.get();
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUserName(username);
        if (user.isPresent()){
            return user.get();
        }
        return null;
    }

    @Override
    public User save(User user) {
        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    /*
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteUser(id);
    }*/

    @Override
    public boolean checkPassword(String password) {
        int ltr = 0;
        int nmbr = 0;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLetter(password.charAt(i))){
                ltr++;
            }
            if (Character.isDigit(password.charAt(i))){
                nmbr++;
            }
        }
        if (ltr <= 0 || nmbr <= 0){
            return false;
        }
        return true;
    }

}
