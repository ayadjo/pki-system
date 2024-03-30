package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public User getByUsername(String username){
        return userRepository.findByMail(username);
    }


    public List<User> getUsers(){
        List<User> clients = new ArrayList<>();
        for(User c : userRepository.findAll()){
            if(Objects.equals(c.getRole().getName(), "USER" )){
                clients.add(c);
            }
        }
        return clients;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

}
