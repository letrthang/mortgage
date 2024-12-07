package com.squad1.hackathon.repository;

import com.squad1.hackathon.entity.User;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRepoImpl {

    private final UserRepository repository;

    public UserRepoImpl(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> findAll() {
        return repository.findAll();
    }
    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }
    public void delete(User entity) {
        repository.delete(entity);
    }
    public User saveAndUpdate(User entity) {
        return repository.save(entity);
    }
    public Optional<User> findByName(@Param("name") String name){
        return repository.findByName(name);
    }


}
