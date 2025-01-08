package com.kirillpolyakov.service;

import com.kirillpolyakov.model.User;
import com.kirillpolyakov.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User get(long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User doesn't exist"));
    }

    @Override
    public void add(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        try {
            this.userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("User is already exist");
        }
    }

    @Override
    public User delete(long id) {
        User user  = this.get(id);
        this.userRepository.delete(user);
        return user;
    }
}
