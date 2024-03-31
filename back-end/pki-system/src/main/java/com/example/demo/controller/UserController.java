package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping(value = "/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {

        List<User> users = userService.findAll();

        List<UserDto> usersDTO = new ArrayList<>();
        for (User u : users) {
            usersDTO.add(new UserDto(u));
        }

        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }
}
