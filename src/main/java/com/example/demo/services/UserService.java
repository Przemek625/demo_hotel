package com.example.demo.services;


import com.example.demo.domain.User;

import java.util.List;

public interface UserService {

    User save(User user);
    List<User> findAll();
    void delete(long id);
}
