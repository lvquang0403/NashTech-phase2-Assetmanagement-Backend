package com.nashtech.rookies.java05.AssetManagement.controler;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public APIResponse<List<UserViewResponseDto>> getAllUsers(@RequestParam(required = false) List<String> types,
                                                              @RequestParam(required = false, defaultValue = "") String keyword,
                                                              @RequestParam int locationId,
                                                              @RequestParam(required = false, defaultValue = "0") int page){
        return userService.getUsersByPredicates(types, keyword, locationId, page);
    }


}
