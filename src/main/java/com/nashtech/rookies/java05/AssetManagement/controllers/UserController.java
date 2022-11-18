package com.nashtech.rookies.java05.AssetManagement.controllers;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.UserRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserResponseDto;
import com.nashtech.rookies.java05.AssetManagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserById(@PathVariable String id){
        UserResponseDto userResponseDto=userService.getUserById(id);
        if (userResponseDto==null)  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("That user is not exist.");
        return ResponseEntity.ok(userResponseDto);
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserRequestDto userRequestDto) throws ParseException {
        return ResponseEntity.ok(userService.createUser(userRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable("id") String id,@RequestBody UserRequestDto userRequestDto) throws ParseException {
        return ResponseEntity.ok(userService.updateUser(id,userRequestDto));
    }
}

