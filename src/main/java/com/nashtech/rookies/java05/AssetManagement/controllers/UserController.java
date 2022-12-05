package com.nashtech.rookies.java05.AssetManagement.controllers;

import com.google.gson.Gson;
import com.nashtech.rookies.java05.AssetManagement.dtos.request.ResetPasswordDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.request.UserRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.*;
import com.nashtech.rookies.java05.AssetManagement.controler.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    Gson gson;

    @Autowired
    UserService userService;

    @GetMapping("")
    public APIResponse<List<UserViewResponseDto>> getAllUsers(@RequestParam(required = false) List<String> types,
                                                              @RequestParam(required = false, defaultValue = "") String keyword,
                                                              @RequestParam int locationId,
                                                              @RequestParam(required = false, defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "updatedWhen_DESC") String orderBy) {
        return userService.getUsersByPredicates(types, keyword, locationId, page, orderBy);
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserById(@PathVariable String id) {
        UserResponseDto userResponseDto = userService.getUserById(id);
        if (userResponseDto == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("That user is not exist.");
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/{id}/disable")
    public ResponseEntity checkValidUserToDisable(@PathVariable String id){
        boolean checked=userService.checkUserValidToDisableById(id);
        UserValidToDisableDto userValidToDisableDto=new UserValidToDisableDto();
        if (checked){
            userValidToDisableDto.setTitle("User valid to disable");
            userValidToDisableDto.setMessage("success");
            return ResponseEntity.ok(userValidToDisableDto);
        }
        else {
            userValidToDisableDto.setTitle("Can not disable user");
            userValidToDisableDto.setMessage("There are valid assignments belonging to this user.\n" +
                    "Please close all assignments before disabling user.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userValidToDisableDto);
    }

    @GetMapping("/{id}/assignments")
    public ResponseEntity getListAssignmentsToOfUser(@PathVariable String id){
        return ResponseEntity.ok(userService.getListAssignmentsToOfUser(id));
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserRequestDto userRequestDto) throws ParseException {
        return ResponseEntity.ok(userService.createUser(userRequestDto));
    }

    @PostMapping("/reset-password")
    public ResponseEntity resetPassword(@RequestBody ResetPasswordDto resetPasswordDto,Authentication authentication){
        ChangePasswordDto changePasswordDto =userService.changePassword(authentication,resetPasswordDto);
        return ResponseEntity.status(changePasswordDto.getStatus()).body(changePasswordDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable("id") String id, @RequestBody UserRequestDto userRequestDto) throws ParseException {
        return ResponseEntity.ok(userService.updateUser(id, userRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity disableUserById(@PathVariable String id){
        boolean isDisabled= userService.disableUserById(id);
        if (isDisabled)    return ResponseEntity.ok("success");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed to disable that user");
    }
}

