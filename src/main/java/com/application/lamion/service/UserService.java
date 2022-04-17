package com.application.lamion.service;

import com.application.lamion.model.User;
import com.application.lamion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User create(User data) {
        return repository.save(data);
    }

    public List<User> get() {
        return repository.findAll();
    }

    public User find(long id) {
        return repository.findById(id).orElse(null);
    }

    public User findByEmail(String email) {
        return repository.findFirstByEmail(email);
    }

    public boolean exists(long id) {
        return repository.existsById(id);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    public void update(User data) {
        repository.saveAndFlush(data);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }
}
